package com.su.supicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author NoPwd
 * @version 1.0
 * @description: TODO
 * @date 2026/5/25 18:19
 */
@Data
public class UserRegisterRequest implements Serializable {
    // 序列化版本UID，用于控制版本兼容性
    private static final long serialVersionUID = 2123751449422943908L;

    // 用户账号，用于登录的唯一标识
    private String userAccount;

    // 用户密码，经过加密处理的字符串
    private String userPassword;

    // 确认密码，用于验证用户输入密码的一致性
    private String checkPassword;
}
