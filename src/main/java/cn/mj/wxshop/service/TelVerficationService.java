package cn.mj.wxshop.service;

import cn.mj.wxshop.controller.AuthController;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TelVerficationService {
    private static Pattern TEL_PATTERN = Pattern.compile("1\\d{10}");

    public boolean verifyTelParameter(AuthController.TelAndCode param) {
        if (param == null || param.getTel() == null) {
            return false;
        } else {
            return TEL_PATTERN.matcher(param.getTel()).find();
        }
    }
}
