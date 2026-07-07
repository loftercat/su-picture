package com.su.supicturebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.su.supicturebackend.constant.UserConstant;
import com.su.supicturebackend.model.dto.user.UserQueryRequest;
import com.su.supicturebackend.model.dto.user.UserUpdateRequest;
import com.su.supicturebackend.model.enums.UserRoleEnum;
import com.su.supicturebackend.exception.BusinessException;
import com.su.supicturebackend.exception.ErrorCode;
import com.su.supicturebackend.exception.ThrowUtils;
import com.su.supicturebackend.model.entity.User;
import com.su.supicturebackend.model.vo.LoginUserVO;
import com.su.supicturebackend.model.vo.UserVO;
import com.su.supicturebackend.service.UserService;
import com.su.supicturebackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author NoPwd
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2026-05-25 18:03:17
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验参数
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        //检查用户账号是否小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        //检查用户密码是否小于8位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        //检查密码是否重复
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不一致");
        }
        //2.检查用户账号是否和数据库中已有的重复
        Long count = this.baseMapper.selectCount(new QueryWrapper<User>().eq("userAccount", userAccount));
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号已存在");
        }
        //3.密码一定要加密
        String encryptedPassword = getEncryptedPassword(userPassword);
        //4.将用户信息存入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户注册失败，数据库错误");
        }
        //save 主键回填
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        //检查用户账号是否小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号或密码错误");
        }
        //检查用户密码是否小于8位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号或密码错误");
        }
        //2.对用户的密码加密
        String encryptedPassword = getEncryptedPassword(userPassword);
        //3.查询用户是否存在
        User user = this.baseMapper.selectOne(new QueryWrapper<User>().
                eq("userAccount", userAccount).
                eq("userPassword", encryptedPassword));
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        //4.保存用户状态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public String getEncryptedPassword(String password) {
        final String salt = "su";
        return DigestUtils.md5DigestAsHex((salt + password).getBytes());
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        //从session中获取用户信息
        User currentUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (null == currentUser) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //数据库查询。可能有用户登录后，被删除等特殊场景，严格校验
        currentUser = this.getById(currentUser.getId());
        if (null == currentUser) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public User userLogOut(HttpServletRequest request) {
        User currentUser = (User)request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (null == currentUser) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户未登录");
        }
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return currentUser;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (null == user) {
            return null;
        }
       LoginUserVO loginUserVO =  new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return Collections.emptyList();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public Page<UserVO> listUserByPage(UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 构建查询条件
        QueryWrapper<User> queryWrapper = getQueryWrapper(userQueryRequest);
        // 分页查询
        Page<User> page = this.page(
                new Page<>(userQueryRequest.getCurrent(), userQueryRequest.getPageSize()),
                queryWrapper
        );
        // 转换为VO分页对象
        Page<UserVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(getUserVOList(page.getRecords()));
        return voPage;
    }

    @Override
    public boolean updateUser(UserUpdateRequest userUpdateRequest) {
        // 1.校验参数
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 2.检查用户是否存在
        User oldUser = this.getById(userUpdateRequest.getId());
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        // 3.校验用户角色是否合法
        String userRole = userUpdateRequest.getUserRole();
        if (StrUtil.isNotBlank(userRole)) {
            UserRoleEnum roleEnum = UserRoleEnum.getEnumByValue(userRole);
            if (roleEnum == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户角色不合法");
            }
        }
        // 4.校验用户昵称长度
        String userName = userUpdateRequest.getUserName();
        if (StrUtil.isNotBlank(userName) && userName.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户昵称过长");
        }
        // 5.校验用户简介长度
        String userProfile = userUpdateRequest.getUserProfile();
        if (StrUtil.isNotBlank(userProfile) && userProfile.length() > 200) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户简介过长");
        }
        // 6.更新用户信息
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        return this.updateById(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }
}




