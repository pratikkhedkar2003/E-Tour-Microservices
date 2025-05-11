package com.pkcoder.authorization_server.controller;

import com.pkcoder.authorization_server.enumeration.LoginType;
import com.pkcoder.authorization_server.model.User;
import com.pkcoder.authorization_server.security.MfaAuthenticationToken;
import com.pkcoder.authorization_server.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final AuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler("/mfa?error");
    private final AuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/mfa")
    public String mfa(Model model, @CurrentSecurityContext SecurityContext securityContext) {
        User user = getAuthenticatedUser(securityContext.getAuthentication());
        model.addAttribute("email", user.getEmail());
        userService.resetLoginAttempts(user.getEmail());
        return "mfa";
    }

    @PostMapping("/mfa")
    public void validateMfaCode(
            @RequestParam("code") String code,
            HttpServletRequest request,
            HttpServletResponse response,
            @CurrentSecurityContext SecurityContext securityContext
    ) throws ServletException, IOException {
        User user = getAuthenticatedUser(securityContext.getAuthentication());
        if (user.getEmail().equalsIgnoreCase("pk@gmail.com") && code.equals("134679")) {
            userService.updateLoginAttempts(user.getEmail(), LoginType.LOGIN_SUCCESS);
            this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, getSavedAuthentication(request, response));
            return;
        }
        this.authenticationFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Invalid QR code. Please try again."));
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    private Authentication getSavedAuthentication(HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        MfaAuthenticationToken mfaAuthenticationToken = (MfaAuthenticationToken) securityContext.getAuthentication();
        securityContext.setAuthentication(mfaAuthenticationToken.getPrimaryAuthentication());
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);
        return mfaAuthenticationToken.getPrimaryAuthentication();
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
        {
            return (User) usernamePasswordAuthenticationToken.getPrincipal();
        }
        return (User) authentication.getPrincipal();
    }

}
