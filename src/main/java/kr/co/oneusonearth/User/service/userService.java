package kr.co.oneusonearth.User.service;

import kr.co.oneusonearth.User.domain.User;
import kr.co.oneusonearth.User.dto.AddUserRequest;
import kr.co.oneusonearth.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class userService {

    private final UserRepository userRepository;
    private  final BCryptPasswordEncoder bCryptPasswordEncode;

    public Boolean singUp(AddUserRequest addUserRequest) {

        userRepository.save(
                User.builder()
                        .email(addUserRequest.getEmail())
                        .nickname(addUserRequest.getNickname())
                        .name(addUserRequest.getName())
                        .password(bCryptPasswordEncode.encode(addUserRequest.getPassword()))
                        .loginMethod("일반")
                        .adress(addUserRequest.getAdress())
                        .build());
        return true;

    }
}
