package com.hc.web;

import com.hc.model.User;
import com.hc.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Component;

/**
 * Created by hexi on 16-10-5.
 */
@Component("_authenticationManager")
public class UserAuthenticationManager implements AuthenticationManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        if (StringUtils.isNotEmpty(username)) {
            User user = userService.findByUsername(username);
            if (user != null)
                return authentication;
        }
        return null;
    }
}
