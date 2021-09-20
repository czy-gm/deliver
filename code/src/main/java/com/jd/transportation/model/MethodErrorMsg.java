package com.jd.transportation.model;

/**
 * 方法错误信息
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public class MethodErrorMsg {

    //错误码
    private int code;

    //错误信息
    private String errMsg;

    public MethodErrorMsg(int code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "MethodErrorMsg{" +
                "code=" + code +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
