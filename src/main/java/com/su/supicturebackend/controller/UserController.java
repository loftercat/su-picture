package com.su.supicturebackend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.su.supicturebackend.annotation.AuthCheck;
import com.su.supicturebackend.common.BaseResponse;
import com.su.supicturebackend.common.DeleteRequest;
import com.su.supicturebackend.common.ResultUtils;
import com.su.supicturebackend.constant.UserConstant;
import com.su.supicturebackend.exception.ErrorCode;
import com.su.supicturebackend.exception.ThrowUtils;
import com.su.supicturebackend.model.dto.UserAddRequest;
import com.su.supicturebackend.model.dto.UserLoginRequest;
import com.su.supicturebackend.model.dto.UserRegisterRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.su.supicturebackend.model.dto.UserUpdateRequest;
import com.su.supicturebackend.model.dto.UserQueryRequest;
import com.su.supicturebackend.model.entity.User;
import com.su.supicturebackend.model.enums.UserRoleEnum;
import com.su.supicturebackend.model.vo.LoginUserVO;
import com.su.supicturebackend.model.vo.UserVO;
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

    //创建用户
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        //默认密码
        String DEFAULT_PASSWORD = "123456";
        user.setUserPassword(userService.getEncryptedPassword(DEFAULT_PASSWORD));
        userService.save(user);
        return ResultUtils.success(user.getId());
    }


    //根据id获取用户(仅管理员)
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/get/{id}")
    public BaseResponse<User> getUserById(@PathVariable long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    //根据id获取用户
    @GetMapping("/get/vo/{id}")
    public BaseResponse<UserVO> getUserVOById(@PathVariable long id) {
        BaseResponse<User> response = this.getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    //分页查询用户
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/list/page")
    public BaseResponse<Page<UserVO>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<UserVO> userVOPage = userService.listUserByPage(userQueryRequest);
        return ResultUtils.success(userVOPage);
    }

    //删除用户
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserById(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(deleteRequest.getId() == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(deleteRequest.getId());
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    //更新用户
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.updateUser(userUpdateRequest);
        return ResultUtils.success(result);
    }



}
