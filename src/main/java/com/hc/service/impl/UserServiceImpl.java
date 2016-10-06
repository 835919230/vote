package com.hc.service.impl;

import com.hc.dao.UserDao;
import com.hc.model.User;
import com.hc.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by hexi on 16-10-3.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    @Autowired
//    @Qualifier("_authenticationManager")
    private AuthenticationManager manager;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        logger.info(user.toString());
        if (user.getId() <= 0L)
            throw new UsernameNotFoundException("");
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
    public User verifyUser(User toVerify, HttpServletRequest request) {
        User user;
        if ((user = findByUsername(toVerify.getUsername())).getId() > 0L) {
            Authentication authenticate = manager.authenticate(toVerify);
            if (authenticate != null && authenticate.isAuthenticated()) {
                user.setLastLoginTime(new Date());
                String ip = request.getRemoteAddr();
                user.setLastLoginIp(ip);
                logger.info("ip:{}",ip);
                userDao.save(user);
                return user;
            }
        }
        return null;
    }
}
