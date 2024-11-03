package kr.co.ouoe.common.config.oauth;

import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
   // private final EnableSpringDataWebSupport.QuerydslActivator querydslActivator;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //요청을 바탕으로 유저정보를 담은 객체 반환
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;

    }

    //유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email=(String)attributes.get("email");
        String name=(String)attributes.get("name");
        if(userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email);
            return userRepository.save(user);

        }else{
             User newUser=User.builder()
                    .email(email)
                    .name(name)
                     .loginMethod("GOOGLE")
                     .nickname(name)
                    .build();
            return userRepository.save(newUser);
        }

    }
}
