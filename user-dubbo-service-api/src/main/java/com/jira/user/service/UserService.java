package com.jira.user.service;

import com.jira.user.dto.UserInfo;

public interface UserService {
    UserInfo getUserById(int id);
    UserInfo getUserByName(String userName);
}
