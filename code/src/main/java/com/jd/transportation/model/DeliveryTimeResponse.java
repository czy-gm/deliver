package com.jd.transportation.model;

/**
 * 查询配送时效数据的返回类
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public class DeliveryTimeResponse {

    public static final int NORMAL = 0;

    public static final int ABNORMAL = 1;

    //错误码 0：正常 1：异常（参数错误、无对应数据等）
    private int code;

    //配送时效结果
    private String time;

    //错误信息
    private String errMsg;

    public DeliveryTimeResponse(int code, String time, String errMsg) {
        this.code = code;
        this.time = time;
        this.errMsg = errMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
