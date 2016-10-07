package com.hc;

import com.hc.dao.UserDao;
import com.hc.model.User;
import com.hc.service.impl.UserServiceImpl;
import com.hc.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by hexi on 16-10-3.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    @Autowired
    private UserServiceImpl userService;


    /**
     * Url权限认证
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("protected void configure(HttpSecurity http) throws Exception ");
        http
                .authorizeRequests()
                .antMatchers("/vote/**","/publish","/admin/**","choice/**")
                .hasAnyRole("CREATOR","ADMIN","USER")
                .anyRequest()
                .permitAll()
        .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/login")
                .loginProcessingUrl("/login/post")
                .successHandler(userService)
                .failureUrl("/login?error")
                .defaultSuccessUrl("/admin")
                .permitAll()
        .and()
                .logout()
                .permitAll()
                .logoutUrl("/logout")
                .deleteCookies("key_rememberMe")
                .logoutSuccessUrl("/")
        .and()
                .rememberMe()
                .tokenValiditySeconds(((int) TimeUtils.getDays(7L)))
                .key("key_rememberMe")
//                .rememberMeCookieName("key_rememberMe")
//                .rememberMeParameter(((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
//                .authenticationSuccessHandler()
                .alwaysRemember(false)
                .rememberMeServices(userService)
                .userDetailsService(userService)
        ;
    }

    /**
     * 用户权限认证
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        logger.info("----------------------------configure---------------------");
        auth
                .userDetailsService(userService)
        .and()
                .eraseCredentials(true)
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

}
