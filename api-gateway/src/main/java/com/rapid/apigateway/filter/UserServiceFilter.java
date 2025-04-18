package com.rapid.apigateway.filter;

import com.rapid.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class UserServiceFilter extends AbstractGatewayFilterFactory<UserServiceFilter.Config> {

    private final JwtUtil jwtUtil;
    public UserServiceFilter(JwtUtil jwtUtil) {
        super(UserServiceFilter.Config.class);
        this.jwtUtil = jwtUtil;
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Check for Authorization Header
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                unauthorized(exchange);
            }

            String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION).substring(7);
            Claims claims = null;
            try {
                claims = jwtUtil.extractClaims(token);
            } catch (Exception e) {
                unauthorized(exchange);
            }

            String path = request.getURI().getPath();
            List<String> roles = (List<String>) claims.get("roles");

            // Example: Check access for /admin/** only for ADMIN role
            if (path.startsWith("/user") && !roles.contains("ROLE_ADMIN")) {
                forbidden(exchange);
            }

            // Pass user info downstream
            ServerHttpRequest mutated = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", claims.getSubject())
                    .build();

            return chain.filter(exchange.mutate().request(mutated).build());
        };
    }

    private void unauthorized(ServerWebExchange exchange) {
        throw new IllegalArgumentException("no valid credentials");
    }

    private void forbidden(ServerWebExchange exchange) {
        throw new IllegalArgumentException("no valid authorization");
    }

    public static class Config {
        private String role;
    }
}
