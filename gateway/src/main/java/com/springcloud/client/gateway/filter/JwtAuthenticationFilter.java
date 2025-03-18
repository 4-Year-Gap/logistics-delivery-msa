package com.springcloud.client.gateway.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    // Secret Key를 담을 객체
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = exchange.getRequest().getURI().getPath();
        log.info("[PATH] {}", path);

        if(path.equals("/api/auth/signin") || path.equals("/api/auth/signup")){
            return chain.filter(exchange);
        }

        String rawAuthTokenFromCookie = getTokenFromCookie(request);
        log.info("[RAW_AUTH_TOKEN_FROM_COOKIE] {}", rawAuthTokenFromCookie);

        String extractedToken = substringToken(rawAuthTokenFromCookie);
        log.info("[EXTRACTED_TOKEN] {}", extractedToken);

        if(!validateToken(extractedToken)){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims = getUserInfoFromToken(extractedToken);

        ServerHttpRequest.Builder builder = request.mutate();
        builder.header("X-USER-ID", claims.getSubject());
        builder.header("X-USERNAME", claims.get("username", String.class));
        builder.header("X-SLACK-ID", claims.get("slackId", String.class));
        builder.header("X-USER-ROLE", claims.get("role", String.class));

        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    private String getTokenFromCookie(ServerHttpRequest request) {
        String value = Objects.requireNonNull(request.getCookies().getFirst(AUTHORIZATION_HEADER_KEY)).getValue();
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private String substringToken(String tokenValue) {
        if (!tokenValue.startsWith(BEARER_PREFIX)) throw new NullPointerException("[TOKEN_NOT_FOUND] 토큰을 찾을 수 없습니다.");
        // Bearer 다음에 있는 토큰 값만 추출
        return tokenValue.substring(7);
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("[JWT_SIGNATURE_ERROR] {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("[JWT_EXPIRED] {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("[UNSUPPORTED_JWT] {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("[JWT_ILLEGAL_ARGUMENT] {}", e.getMessage());
        }
        return false;
    }

    private Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
