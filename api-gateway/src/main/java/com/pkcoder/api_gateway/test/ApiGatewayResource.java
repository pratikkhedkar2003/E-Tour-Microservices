package com.pkcoder.api_gateway.test;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PK Coder
 * @version 1.0
 * @project api-gateway
 * @since 10-03-2025
 */

@RestController
@RequestMapping(path = "/api/v1")
public class ApiGatewayResource {

    @GetMapping(path = "/gateway")
    @PreAuthorize(value = "hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<String> getApiGatewayResource() {
        return ResponseEntity.ok().body("Hello from Api Gateway");
    }

}
