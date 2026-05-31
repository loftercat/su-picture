package com.su.supicturebackend.controller;

import com.su.supicturebackend.annotation.AuthCheck;
import com.su.supicturebackend.common.BaseResponse;
import com.su.supicturebackend.common.ResultUtils;
import com.su.supicturebackend.constant.UserConstant;
import com.su.supicturebackend.exception.ErrorCode;
import com.su.supicturebackend.exception.ThrowUtils;
import com.su.supicturebackend.model.dto.UserLoginRequest;
import com.su.supicturebackend.model.dto.UserRegisterRequest;
import com.su.supicturebackend.model.entity.User;
import com.su.supicturebackend.model.enums.UserRoleEnum;
import com.su.supicturebackend.model.vo.LoginUserVO;
import com.su.supicturebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author NoPwd
 * @version 1.0
 * @description: TODO
 * @date 2026/5/25 18:42
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    //用户注册
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        long userId = userService.userRegister(userRegisterRequest.getUserAccount(),
                userRegisterRequest.getUserPassword(),
                userRegisterRequest.getCheckPassword());
        return ResultUtils.success(userId);
    }

    //用户登录
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        LoginUserVO loginUserVO = userService.userLogin(userLoginRequest.getUserAccount(),
                userLoginRequest.getUserPassword(),
                request);
        return ResultUtils.success(loginUserVO);
    }

    //用户登录注销
    @PostMapping("/logout")
    public BaseResponse<LoginUserVO> userLogOut(HttpServletRequest request) {
        User loginUser = userService.userLogOut(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    //获取登录用户信息
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoinUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }


}
