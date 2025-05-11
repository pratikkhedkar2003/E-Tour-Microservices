package com.pkcoder.authorization_server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

@Component
@RequiredArgsConstructor
public class ClientRefreshTokenAuthenticationProvider implements AuthenticationProvider {
    private final RegisteredClientRepository registeredClientRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ClientRefreshTokenAuthenticationToken clientRefreshTokenAuthenticationToken = (ClientRefreshTokenAuthenticationToken) authentication;
        if (!ClientAuthenticationMethod.NONE.equals(clientRefreshTokenAuthenticationToken.getClientAuthenticationMethod())) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT, "Authentication method is not valid", null));
        }
        String clientId = clientRefreshTokenAuthenticationToken.getPrincipal().toString();
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT, "Client is not valid", null));
        }
        if (!registeredClient.getClientAuthenticationMethods().contains(clientRefreshTokenAuthenticationToken.getClientAuthenticationMethod())) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT, "Authentication method is not valid", null));
        }
        return new ClientRefreshTokenAuthenticationToken(registeredClient);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClientRefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
