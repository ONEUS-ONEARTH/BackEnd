package kr.co.ouoe.common.jwt;


import kr.co.ouoe.User.dto.TokenUserInfo;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.common.jwt.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import kr.co.ouoe.User.dto.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
    private final Key key;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Autowired UserDetailsService userDetailsService
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.userDetailsService = userDetailsService;
    }

    public TokenDto generateToken(Authentication authentication,User user) {
        return new TokenDto("Bearer",generateAccessToken(authentication,user), generateRefreshToken(authentication));
    }

    //엑세스토큰을 만드는 함수: 비공개 클레임 (유저 email)
    public String generateAccessToken(Authentication authentication, User user) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());

        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .claim("email",user.getEmail())// 클레임 email: 유저 이메일
                .signWith(SignatureAlgorithm.HS256, key)//서명: 비밀값고 함께 해시값을 HS256으로 암호화
                .compact();
    }


    //액세스 토큰 만드는 함수2
    public String generateAccessToken( User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());

        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .claim("id",user.getId())// 클레임 email: 유저 이메일
                .signWith(SignatureAlgorithm.HS256, key)//서명: 비밀값고 함께 해시값을 HS256으로 암호화
                .compact();
    }


    //리프레시 토큰을 만드는 함수
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

    //리프레시 토큰을 만드는 함수
    public String generateRefreshToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());

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

    //토큰 기반으로 유저 email을 가져옴
    public String getUserEmail(String token) {
        Claims claims = getClaims(token);
        return claims.get("email", String.class);
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    //JWT 토큰 유효성 검사
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

    /**
     * 클라이언트가 전송한 토큰을 디코딩하여 토큰의 위조여부를 확인
     * 토큰을 json으로 파싱해서 클레임(토큰정보)를 리턴
     * @param token
     * @return - 토큰 안에있는 인증된 유저정보를 반환
     */


}
