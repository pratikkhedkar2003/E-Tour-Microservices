package com.pkcoder.api_gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

/**
 * @author PK Coder
 * @version 1.0
 * @project api-gateway
 * @since 10-03-2025
 */

@Configuration
public class ApiGatewayRoutesConfig {
    @Value("${services.uri.authorization-server-uri}")
    private String authorizationServerUri;

    @Bean
    public RouterFunction<ServerResponse> authorizationServerRoute() {
        return GatewayRouterFunctions.route("authorization-server")
                .route(RequestPredicates.path("/oauth2/**"), HandlerFunctions.http(authorizationServerUri))
                .route(RequestPredicates.path("/logout/**"), request ->
                        ServerResponse.permanentRedirect(URI.create(authorizationServerUri + "/logout")).build()
                )
                .build();
    }

}


