package com.example.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_VALIDITY = 60 * 60 * 1000L; // 1 hours
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000L; // 7 hours
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Set<String> authorities, String username) {
        return buildToken(authorities, username, TOKEN_VALIDITY);
    }

    public String createRefreshToken(Set<String> authorities, String username) {
        return buildToken(authorities, username, REFRESH_TOKEN_VALIDITY);
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private String buildToken(Set<String> authorities, String username, Long validity) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + validity);

        List<String> authorityList = new ArrayList<>(authorities);

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(username)
                .claim("Authority", authorityList)
                .setExpiration(expireDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }
}
