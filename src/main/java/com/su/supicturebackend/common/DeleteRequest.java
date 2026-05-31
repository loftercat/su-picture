package com.su.supicturebackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author NoPwd
 * @version 1.0
 * @description: 删除通用请求实体
 * @date 2026/5/22 11:56
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 1439515443460374503L;
    private Long id;


}
