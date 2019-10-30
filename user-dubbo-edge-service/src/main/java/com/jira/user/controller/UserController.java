package com.jira.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jira.user.dto.UserInfo;
import com.jira.user.redis.RedisClient;
import com.jira.user.response.LoginResponse;
import com.jira.user.response.Response;
import com.jira.user.service.UserService;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;

    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestParam("userName")String userName,
                          @RequestParam("password")String password){
        //1.验证用户名密码
        UserInfo userInfo = userService.getUserByName(userName);
        if(userInfo == null){
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if(!userInfo.getPassword().equalsIgnoreCase(md5(password))){
            return Response.USERNAME_PASSWORD_INVALID;
        }

        //2. 生成token
        String token = generateToken();

        //3. 缓存用户
        redisClient.set(token, userInfo, 3600);

        return new LoginResponse(token);
    }

    //单点登录，访问服务前验证用户是否已登录
    @RequestMapping(value="/authentication", method = RequestMethod.POST)
    @ResponseBody
    public UserInfo authentication(@RequestParam("token") String token){
        return redisClient.get(token);
    }

    private String generateToken() {
        return randomCode("0123456789abcdefghijklmnopqrstuvwxyz",32);
    }

    private String randomCode(String s, int size){
        StringBuilder result = new StringBuilder(size);
        Random random = new Random();
        for(int i=0; i<size; i++){
            int loc = random.nextInt(s.length());
            result.append(s.charAt(loc));
        }
        return result.toString();
    }

    private String md5(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(password.getBytes("utf-8"));
            return HexUtils.toHexString(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
