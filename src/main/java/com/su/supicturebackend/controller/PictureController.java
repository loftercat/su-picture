package com.su.supicturebackend.controller;

import com.su.supicturebackend.annotation.AuthCheck;
import com.su.supicturebackend.common.BaseResponse;
import com.su.supicturebackend.common.ResultUtils;
import com.su.supicturebackend.constant.UserConstant;
import com.su.supicturebackend.model.dto.pictrue.PictureUploadRequest;
import com.su.supicturebackend.model.entity.User;
import com.su.supicturebackend.model.vo.PictureVO;
import com.su.supicturebackend.service.PictureService;
import com.su.supicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/upload")
    public BaseResponse<PictureVO> uploadPicture(@RequestPart("file") MultipartFile multipartFile
            , PictureUploadRequest pictureUploadRequest
            , HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PictureVO pictureVO = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return ResultUtils.success(pictureVO);
    }
}
