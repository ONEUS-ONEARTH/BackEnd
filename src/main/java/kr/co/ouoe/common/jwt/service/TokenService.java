package kr.co.ouoe.common.jwt.service;

import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.service.UserService;
import kr.co.ouoe.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        //토큰 유효성 검사에 실패하면 예외 발생
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("unexpected refresh token");
        }
        Long userId=refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user=userService.findById(userId);
        return jwtTokenProvider.generateAccessToken(user);

    }

}
