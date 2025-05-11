package com.pkcoder.authorization_server.security;

import com.pkcoder.authorization_server.enumeration.LoginType;
import com.pkcoder.authorization_server.model.User;
import com.pkcoder.authorization_server.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

public class MfaAuthenticationHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final AuthenticationSuccessHandler mfaNotEnabled = new SavedRequestAwareAuthenticationSuccessHandler();
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final String authority;

    public MfaAuthenticationHandler(String successUrl, String authority, UserService userService) {
        SimpleUrlAuthenticationSuccessHandler simpleUrlAuthenticationSuccessHandler = new SimpleUrlAuthenticationSuccessHandler(successUrl);
        simpleUrlAuthenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
        this.authenticationSuccessHandler = simpleUrlAuthenticationSuccessHandler;
        this.authority = authority;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            User user = (User) authentication.getPrincipal();
            if (!user.isMfa()) {
                userService.updateLoginAttempts(user.getEmail(), LoginType.LOGIN_SUCCESS);
                mfaNotEnabled.onAuthenticationSuccess(request, response, authentication);
                return;
            }
        }
        saveAuthentication(request, response, new MfaAuthenticationToken(authentication, authority));
        authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }

    private void saveAuthentication(HttpServletRequest request, HttpServletResponse response, MfaAuthenticationToken mfaAuthenticationToken) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(mfaAuthenticationToken);
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);
    }
}
