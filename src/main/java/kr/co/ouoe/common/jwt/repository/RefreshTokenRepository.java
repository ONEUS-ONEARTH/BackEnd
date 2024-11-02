package kr.co.ouoe.common.jwt.repository;



import kr.co.ouoe.common.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.OptionalInt;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUserId(Long userId);
    RefreshToken findByRefreshToken(String refreshToken);
}
