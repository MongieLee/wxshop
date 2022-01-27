package cn.mj.wxshop.service;

public interface SmsCodeService {
    /**
     * 向指定的手机号发验证码，返回正确答案
     * @param tel 目标手机号
     * @return 正确答案
     */
    String sendSmsCode(String tel);
}
