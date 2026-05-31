package com.su.supicturebackend.exception;

import lombok.Getter;

/**
 * @author NoPwd
 * @version 1.0
 * @description: 自定义业务异常
 * @date 2026/5/22 11:15
 */

@Getter
public class BusinessException extends RuntimeException {

    /**
     * 状态码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
