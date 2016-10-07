package com.hc.service.impl;

import com.hc.dao.UserDao;
import com.hc.model.User;
import com.hc.service.LoginFailException;
import com.hc.service.UserService;
import com.hc.web.controller.VoteController;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * Created by hexi on 16-10-3.
 */
@Service("userService")
public class UserServiceImpl implements UserService,
        AuthenticationSuccessHandler,
        RememberMeServices, AuthenticationManager, PasswordEncoder
{
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        logger.info(user.toString());
        if (user.getId() <= 0L)
            throw new UsernameNotFoundException("该用户不存在");
        return user;
    }

    @Override
    public User findByUsername(String username) {
        if (StringUtils.isNotEmpty(username)) {
            User user = userDao.findByUsername(username);
            if (user != null) {
                return user;
            }
        }
        User user = new User();
        user.setId(-1L);
        return user;
    }

    @Transactional
    @Override
    public User verifyUser(User toVerify, HttpServletRequest request, HttpServletResponse response) throws LoginFailException {
        User user;
        if ((user = findByUsername(toVerify.getUsername())).getId() > 0L) {
            Authentication auth = authenticate(toVerify);
            if (auth != null && auth.isAuthenticated()) {
                try {
                    request.setAttribute("USER", user);
                    onAuthenticationSuccess(request, response, auth);
                    return user;
                } catch (IOException e) {
                    logger.error("IOException In verifyUser:{}",e);
                } catch (ServletException e) {
                    logger.error("ServletException In verifyUser:{}",e);
                }
            }
        }
        throw new LoginFailException("没有验证成功，请检查用户名或密码");
    }

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) request.getAttribute("USER");
        user.setLastLoginTime(new Date());
        String ip = request.getRemoteAddr();
        user.setLastLoginIp(ip);
        logger.info("ip:{}",ip);
        userDao.save(user);
    }
    private static final String AUTO_LOGIN_COOKIE_NAME = "key_rememberMe";

    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        logger.info("---------------------------------------------autoLogin---------------------------------------");
        Cookie[] cookies = request.getCookies();
        if (isNotEmpty(cookies)) {
            for (Cookie c : cookies) {
                logger.info("cookie:{}",c.getValue());
                if (c.getName().equals(AUTO_LOGIN_COOKIE_NAME)) {
                    logger.info("要自动登录了！");
                    String username = c.getValue();
                    User user = userDao.findByUsername(username);
                    saveSecurityContext(request, user);
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
        if (StringUtils.equals(rememberMe,"true") && !isExistsCookie(request, AUTO_LOGIN_COOKIE_NAME)) {
            logger.info("开始设置自动登录Cookie");
            Cookie autoLoginCookie = new Cookie(AUTO_LOGIN_COOKIE_NAME, request.getParameter("username"));
            autoLoginCookie.setMaxAge(86400 * 7);//A week
            response.addCookie(autoLoginCookie);
        }
        saveSecurityContext(request,successfulAuthentication);
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

    private void saveSecurityContext(HttpServletRequest request, Authentication successfulAuthentication) {
        SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
        HttpSession session = request.getSession();
        session.setAttribute(VoteController.SPRING_SECURITY_CONTEXT,SecurityContextHolder.getContext());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        if (StringUtils.isNotEmpty(username)) {
            User user = findByUsername(username);
            if (user != null && matches((String)authentication.getCredentials(),user.getPassword()))
                return authentication;
        }
        return null;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        logger.info("执行encode方法");
        return new Md5Hash(rawPassword,Md5Hash.ALGORITHM_NAME).toHex();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        logger.info("执行matches方法");
        return rawPassword != null && StringUtils.equals(new Md5Hash(rawPassword, Md5Hash.ALGORITHM_NAME).toHex(), encodedPassword);
    }
}
