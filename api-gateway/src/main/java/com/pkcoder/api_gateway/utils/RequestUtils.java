package com.pkcoder.api_gateway.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.pkcoder.api_gateway.exception.ApiException;
import com.pkcoder.api_gateway.payload.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author PK Coder
 * @version 1.0
 * @project api-gateway
 * @since 10-03-2025
 */

public class RequestUtils {

    private static final BiConsumer<HttpServletResponse, Response> writeResponse = (httpServletResponse, response) -> {
        try {
            var outPutStream = httpServletResponse.getOutputStream();
            new ObjectMapper().writeValue(outPutStream, response);
            outPutStream.flush();
        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    };

    private static final BiFunction<Exception, HttpStatus, String> errorReason = (exception, status) -> {
        if (status.isSameCodeAs(FORBIDDEN)) { return "You do not have enough permission"; }
        if (status.isSameCodeAs(UNAUTHORIZED)) { return "You are not logged in"; }
        if (exception instanceof DisabledException || exception instanceof LockedException || exception instanceof BadCredentialsException ||
                exception instanceof CredentialsExpiredException || exception instanceof AccountExpiredException || exception instanceof ApiException) {
            return exception.getMessage();
        }
        if (status.is5xxServerError()) {
            return "An internal server error occurred";
        } else {
            return "An error occurred. Please try again";
        }
    };

    public static Response getResponse(HttpServletRequest request, Map<?, ?> data, String message, HttpStatus status) {
        return new Response(now().toString(), status.value(), request.getRequestURI(), valueOf(status.value()), message, EMPTY, data);
    }

    public static Response handleErrorResponse(String message, String exception, HttpServletRequest request, HttpStatusCode status) {
        return new Response(now().toString(), status.value(), request.getRequestURI(), valueOf(status.value()), message, exception, emptyMap());
    }

    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        if (exception instanceof AccessDeniedException) {
            var apiResponse = getErrorResponse(request, response, exception, FORBIDDEN);
            writeResponse.accept(response, apiResponse);
        } else if (exception instanceof InsufficientAuthenticationException) {
            var apiResponse = getErrorResponse(request, response, exception, UNAUTHORIZED);
            writeResponse.accept(response, apiResponse);
        } else if (exception instanceof OAuth2AuthenticationException) {
            var apiResponse = getErrorResponse(request, response, exception, UNAUTHORIZED);
            writeResponse.accept(response, apiResponse);
        } else if (exception instanceof MismatchedInputException) {
            var apiResponse = getErrorResponse(request, response, exception, BAD_REQUEST);
            writeResponse.accept(response, apiResponse);
        } else if (exception instanceof DisabledException || exception instanceof LockedException || exception instanceof BadCredentialsException ||
                exception instanceof CredentialsExpiredException || exception instanceof AccountExpiredException || exception instanceof ApiException) {
            var apiResponse = getErrorResponse(request, response, exception, BAD_REQUEST);
            writeResponse.accept(response, apiResponse);
        } else {
            var apiResponse = getErrorResponse(request, response, exception, INTERNAL_SERVER_ERROR);
            writeResponse.accept(response, apiResponse);
        }
    }

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception, HttpStatus status) {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        return new Response(now().toString(), status.value(), request.getRequestURI(), valueOf(status.value()), errorReason.apply(exception, status), getRootCauseMessage(exception), emptyMap());
    }

}
