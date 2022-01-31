package cn.mj.wxshop.configuration;

import cn.mj.wxshop.service.CustomShiroRealm;
import cn.mj.wxshop.service.UserLoginInterceptor;
import cn.mj.wxshop.service.VerificationCodeCheckService;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig implements WebMvcConfigurer {
    private final UserLoginInterceptor userLoginInterceptor;

    public ShiroConfig(UserLoginInterceptor userLoginInterceptor) {
        this.userLoginInterceptor = userLoginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor);
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> pattern = new HashMap<>();
        pattern.put("/api/code", "anon");
        pattern.put("/api/login", "anon");
        pattern.put("/api/register", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(pattern);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(CustomShiroRealm shiroRealm) {
        DefaultSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
//        securityManager.setSessionManager(new DefaultSessionManager());
        securityManager.setSessionManager(new DefaultWebSessionManager());
        return securityManager;
    }

    @Bean
    public CustomShiroRealm shiroRealm(VerificationCodeCheckService verificationCodeCheckService) {
        return new CustomShiroRealm(verificationCodeCheckService);
    }
}
