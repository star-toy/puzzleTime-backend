package startoy.puzzletime.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKeyGenerator secretKeyGenerator;
    private Key key;
    private final long tokenValidityInMilliseconds;

    public JwtTokenProvider(
            SecretKeyGenerator secretKeyGenerator,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secretKeyGenerator = secretKeyGenerator;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        refreshSecretKey(); // 초기 시크릿 키 생성
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    // 시크릿 키 갱신 메소드
    private void refreshSecretKey() {
        String secret = secretKeyGenerator.generateSecretKey();
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        logger.info("JWT Secret key has been refreshed at: {}", LocalDateTime.now());
    }

    // 토큰 생성 시 새로운 시크릿 키 사용
    public String createToken(String email, String name) {
        refreshSecretKey(); // 새로운 토큰 생성 시 시크릿 키 갱신

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .claim("name", name)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰에서 만료 시간 추출
    public LocalDateTime getExpiredAtFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 만료 시간 추출 (Date 타입을 LocalDateTime으로 변환)
            Date expiration = claims.getExpiration();
            return expiration.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Failed to extract expiration time from token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

}