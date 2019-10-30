package com.jira.user.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jira.user.dto.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

//为让其他服务可以使用用户服务中的身份验证，而写的client客户端，便于其他服务嵌入使用。
public abstract class LoginFilter implements Filter {

    private static Cache<String, UserInfo> cache =
            CacheBuilder.newBuilder().maximumSize(10000)
            .expireAfterWrite(3, TimeUnit.MINUTES).build();

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //从请求中获取token参数
        String token = request.getParameter("token");
        //同时支持token存放在cookie中
        if(StringUtils.isBlank(token)){
            Cookie[] cookies = request.getCookies();
            if(cookies!=null){
                for (Cookie cookie: cookies){
                    if(cookie.getName().equals("token")){
                        token = cookie.getValue();
                    }
                }

            }
        }

        UserInfo userInfo = null;
        if(StringUtils.isNotBlank(token)){
            //每次访问服务都去用户服务验证身份，会让用户服务访问压力变大，所以使用缓存将用户信息缓存在服务中
            userInfo = cache.getIfPresent(token);
            if(userInfo==null){
                userInfo = requestUserInfo(token);
                if(userInfo!=null){
                    cache.put(token, userInfo);
                }
            }
        }


        //未登录，跳转登录页面
        if(userInfo==null){
//            response.sendRedirect("url...");//url: vue客户端的登录页面
        }
        loginAfter(request, response, userInfo);
        filterChain.doFilter(request,response);
    }

    protected abstract void loginAfter(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo);

    private UserInfo requestUserInfo(String token) {
        String url = "http://127.0.0.1:8081/user/authentication";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.addHeader("token", token);
        InputStream inputStream = null;
        try {
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
                throw new RuntimeException("request user info failed! StatusLine:"+response.getStatusLine());
            }
            inputStream  = response.getEntity().getContent();
            byte[] temp = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while((len = inputStream.read(temp))>0){
                sb.append(new String(temp,0,len));
            }

            UserInfo userInfo = new ObjectMapper().readValue(sb.toString(),UserInfo.class);
            return userInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public void destroy() {

    }
}
