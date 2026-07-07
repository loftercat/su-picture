package com.su.supicturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.su.supicturebackend.exception.ErrorCode;
import com.su.supicturebackend.exception.ThrowUtils;
import com.su.supicturebackend.manager.FileManager;
import com.su.supicturebackend.mapper.PictureMapper;
import com.su.supicturebackend.model.dto.pictrue.PictureUploadRequest;
import com.su.supicturebackend.model.entity.Picture;
import com.su.supicturebackend.model.entity.User;
import com.su.supicturebackend.model.file.UploadPictureResult;
import com.su.supicturebackend.model.vo.PictureVO;
import com.su.supicturebackend.service.PictureService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 83639
 * @description 针对表【picture(图片)】的数据库操作Service实现
 * @createDate 2026-06-27 16:56:17
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {

    @Resource
    private FileManager fileManager;

    @Override
    public PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser) {
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NO_AUTH_ERROR);
        ThrowUtils.throwIf(pictureUploadRequest == null, ErrorCode.PARAMS_ERROR, "参数为空");
        //判断是新增还是删除
        Long id = pictureUploadRequest.getId();
        //更新
        if (id != null) {
            //判断是否存在
            boolean exists = lambdaQuery().eq(Picture::getId, id).exists();
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "图片不存在");
        }
        //上传图片
        String uploadPrefix = String.format("public/%s", loginUser.getId());
        UploadPictureResult uploadPictureResult = fileManager.uploadFile(multipartFile, uploadPrefix);
        //构造入库
        Picture picture = getPicture(loginUser, uploadPictureResult, id);
        boolean result = this.saveOrUpdate(picture);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "图片上传失败，数据库操作失败");
        return PictureVO.objToVo(picture);
    }

    @NotNull
    private Picture getPicture(User loginUser, UploadPictureResult uploadPictureResult, Long id) {
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setName(uploadPictureResult.getPicName());
        picture.setPicSize(uploadPictureResult.getPicSize());
        picture.setPicWidth(uploadPictureResult.getPicWidth());
        picture.setPicHeight(uploadPictureResult.getPicHeight());
        picture.setPicScale(uploadPictureResult.getPicScale());
        picture.setPicFormat(uploadPictureResult.getPicFormat());
        picture.setUserId(loginUser.getId());
        if (id != null) {
            //更新操作
            picture.setId(id);
            picture.setEditTime(new Date());
        }
        return picture;
    }
}




