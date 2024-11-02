package kr.co.ouoe.common.jwt.controller;

import kr.co.ouoe.common.jwt.dto.CreateAccessTokenRequest;
import kr.co.ouoe.common.jwt.dto.CreateAccessTokenResponse;
import kr.co.ouoe.common.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;
    @PostMapping("/api/toekn")
    public ResponseEntity<CreateAccessTokenResponse> createToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken=tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.ok().body(new CreateAccessTokenResponse(newAccessToken));
    }
}
