package com.su.supicturebackend.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.su.supicturebackend.common.ResultUtils;
import com.su.supicturebackend.config.CosClientConfig;
import com.su.supicturebackend.exception.BusinessException;
import com.su.supicturebackend.exception.ErrorCode;
import com.su.supicturebackend.exception.ThrowUtils;
import com.su.supicturebackend.model.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FileManager {

    @Resource
    private CosManager cosManager;

    @Resource
    private CosClientConfig cosClientConfig;

    /**
     * 上传图片
     *
     * @return 上传结果
     */
    public UploadPictureResult uploadFile(MultipartFile multipartFile, String pathPrefix) {
        //1.校验图片
        validatePicture(multipartFile);
        //图片上传地址
        String uuid = RandomUtil.randomString(32);
        String originalFilename = multipartFile.getOriginalFilename();
        String uploadFileName = String.format("%s_/%s.%s", DateUtil.formatDate(new Date()), uuid, FileUtil.getSuffix(originalFilename));
        String uploadPath = String.format("%s/%s", pathPrefix, uploadFileName);
        File file = null;
        try {
            file = File.createTempFile(uploadPath, null);
            multipartFile.transferTo(file);
            PutObjectResult putObjectResult = cosManager.putObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();

            UploadPictureResult uploadPictureResult = new UploadPictureResult();
            uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
            uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
            uploadPictureResult.setPicSize(FileUtil.size(file));
            uploadPictureResult.setPicWidth(imageInfo.getWidth());
            uploadPictureResult.setPicHeight(imageInfo.getHeight());
            uploadPictureResult.setPicScale(NumberUtil.round(imageInfo.getWidth() * 1.0 / imageInfo.getHeight(), 2).doubleValue());
            return uploadPictureResult;
        } catch (Exception e) {
            log.error("图片上传到对象存储失败:", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传文件失败");
        } finally {
            //清理临时文件
            deleteTempFile(file);
        }
    }

    private static void deleteTempFile(File file) {
        if (file != null) {
            boolean delete = file.delete();
            if (!delete) {
                log.error("delete file failed = " + file.getAbsolutePath());
            }
        }
    }

    private void validatePicture(MultipartFile file) {
        ThrowUtils.throwIf(file == null, ErrorCode.PARAMS_ERROR, "图片为空");
        final long ONE_Max = 1024 * 1024;
        ThrowUtils.throwIf(file.getSize() > ONE_Max, ErrorCode.PARAMS_ERROR, "图片大小超出限制");
        String fileSuffix = FileUtil.getSuffix(file.getOriginalFilename());
        List<String> imageType = Arrays.asList("jpg", "jpeg", "png", "webp");
        ThrowUtils.throwIf(imageType.contains(fileSuffix), ErrorCode.PARAMS_ERROR, "图片格式不支持");
    }

}
