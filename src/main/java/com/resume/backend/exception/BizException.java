package com.resume.backend.exception;

/**
 * 业务异常
 * 在业务运行中，抛出的异常错误
 *
 * @author: Amadeus
 * @date: 2025-01-01
 */
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private String code;

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}