package cn.mj.wxshop.entity;

public class CommonResult extends Result {
    protected CommonResult(Result.ResultEnum status, String msg) {
        super(status, msg, null);
    }

    public static CommonResult success(String msg) {
        return new CommonResult(Result.ResultEnum.SUCCESSFUL, msg);
    }

    public static CommonResult failure(String msg) {
        return new CommonResult(Result.ResultEnum.FAILURE, msg);
    }
}
