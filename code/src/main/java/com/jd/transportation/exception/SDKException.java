package com.jd.transportation.exception;

/**
 * 业务异常（包括非法参数、空数据等）
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public class SDKException extends Exception {

    public SDKException() {
    }

    public SDKException(String message) {
        super(message);
    }
}
