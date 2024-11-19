package kr.co.ouoe.User.service;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.ouoe.User.dto.GoogleInfoDTO;
import kr.co.ouoe.User.dto.GoogleLoginUserResponseDTO;
import kr.co.ouoe.User.dto.kakao.KakaoAccessTokenDTO;
import kr.co.ouoe.User.dto.kakao.KakaoInfoDTO;
import kr.co.ouoe.User.dto.kakao.KakaoLoginUserResponseDTO;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.exception.DuplicateEmailException;
import kr.co.ouoe.User.exception.GoogleLoginErrorException;
import kr.co.ouoe.User.repository.UserRepository;
import kr.co.ouoe.Util.TokenProvider;
import kr.co.ouoe.common.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
@Slf4j
public class KakaoUserService {

    private final UserRepository userRepository;


    String grantType = "authorization_code";


    static final String googleAuthUri="https://oauth2.googleapis.com/token";
    static final String kakapAPI="https://kapi.kakao.com/v2/user/me";

    private final TokenProvider tokenProvider;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    //accessToken을 가지고 정보를 가져오는 메서드
    public KakaoLoginUserResponseDTO callGoogleAPI(KakaoAccessTokenDTO kakaoAccessTokenDTO) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + kakaoAccessTokenDTO.getAccessToken());
        headers.set("Content-Type", "application/json");
        HttpEntity entity = new HttpEntity(headers);
        JsonNode res = restTemplate.exchange(kakapAPI, HttpMethod.GET, entity, JsonNode.class).getBody();
        log.info("Google API response: " + res);
        String nickname = res.get("kakao_account").get("profile").get("nickname").asText();
        String thumbNailUrl = res.get("kakao_account").get("profile").get("thumbnail_image_url").asText();
        String email = res.get("kakao_account").get("email").asText();
        String name = res.get("kakao_account").get("name").asText();
        boolean hasPhoneNumber = res.get("kakao_account").get("has_phone_number").asBoolean();
        String phoneNumber = res.get("kakao_account").get("phone_number").asText();
        String id = res.get("id").asText();


        //이메일 존재를 확인하기
        try{
            if(checkEmail(email)){
                if(checkLoginMethod(email).equals("카카오")){
                    //이미 구글로 로그인 한 회원이면, 로그인 시킴.
                    User kakaoUser=userRepository.findByEmail(email);
                    TokenDto tokenDto=new TokenDto(kakaoAccessTokenDTO.getTokenType(), tokenProvider.createToken(kakaoUser), tokenProvider.generateRefreshToken(kakaoUser));
                    return new KakaoLoginUserResponseDTO(kakaoUser,tokenDto);

                }

                //이미 일반회원으로 존재한다는 메시지
                else{throw new DuplicateEmailException("이미 일반회원으로 가입하셨습니다. 다시 로그인 해주세요!");}

            }else{
                //1.회원가입
                KakaoInfoDTO kakaoInfoDTO=KakaoInfoDTO.builder().id(id).thumbNailUrl(thumbNailUrl).name(name).email(email).phoneNumber(hasPhoneNumber?phoneNumber:null).build();
                KakaoLoginUserResponseDTO kakaoLoginUserResponseDTO=new KakaoLoginUserResponseDTO();
                User kakaoUser= kakaoLoginUserResponseDTO.toEntity(bCryptPasswordEncoder,kakaoInfoDTO);
                userRepository.save(kakaoUser);
                //2 tokenDTO 저장
                TokenDto tokenDto=new TokenDto(kakaoAccessTokenDTO.getTokenType(), tokenProvider.createToken(kakaoUser), tokenProvider.generateRefreshToken(kakaoUser));
                return new KakaoLoginUserResponseDTO(kakaoUser,tokenDto);

            }


        }catch (NullPointerException e){
            throw new GoogleLoginErrorException("구글 로그인에 문제가 생겼습니다 다시 시도해두세요!");

        }

    }

    public Boolean checkEmail(String email) {
        log.info("중복여부 {}",userRepository.existsByEmail(email));
        return userRepository.existsByEmail(email);
    }

    public String checkLoginMethod(String email) {
        return userRepository.findByEmail(email).getLoginMethod().toString();
    }





}


