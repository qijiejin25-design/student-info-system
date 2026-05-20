package com.qijiejin.studentinfo.service;

import com.qijiejin.studentinfo.entity.SysUser;
import com.qijiejin.studentinfo.exception.BusinessException;
import com.qijiejin.studentinfo.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final SysUserMapper userMapper;

    public AuthService(SysUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public SysUser login(String username, String password) {
        SysUser u = userMapper.findByUsername(username);
        if (u == null || !u.getPassword().equals(password)) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        return u;
    }
}
