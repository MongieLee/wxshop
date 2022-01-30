package cn.mj.wxshop.service;

import cn.mj.wxshop.controller.AuthController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TelVerificationServiceTest {

    public static final AuthController.TelAndCode VALID_PARAMETER = new AuthController.TelAndCode("13232251037");
    public static final AuthController.TelAndCode VALID_PARAMETER_CODE = new AuthController.TelAndCode("888888","13232251037");
    public static final AuthController.TelAndCode EMPTY_TEL = new AuthController.TelAndCode();

    @Test
    public void returnTrueIfValid() {
        Assertions.assertTrue(new TelVerificationService().verifyTelParameter(VALID_PARAMETER));
    }

    @Test
    public void returnFalseIfInvalid() {
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(EMPTY_TEL));
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(null));
    }
}
