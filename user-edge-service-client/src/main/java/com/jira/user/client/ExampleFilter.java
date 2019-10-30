package com.jira.user.client;

import com.jira.user.dto.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//被访问服务在身份验证成功后，可以在loginAfter方法中实现需要的操作
public class ExampleFilter extends LoginFilter {
    protected void loginAfter(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo) {
        request.setAttribute("user", userInfo);
    }
}
