package com.hc.web;

import com.hc.service.impl.UserServiceImpl;
import com.hc.util.TimeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by hexi on 16-10-3.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    /**
     * Url权限认证
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/view/*","/publish","/admin/*")
                .hasAnyRole("admin","user")
                .anyRequest().permitAll()
        .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
        .and()
                .logout()
                .permitAll()
                .logoutUrl("/logout")
        .and()
                .rememberMe()
                .tokenValiditySeconds(((int) TimeUtils.getDays(7L)))
                .key("key_rememberMe")
        ;
    }

    /**
     * 用户权限认证
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.userDetailsService(userDetailsService());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
