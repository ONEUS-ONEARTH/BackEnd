package kr.co.ouoe.User.service;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.ouoe.Util.TokenProvider;
import kr.co.ouoe.common.jwt.JwtTokenProvider;
import kr.co.ouoe.common.jwt.dto.TokenDto;
import kr.co.ouoe.User.dto.GoogleAccessTokenDTO;
import kr.co.ouoe.User.dto.GoogleInfoDTO;
import kr.co.ouoe.User.dto.GoogleLoginUserResponseDTO;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.exception.DuplicateEmailException;
import kr.co.ouoe.User.exception.GoogleLoginErrorException;
import kr.co.ouoe.User.repository.UserRepository;
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
public class GoogleUserService {

    private final UserRepository userRepository;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;


    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;

    String responseType = "code";

    String grantType = "authorization_code";

    @Value("${spring.security.oauth2.client.registration.google.scope[0]}")
    String scope;

    static final String googleAuthUri="https://oauth2.googleapis.com/token";
    static final String gooleAPI="https://www.googleapis.com/oauth2/v2/userinfo";

    private final TokenProvider tokenProvider;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    //accessToken을 가지고 정보를 가져오는 메서드
    public GoogleLoginUserResponseDTO callGoogleAPI(GoogleAccessTokenDTO googleAccessTokenDTO){
        //
        String uri=gooleAPI+"?access_token="+googleAccessTokenDTO.getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        //String uri="";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+googleAccessTokenDTO.getAccessToken());
        HttpEntity entity = new HttpEntity(headers);
        JsonNode res= restTemplate.exchange(gooleAPI, HttpMethod.GET, entity, JsonNode.class).getBody();
        log.info("Google API response: "+res);
        String email=res.get("email").asText();
        String name=res.get("name").asText();
        String picture=res.get("picture").asText();
        String id=res.get("id").asText();
        //이메일 존재를 확인하기
        try{
            if(checkEmail(email)){
                //이미 일반회원으로 존재한다는 메시지
                throw new DuplicateEmailException("이미 일반회원으로 가입하셨습니다. 다시 로그인 해주세요!");

            }else{
                //1.회원가입
                GoogleInfoDTO googleInfoDTO=GoogleInfoDTO.builder().id(id).imgPath(picture).name(name).email(email).build();
                GoogleLoginUserResponseDTO googleLoginUserResponseDTO=new GoogleLoginUserResponseDTO();
                User googleUser= googleLoginUserResponseDTO.toEntity(bCryptPasswordEncoder,googleInfoDTO);
                userRepository.save(googleUser);
                //2 tokenDTO 저장
                TokenDto tokenDto=new TokenDto(googleAccessTokenDTO.getTokenType(), tokenProvider.createToken(googleUser), tokenProvider.generateRefreshToken(googleUser));
                return new GoogleLoginUserResponseDTO(googleUser,tokenDto);

            }


        }catch (NullPointerException e){
            throw new GoogleLoginErrorException("구글 로그인에 문제가 생겼습니다 다시 시도해두세요!");

        }

    }

    public Boolean checkEmail(String email) {
        log.info("중복여부 {}",userRepository.existsByEmail(email));
        return userRepository.existsByEmail(email);
    }



}
