package com.anhnt.configuration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .ignoringAntMatchers("/configuration/config/encrypt/**")
            .ignoringAntMatchers("/config/decrypt/**")
                .ignoringAntMatchers("/encrypt/**")
                .ignoringAntMatchers("/config/decrypt/**")
                .ignoringAntMatchers("/config/encrypt/**")
                .ignoringAntMatchers("/config/decrypt/**");
//            .and().httpBasic().authenticationEntryPoint((HttpServletRequest req, HttpServletResponse res, AuthenticationException ex)->{
//            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            res.getWriter().println("HTTP Status 401 - " + ex.getMessage());
//        });
        super.configure(http); // Important
    }
}
