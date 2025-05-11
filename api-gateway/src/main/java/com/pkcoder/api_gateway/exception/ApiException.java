package com.pkcoder.api_gateway.exception;

/**
 * @author PK Coder
 * @version 1.0
 * @project api-gateway
 * @since 10-03-2025
 */

public class ApiException extends RuntimeException {

    public ApiException() {
        super("An error occurred");
    }

    public ApiException(String message) {
        super(message);
    }

}
