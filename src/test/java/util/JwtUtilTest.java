package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.user.common.UserFixture;
import com.example.user.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(classes = {JwtUtil.class})
public class JwtUtilTest implements UserFixture {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
        jwtUtil.init();
    }

    @Test
    public void testCreateToken() {
        String token = jwtUtil.createToken(TEST_USER1_ID, TEST_USER_USERNAME);
        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));
    }

    @Test
    public void testGetJwtFromHeader() {
        String token = "Bearer testtoken";

        when(request.getHeader(JwtUtil.AUTHORIZATION_HEADER)).thenReturn(token);

        String jwt = jwtUtil.getJwtFromHeader(request);
        assertEquals("testtoken", jwt);
    }

    @Test
    public void testGetJwtFromHeader_Invalid() {
        String token = "Invalid token";

        when(request.getHeader(JwtUtil.AUTHORIZATION_HEADER)).thenReturn(token);

        String jwt = jwtUtil.getJwtFromHeader(request);
        assertNull(jwt);
    }

    @Test
    public void testGetUserInfoFromToken() {
        Long userId = 1L;
        String username = "testuser";
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + 60 * 60 * 1000L);
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));

        String token = Jwts.builder()
            .setSubject(username)
            .claim("userId", userId)
            .setExpiration(expireDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        Claims claims = jwtUtil.getUserInfoFromToken(token);
        assertEquals(username, claims.getSubject());
        assertEquals(userId, claims.get("userId", Long.class));

    }

}
