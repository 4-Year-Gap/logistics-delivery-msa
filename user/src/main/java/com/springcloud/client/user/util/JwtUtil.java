package com.springcloud.client.user.util;

import com.springcloud.client.user.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "role";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    // 1일 = 24시간
    private static final long HOURS_IN_A_DAY = 24;

    // 1시간 = 60분
    private static final long SECONDS_IN_AN_HOUR = 60 * 60;

    // 1초 = 1000ms
    private static final long MILLISECONDS_IN_A_SECOND = 1000L;

    // 토큰 만료 시간(24시간)
    private static final long TOKEN_EXPIRATION_TIME = HOURS_IN_A_DAY * SECONDS_IN_AN_HOUR * MILLISECONDS_IN_A_SECOND;

    @Value("${jwt.secret.key}")
    private String secretKey;

    // Secret Key를 담을 객체
    private Key key;

    // JWT 서명 알고리즘
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(User user) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(user.getUserId())) // 사용자 식별자 값
                        .claim("username", user.getUsername()) // 사용자 로그인 ID
                        .claim("slackId", user.getSlackId()) // 사용자 Slack ID
                        .claim(AUTHORIZATION_KEY, user.getRole()) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_EXPIRATION_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
            cookie.setPath("/");

            // Response 객체에 쿠키 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    public String substringToken(String tokenValue) {
        // Bearer 다음에 있는 토큰 값만 추출
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("토큰을 찾을 수 없습니다.");
        throw new NullPointerException("토큰을 찾을 수 없습니다.");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("JWT 서명이 유효하지 않습니다.");
        } catch (ExpiredJwtException e) {
            logger.error("만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("잘못된 JWT 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
