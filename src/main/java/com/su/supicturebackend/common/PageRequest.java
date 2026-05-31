package com.su.supicturebackend.common;

import lombok.Data;

/**
 * @author NoPwd
 * @version 1.0
 * @description: 通用分页请求类实体
 * @date 2026/5/22 11:53
 */
@Data
public class PageRequest {

    /**
     * 当前页码
     */
    private int current = 1;

    /**
     * 每页大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（asc/desc）,默认升序
     */
    private String sortOrder = "desc";
}
