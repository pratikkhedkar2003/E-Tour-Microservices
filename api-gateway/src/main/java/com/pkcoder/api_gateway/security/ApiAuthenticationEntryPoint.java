package com.pkcoder.api_gateway.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.pkcoder.api_gateway.utils.RequestUtils.handleErrorResponse;

/**
 * @author PK Coder
 * @version 1.0
 * @project api-gateway
 * @since 10-03-2025
 */

@Component
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        handleErrorResponse(request, response, exception);
    }
}
