package com.pkcoder.api_gateway.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * @author PK Coder
 * @version 1.0
 * @project api-gateway
 * @since 10-03-2025
 */

@JsonInclude(value = NON_DEFAULT)
public record Response(String time, int code, String path, HttpStatus status, String message, String exception, Map<?, ?> data) {}
