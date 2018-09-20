package com.troy.keeper.management.security;

import com.troy.keeper.core.config.HttpSecurityConfigure;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

/**
* Created by yg on 2017/4/28.
* 1. please access /login to understand how to get the crsf token.
* 2. and also the crsf token will get on the cookie. go to see.
*/
@Component
public class SecurityConfig implements HttpSecurityConfigure {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/api/manager/**").permitAll();
    }
}
