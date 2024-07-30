package com.example.user.filter;

import com.example.user.dto.UserDetailsImpl;
import com.example.user.entity.User;
import com.example.user.security.CustomAuthentication;
import com.example.user.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        HttpServletRequest req,
        HttpServletResponse res,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {

            try {
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                Long userId = info.get("userId", Long.class);
                String username = info.getSubject();
                setAuthentication(userId, username);
            } catch (SecurityException | MalformedJwtException e) {
                log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
                return;
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token, 만료된 JWT token 입니다.");
                return;
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
                return;
            } catch (IllegalArgumentException e) {
                log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
                return;
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }

        }

        filterChain.doFilter(req, res);
    }

    public void setAuthentication(Long userId, String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId, username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(Long userId, String username) {
        User user = new User(userId, username);
        UserDetails userDetails = new UserDetailsImpl(user);
        return new CustomAuthentication(userDetails);
    }
}
