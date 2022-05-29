package com.anhnt.configuration.config;

import com.anhnt.common.domain.response.ErrorFactory.ConfigurationError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf()
//            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringAntMatchers("/config/encrypt/**")
            .ignoringAntMatchers("/config/decrypt/**")
            .and().httpBasic().authenticationEntryPoint((HttpServletRequest req, HttpServletResponse res, AuthenticationException ex)->{
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(res.getWriter(), ConfigurationError.UNAUTHORIZED.apply(null).toResponseEntity().getBody());
        });
        super.configure(http); // Important
    }
}
