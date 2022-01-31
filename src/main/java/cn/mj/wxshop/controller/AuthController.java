package cn.mj.wxshop.controller;

import cn.mj.wxshop.entity.AuthResult;
import cn.mj.wxshop.entity.CommonResult;
import cn.mj.wxshop.generate.User;
import cn.mj.wxshop.service.AuthService;
import cn.mj.wxshop.service.TelVerificationService;
import cn.mj.wxshop.service.UserContent;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final TelVerificationService telVerficationService;

    public AuthController(AuthService authService, TelVerificationService telVerficationService) {
        this.authService = authService;
        this.telVerficationService = telVerficationService;
    }

    @PostMapping("/code")
    public Object code(@RequestBody TelAndCode telAndCode, HttpServletResponse response) {
        if (telVerficationService.verifyTelParameter(telAndCode)) {
            authService.sendVerification(telAndCode.getTel());
            return CommonResult.success("验证码已发送");
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return CommonResult.failure("请填写正确的手机号码");
        }
    }

    @GetMapping("/isLogin")
    public Object code() {
        User currentUser = UserContent.getCurrentUser();
        if (currentUser == null) {
            return AuthResult.failure("未登录");
        } else {
            return AuthResult.success("已登录", currentUser);
        }
    }

    @GetMapping("/logout")
    public Object logout() {
        SecurityUtils.getSubject().logout();
        return CommonResult.success("注销成功");
    }

    @PostMapping("/login")
    public Object login(@RequestBody TelAndCode telAndCode) {
        UsernamePasswordToken token = new UsernamePasswordToken(
                telAndCode.getTel(),
                telAndCode.getCode());
        token.setRememberMe(true);
        SecurityUtils.getSubject().login(token);
        return AuthResult.success("登录成功");
    }

    public static class TelAndCode {
        private String code;
        private String tel;

        public TelAndCode(String code, String tel) {
            this.code = code;
            this.tel = tel;
        }

        public TelAndCode(String tel) {
            this.tel = tel;
        }

        public TelAndCode() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }
}
