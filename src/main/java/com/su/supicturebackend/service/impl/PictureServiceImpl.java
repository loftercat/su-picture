package com.su.supicturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.su.supicturebackend.mapper.PictureMapper;
import com.su.supicturebackend.model.dto.pictrue.PictureUploadRequest;
import com.su.supicturebackend.model.entity.Picture;
import com.su.supicturebackend.model.entity.User;
import com.su.supicturebackend.model.vo.PictureVO;
import com.su.supicturebackend.service.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 83639
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2026-06-27 16:56:17
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService {

    @Override
    public PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser) {
        return null;
    }
}




