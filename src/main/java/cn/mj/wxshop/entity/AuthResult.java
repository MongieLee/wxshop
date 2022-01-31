package cn.mj.wxshop.entity;

import cn.mj.wxshop.generate.User;

public class AuthResult extends Result<User> {
    boolean isLogin;

    protected AuthResult(ResultEnum status, String msg, boolean isLogin, User data) {
        super(status, msg, data);
        this.isLogin = isLogin;
    }

    public AuthResult() {
    }

    public static AuthResult success(String msg, User data) {
        return new AuthResult(ResultEnum.SUCCESSFUL, msg, true, data);
    }

    public static AuthResult success(String msg) {
        return new AuthResult(ResultEnum.SUCCESSFUL, msg, true, null);
    }

    public static AuthResult failure(String msg) {
        return new AuthResult(ResultEnum.FAILURE, msg, false, null);
    }

    public boolean getIsLogin() {
        return isLogin;
    }
}
