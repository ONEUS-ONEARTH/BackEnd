package kr.co.oneusonearth.common.jwt.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
public class JwtTokenProvider {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
    private final Key key;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(Key key, UserDetailsService userDetailsService) {
        this.key = key;
        this.userDetailsService = userDetailsService;
    }
}
