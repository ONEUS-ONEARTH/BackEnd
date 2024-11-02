package kr.co.ouoe.common.jwt.service;


import kr.co.ouoe.User.repository.UserRepository;
import kr.co.ouoe.common.jwt.entity.RefreshToken;
import kr.co.ouoe.common.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }
}
