package cn.mj.wxshop.entity;

public abstract class Result<T> {
    ResultEnum status;
    String msg;
    T data;

    protected Result(ResultEnum status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    protected Result() {
    }

    public ResultEnum getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public enum ResultEnum {
        SUCCESSFUL("success"),
        FAILURE("failure");

        private String status;

        ResultEnum(String status) {
            this.status = status;
        }

        public String getValue() {
            return this.status;
        }
    }
}
