package cn.mj.wxshop.controller;

import cn.mj.wxshop.service.AuthService;
import cn.mj.wxshop.service.TelVerificationService;
import cn.mj.wxshop.service.UserContent;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

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
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("success", true);
        return objectObjectHashMap;
    }

    @GetMapping("/isLogin")
    public Object code() {
        if (UserContent.getCurrentUser() == null) {
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("isLogin", false);
            return objectObjectHashMap;
        } else {
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("isLogin", true);
            return objectObjectHashMap;
        }
    }

    @GetMapping("/logout")
    public Object logout() {
        SecurityUtils.getSubject().logout();
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("success", true);
        return objectObjectHashMap;
    }

    @PostMapping("/login")
    public Object login(@RequestBody TelAndCode telAndCode) {
        UsernamePasswordToken token = new UsernamePasswordToken(
                telAndCode.getTel(),
                telAndCode.getCode());
        token.setRememberMe(true);
        SecurityUtils.getSubject().login(token);
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("success", true);
        return objectObjectHashMap;
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
