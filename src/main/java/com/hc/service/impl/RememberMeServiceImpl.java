package com.hc.service.impl;

import com.hc.dao.UserDao;
import com.hc.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.ArrayUtils.*;

/**
 * Created by hexi on 16-10-5.
 */
@Service("rememberMeServices")
public class RememberMeServiceImpl implements RememberMeServices {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    private static final String AUTO_LOGIN_COOKIE_NAME = "key_rememberMe";

    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        logger.info("---------------------------------------------autoLogin---------------------------------------");
        Cookie[] cookies = request.getCookies();
        if (isNotEmpty(cookies)) {
            for (Cookie c : cookies) {
                if (c.getName().equals(AUTO_LOGIN_COOKIE_NAME)) {
                    String username = c.getValue();
                    User user = userDao.findByUsername(username);
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {
        logger.info("-------------loginFail-------------------------");
        request.setAttribute("fail_message","用戶名或密码错误");
    }

    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        logger.info("---------------loginSuccess-----------------------");
        String rememberMe = request.getParameter("rememberMe");
        if (StringUtils.equals(rememberMe,"true") && isExistsCookie(request, AUTO_LOGIN_COOKIE_NAME)) {
            Cookie autoLoginCookie = new Cookie(AUTO_LOGIN_COOKIE_NAME, request.getParameter("username"));
            autoLoginCookie.setMaxAge(86400 * 7);//A week
            response.addCookie(autoLoginCookie);
        }
    }

    public boolean isExistsCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (isNotEmpty(cookies)) {
            for (Cookie c : cookies) {
                if (c.getName().equals(cookieName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
