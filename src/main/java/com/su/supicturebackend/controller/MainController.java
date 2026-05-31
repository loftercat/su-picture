package com.su.supicturebackend.controller;

import com.su.supicturebackend.common.BaseResponse;
import com.su.supicturebackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NoPwd
 * @version 1.0
 * @description: TODO
 * @date 2026/5/22 12:45
 */
@RestController
@RequestMapping("/")
public class MainController {

    /**
     * 健康检查接口方法
     * 用于检查服务是否正常运行
     * @return 返回一个包含成功状态和消息的BaseResponse对象
     */
    @GetMapping("/health")
    public BaseResponse<String> health() {
        // 使用ResultUtils工具类创建一个成功的响应结果
        return ResultUtils.success("ok");
    }
}
