package kr.co.ouoe.User.controller;

import kr.co.ouoe.User.dto.kakao.KakaoAccessTokenDTO;
import kr.co.ouoe.User.service.KakaoUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class kakaoController {
    @Autowired
    private final KakaoUserService kakaoUserService;

    @PostMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> googleLogin(@RequestBody KakaoAccessTokenDTO kakaoAccessTokenDTO) {
        log.info("들어오나요 카카오{}",kakaoAccessTokenDTO.getAccessToken());
        try{
            return ResponseEntity.ok().body(kakaoUserService.callGoogleAPI(kakaoAccessTokenDTO));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }



    }
}





