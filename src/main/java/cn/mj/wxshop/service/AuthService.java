package cn.mj.wxshop.service;

import cn.mj.wxshop.generate.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final VerificationCodeCheckService verificationCodeCheckService;
    private final SmsCodeService smsCodeService;

    public AuthService(UserService userService, VerificationCodeCheckService verificationCodeCheckService, SmsCodeService smsCodeService) {
        this.userService = userService;
        this.verificationCodeCheckService = verificationCodeCheckService;
        this.smsCodeService = smsCodeService;
    }

    public User sendVerification(String tel) {
       User user =  userService.createUserIfExist(tel);
       String correctCode = smsCodeService.sendSmsCode(tel);
       verificationCodeCheckService.addCode(tel,correctCode);
       return user;
    }
}
