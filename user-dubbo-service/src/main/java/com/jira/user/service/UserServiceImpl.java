package com.jira.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jira.user.dto.UserInfo;
import com.jira.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserInfo getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public UserInfo getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }
}
