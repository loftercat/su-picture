package com.su.supicturebackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author NoPwd
 * @version 1.0
 * @description: TODO
 * @date 2026/5/25 18:19
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -1318865973059591788L;

    // 用户账号，用于登录的唯一标识
    private String userAccount;

    // 用户密码，经过加密处理的字符串
    private String userPassword;

}
