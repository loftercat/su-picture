package com.su.supicturebackend.common;

import com.su.supicturebackend.exception.ErrorCode;

/**
 * @author NoPwd
 * @version 1.0
 * @description: 响应工具类，用于快速构建BaseResponse
 * @date 2026/5/22 11:37
 */
public class ResultUtils {

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return BaseResponse
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, data, "ok");
    }

    /**
     * 成功响应（无数据）
     *
     * @param <T> 数据类型
     * @return BaseResponse
     */
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(200, null, "ok");
    }

    /**
     * 失败响应（使用ErrorCode）
     *
     * @param errorCode 错误码枚举
     * @param <T>       数据类型
     * @return BaseResponse
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败响应（使用ErrorCode + 自定义消息）
     *
     * @param errorCode 错误码枚举
     * @param message   自定义错误消息
     * @param <T>       数据类型
     * @return BaseResponse
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }

    /**
     * 失败响应（自定义code和消息）
     *
     * @param code    状态码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return BaseResponse
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }
}
