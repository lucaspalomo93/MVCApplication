package com.challenge.challenge.securityConfig;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.challenge.challenge.controllers.services.OperatorService;
import com.challenge.challenge.models.Operator;

public class CustomSuccessHandler implements AuthenticationSuccessHandler{

            
    @Autowired
    private OperatorService oService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

            String username = authentication.getName();
            Operator op = oService.findByusername(username);
            oService.updateLastLoginDate(op);

            response.sendRedirect("/operator/list");        
    }
    
}
