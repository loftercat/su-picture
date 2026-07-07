package com.su.supicturebackend.model.dto.pictrue;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PictureEditRequest implements Serializable {

    // 图片id
    private Long id;

    // 名称
    private String name;

    // 简介
    private String introduction;

    // 分类和标签
    private String category;

    // 标签
    private List<String> tags;

    private static final long serialVersionUID = 1L;

}
