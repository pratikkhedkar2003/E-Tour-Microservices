package com.pkcoder.authorization_server.security;

import com.pkcoder.authorization_server.exception.ApiException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {
    private static final String AUTHENTICATION_EXCEPTION = "authenticationException";
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private String defaultFailureUrl;

    public LoginFailureHandler(String defaultFailureUrl) {
        setDefaultFailureUrl(defaultFailureUrl);
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("Exception in LoginFailureHandler: {}", exception.getMessage());
        String errorMessage;
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            errorMessage = "Invalid email or password.";
        } else if (exception instanceof DisabledException) {
            errorMessage = "Account is currently disabled.";
        } else if (exception instanceof LockedException) {
            errorMessage = "Account is currently locked.";
        } else if (exception instanceof AccountExpiredException) {
            errorMessage = "Account is currently expired.";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = "Credentials expired. Please reset your credentials.";
        } else if (exception instanceof ApiException) {
            errorMessage = exception.getMessage();
        } else {
            errorMessage = "An error has occurred. Please try again.";
        }
        saveException(request, errorMessage);
        this.redirectStrategy.sendRedirect(request, response, this.defaultFailureUrl);
    }

    protected final void saveException(HttpServletRequest request, String errorMessage) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            request.getSession().setAttribute(AUTHENTICATION_EXCEPTION, errorMessage);
        } else {
            request.setAttribute(AUTHENTICATION_EXCEPTION, errorMessage);
        }
    }

    public void setDefaultFailureUrl(String defaultFailureUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl),
                () -> "'" + defaultFailureUrl + "' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }
}
