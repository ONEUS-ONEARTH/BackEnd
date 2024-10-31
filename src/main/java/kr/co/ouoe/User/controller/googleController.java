package kr.co.ouoe.User.controller;


import kr.co.ouoe.User.dto.GoogleAccessTokenDTO;
import kr.co.ouoe.User.service.GoogleUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 구글 관련 컨트롤러
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class googleController {

    @Autowired
    private final GoogleUserService googleUserService;


    //http://localhost:8080/login/oauth2/code/google?code=4%2F0AVG7fiTGOxZTbc3wVNNR0gq2_IsPHhUlKPvipBonqgvrWQVXx_NPeUoJM2mHr6dev89CuA&scope=profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile
    //Auth code를 통해서 구글에 정보요청
    @PostMapping("/login/oauth2/code/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleAccessTokenDTO googleAccessTokenDTO) {

        log.info("accesstoken dto:{}",googleAccessTokenDTO.getAccessToken());
        try{
            return ResponseEntity.ok().body(googleUserService.callGoogleAPI(googleAccessTokenDTO));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

}
