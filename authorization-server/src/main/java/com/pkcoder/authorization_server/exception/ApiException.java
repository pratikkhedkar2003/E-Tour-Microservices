package com.pkcoder.authorization_server.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author PK Coder
 * @version 1.0
 * @project authorization-server
 * @since 09-03-2025
 */

public class ApiException extends AuthenticationException {
    public ApiException(String message) {
        super(message);
    }
}
