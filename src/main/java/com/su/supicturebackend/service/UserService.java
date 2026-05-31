package com.su.supicturebackend.service;

import com.su.supicturebackend.model.vo.LoginUserVO;
import com.su.supicturebackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.su.supicturebackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author NoPwd
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2026-05-25 18:03:17
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 返回一个long类型的结果，可能是表示注册状态或用户ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request HTTP请求对象，用于获取请求相关信息
     * @return LoginUserVO 登录成功后返回的用户视图对象，包含用户相关信息,脱敏后数据
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取加密后的密码
     * @param password 原始密码字符串
     * @return 加密后的密码字符串
     */
    String getEncryptedPassword(String password);

    /**
     * 根据HTTP请求获取登录用户信息
     *
     * @param request HTTP请求对象，包含客户端请求信息
     * @return User 返回已登录的用户对象，如果用户未登录则可能返回null
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户登出
     * @param request HTTP请求对象，用于获取请求相关信息
     * @return User 返回已登出的用户对象，如果用户未登录则可能返回null
     */
    User userLogOut(HttpServletRequest request);

    /**
     * user转换为返回前端的VO对象
     * @param user 用户对象，包含用户的完整信息
     * @return LoginUserVO 登录用户视图对象，通常包含登录后需要返回给前端的部分用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * user转换为返回前端的VO对象
     * @param user 用户对象，包含用户的完整信息
     * @return UserVO 用户视图对象，通常包含用户的基本信息
     */
    UserVO getUserVO(User user);

    /**
     * user转换为返回前端的VO对象列表
     * @param userList 用户对象列表，包含用户的完整信息
     * @return List<UserVO> 用户视图对象列表，通常包含用户的基本信息
     */
    List<UserVO> getUserVOList(List<User> userList);
}
