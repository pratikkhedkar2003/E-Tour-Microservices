package com.pkcoder.authorization_server.security;

import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

public class ClientRefreshTokenAuthenticationToken extends OAuth2ClientAuthenticationToken {

    public ClientRefreshTokenAuthenticationToken(String clientId) {
        super(clientId, ClientAuthenticationMethod.NONE, null, null);
    }

    public ClientRefreshTokenAuthenticationToken(RegisteredClient registeredClient) {
        super(registeredClient, ClientAuthenticationMethod.NONE, null);
    }
}
