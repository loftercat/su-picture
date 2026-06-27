package com.su.supicturebackend.controller;

import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.utils.IOUtils;
import com.su.supicturebackend.annotation.AuthCheck;
import com.su.supicturebackend.common.BaseResponse;
import com.su.supicturebackend.common.ResultUtils;
import com.su.supicturebackend.constant.UserConstant;
import com.su.supicturebackend.exception.BusinessException;
import com.su.supicturebackend.exception.ErrorCode;
import com.su.supicturebackend.manager.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private CosManager cosManager;

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/test/upload")
    public BaseResponse<String> upload(@RequestPart("file") MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        String filepath = String.format("test/%s", filename);
        File file= null;
        try {
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            return ResultUtils.success(filepath);
        } catch (Exception e) {
            log.error("upload file failed = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传文件失败");
        } finally {
            if (file != null) {
                boolean delete = file.delete();
                if (!delete) {
                    log.error("delete file failed = " + filepath);
                }
            }
        }
    }

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/test/download")
    public void download(@RequestParam("filepath") String filepath, HttpServletResponse response) {
        try (COSObject cosObject = cosManager.getObject(filepath)) {
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filepath + "\"");
            IOUtils.copy(cosObject.getObjectContent(), outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("download file failed = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载文件失败");
        }
    }
}