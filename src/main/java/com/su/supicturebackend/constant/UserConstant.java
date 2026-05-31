package com.su.supicturebackend.constant;

/**
 * 用户常量接口
 * 该接口用于定义系统中与用户相关的常量值
 * 通过接口定义常量可以统一管理用户相关的固定值，便于维护和使用
 */
public interface UserConstant {
    /**
     * 用户登录状态
     */
    String USER_LOGIN_STATE = "user_login_state";

    //region 权限
    /**
     * 用户角色
     */
    String USER_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";
    //endregion
}
