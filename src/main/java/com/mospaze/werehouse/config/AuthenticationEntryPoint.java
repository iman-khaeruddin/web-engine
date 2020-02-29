package com.mospaze.werehouse.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
//@Component
public class AuthenticationEntryPoint /*extends BasicAuthenticationEntryPoint*/ {

    /*@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 - " + authEx.getEnMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("Mospaze-DeveloperStack");
        super.afterPropertiesSet();
    }*/

}