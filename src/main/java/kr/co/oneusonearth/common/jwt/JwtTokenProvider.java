package kr.co.oneusonearth.common.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import kr.co.oneusonearth.common.jwt.dto.TokenDto;
import kr.co.oneusonearth.user.dto.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
    private final Key key;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Autowired UserDetailsService userDetailsService
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.userDetailsService = userDetailsService;
    }

    public TokenDto generateToken(Authentication authentication) {
        return new TokenDto("Bearer",generateAccessToken(authentication), generateRefreshToken(authentication));
    }

    //엑세스토큰을 만드는 함수
    public String generateAccessToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());

        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());

        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    //토큰 정보 검증
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userPrincipal, "", userPrincipal.getAuthorities());
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public long getExpiration(String accessToken) {
        Date expiration = Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody().getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }
}
