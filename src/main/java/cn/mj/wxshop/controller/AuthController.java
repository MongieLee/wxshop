package cn.mj.wxshop.controller;

import cn.mj.wxshop.service.AuthService;
import cn.mj.wxshop.service.TelVerficationService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final TelVerficationService telVerficationService;

    public AuthController(AuthService authService, TelVerficationService telVerficationService) {
        this.authService = authService;
        this.telVerficationService = telVerficationService;
    }

    @PostMapping("/code")
    public void code(@RequestBody TelAndCode telAndCode, HttpServletResponse response) {
        if (telVerficationService.verifyTelParameter(telAndCode)) {
            authService.sendVerification(telAndCode.getTel());
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    @PostMapping("/login")
    public void login(@RequestBody TelAndCode telAndCode) {
        UsernamePasswordToken token = new UsernamePasswordToken(
                telAndCode.getTel(),
                telAndCode.getCode());
        token.setRememberMe(true);
        SecurityUtils.getSubject().login(token);
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
