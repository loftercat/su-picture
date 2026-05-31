package com.su.supicturebackend.common;

import com.su.supicturebackend.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author NoPwd
 * @version 1.0
 * @description: 基础响应实体
 * @date 2026/5/22 11:29
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }


}
