package com.su.supicturebackend.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

@Getter
public enum UserRoleEnum {
    USER("用户", "user"),
    ADMIN("管理员", "admin");

    private final String text;
    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据枚举值获取对应的枚举文本
     * @param value 枚举值
     * @return 对应的枚举
     */
    public static UserRoleEnum getEnumByValue(String value) {
    // 如果传入的值为空，直接返回null
        if (StrUtil.isEmpty(value)) {
            return null;
        }
    // 遍历所有的枚举值
        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
        // 检查当前枚举的值是否与传入的值匹配
            if (roleEnum.getValue().equals(value)) {
            // 如果匹配，返回对应的枚举文本
                return roleEnum;
            }
        }
    // 如果没有找到匹配的枚举，返回null
        return null;
    }
}
