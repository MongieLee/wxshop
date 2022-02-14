package cn.mj.wxshop.configuration;

import cn.mj.wxshop.service.CustomShiroRealm;
import cn.mj.wxshop.service.UserLoginInterceptor;
import cn.mj.wxshop.service.VerificationCodeCheckService;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig implements WebMvcConfigurer {
    private final UserLoginInterceptor userLoginInterceptor;
    private final ShiroLoginFilter shiroLoginFilter;

    public ShiroConfig(UserLoginInterceptor userLoginInterceptor, ShiroLoginFilter shiroLoginFilter) {
        this.userLoginInterceptor = userLoginInterceptor;
        this.shiroLoginFilter = shiroLoginFilter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor);
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        ThreadContext.bind(securityManager);
        Map<String, String> pattern = new HashMap<>();
        pattern.put("/api/code", "anon");
        pattern.put("/api/login", "anon");
        pattern.put("/api/register", "anon");
        pattern.put("/api/isLogin", "anon");
        pattern.put("/**", "authc");

        Map<String, Filter> filtersMap = new LinkedMap();
        filtersMap.put("ShiroLoginFilter", shiroLoginFilter);
        shiroFilterFactoryBean.setFilters(filtersMap);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(pattern);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(CustomShiroRealm shiroRealm) {
        DefaultSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
        // securityManager.setSessionManager(new DefaultSessionManager());
        securityManager.setSessionManager(new DefaultWebSessionManager());
        return securityManager;
    }

    @Bean
    public CustomShiroRealm shiroRealm(VerificationCodeCheckService verificationCodeCheckService) {
        return new CustomShiroRealm(verificationCodeCheckService);
    }
}
