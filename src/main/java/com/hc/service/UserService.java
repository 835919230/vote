package com.hc.service;

import com.hc.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hexi on 16-10-3.
 */
public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    User verifyUser(User toVerify, HttpServletRequest request);
}
