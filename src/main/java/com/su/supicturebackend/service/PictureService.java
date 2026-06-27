package com.su.supicturebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.su.supicturebackend.model.dto.pictrue.PictureUploadRequest;
import com.su.supicturebackend.model.entity.Picture;
import com.su.supicturebackend.model.entity.User;
import com.su.supicturebackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 83639
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2026-06-27 16:56:17
*/
public interface PictureService extends IService<Picture> {

    PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser);
}
