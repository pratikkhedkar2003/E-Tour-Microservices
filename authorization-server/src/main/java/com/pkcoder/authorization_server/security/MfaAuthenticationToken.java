package com.pkcoder.authorization_server.security;

import lombok.Getter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

@Getter
public class MfaAuthenticationToken extends AnonymousAuthenticationToken {
    private final Authentication primaryAuthentication;

    public MfaAuthenticationToken(Authentication authentication, String authority) {
        super("anonymous", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS", authority));
        this.primaryAuthentication = authentication;
    }

    @Override
    public Object getPrincipal() {
        return this.primaryAuthentication;
    }
}
