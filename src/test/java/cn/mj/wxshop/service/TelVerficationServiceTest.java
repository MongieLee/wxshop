package cn.mj.wxshop.service;

import cn.mj.wxshop.controller.AuthController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TelVerficationServiceTest {

    private static final AuthController.TelAndCode VALID_PARAMETER = new AuthController.TelAndCode("13232251037");
    private static final AuthController.TelAndCode EMPTY_TEL = new AuthController.TelAndCode();

    @Test
    public void returnTrueIfValid() {
        Assertions.assertTrue(new TelVerficationService().verifyTelParameter(VALID_PARAMETER));
    }

    @Test
    public void returnFalseIfInvalid() {
        Assertions.assertFalse(new TelVerficationService().verifyTelParameter(EMPTY_TEL));
        Assertions.assertFalse(new TelVerficationService().verifyTelParameter(null));
    }
}
