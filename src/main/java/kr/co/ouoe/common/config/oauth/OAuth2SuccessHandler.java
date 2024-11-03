package kr.co.ouoe.common.config.oauth;//package kr.co.oneusonearth.common.config.oauth;
//

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.service.UserService;
import kr.co.ouoe.Util.CookieUtil;
import kr.co.ouoe.common.jwt.JwtTokenProvider;
import kr.co.ouoe.common.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String  REFRESH_TOKEN_COOKIE_NAME="refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_COOKIE_DURATION = Duration.ofDays(1);
    public static final String  REDIRECT_PATH="/articles";

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository auth2AuthorizationRequestBasedOnCookieRepository;

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User=(OAuth2User) authentication.getPrincipal();
        User user= userService.findByEmaild((String) oAuth2User.getAttributes().get("email"));

        //리프레시 토큰 생성-> 저장-> 쿠키에 저장
        String refreshToken =jwtTokenProvider.generateRefreshToken(authentication);
        saveRefreshToken(user.getEmail(),refreshToken);
        addRefreshTokenCookie(request,response,refreshToken);
        //액세스토큰 생성 패스에 액세스 토큰 추가
        String accessToken=jwtTokenProvider.generateAccessToken(user);
        String targetUrl=getTargetUrl(accessToken);
        //인증 관련 설정갑, 쿠키 제거
        clearAuthenticationAttributes(request,response);
        //리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);


    }

    //생성된 리프레시 토큰을 전달받아 데이터 베이스에 저장
    private void saveRefreshToken(String email,String refreshToken){
        User user=userService.findByEmaild(email);
        user.setRefreshToken(refreshToken);
        //유제 레파지토리에 리프레시 토큰 저장

    }

    //생성된 리프레시 토큰을 쿠키에 저장
    private void addRefreshTokenCookie(HttpServletRequest request,HttpServletResponse response,String refreshToken){
        int cookieMaxAge=(int)REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    //인증 관련설정값, 쿠키제거
    private void clearAuthenticationAttributes(HttpServletRequest request,HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        auth2AuthorizationRequestBasedOnCookieRepository.removeAuthorizationRequest(request,response);
    }

    //엑세스 토큰을 패스에 추가
    private String getTargetUrl(String token){
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token",token)
                .build()
                .toUriString();
    }



}
