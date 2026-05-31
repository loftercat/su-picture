package com.su.supicturebackend.exception;

/**
 * @author NoPwd
 * @version 1.0
 * @description: 异常处理工具类
 * @date 2026/5/22 11:21
 */
public class ThrowUtils {

    /**
     * 条件成立，抛出异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * @description: 条件成立，抛出异常
     * @param: [condition, errorCode]
     * @return: void
     * @author NoPwd
     * @date: 2026/5/22 11:24
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    public static void throwIf(boolean condition, ErrorCode errorCode, String massage) {
        throwIf(condition, new BusinessException(errorCode, massage));
    }

}
