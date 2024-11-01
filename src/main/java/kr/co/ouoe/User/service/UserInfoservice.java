package kr.co.ouoe.User.service;


import kr.co.ouoe.User.dto.ModifyUserRequestDTO;
import kr.co.ouoe.User.dto.TokenUserInfo;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.exception.DuplicateEmailException;
import kr.co.ouoe.User.exception.NoMatchAccountException;
import kr.co.ouoe.User.repository.UserRepository;
import kr.co.ouoe.Util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;


@RequiredArgsConstructor
@Service
@Slf4j //로그
@Transactional// JPA 사용시 필수
public class UserInfoservice {

    private final UserRepository userRepository;
    private final UserService userService;

    private static final String FILEROOTPATH="D:\\Study\\OneusOnearth_BackEnd\\src\\main\\resources\\static\\images";

    public User getUserInfo(String email){

        return  userRepository.findByEmail(email);

    }
    //유저 정보를 수정합니다.
    public Boolean modifyUser(ModifyUserRequestDTO dto) throws IOException {
        User user= userRepository.findByEmail(dto.getEmail());
        if (user == null) {
            throw new NoMatchAccountException("일치하는 계정이 없습니다");
        }
        if(userService.checkPhone(dto.getPhone())){
            throw  new DuplicateEmailException("휴대폰 번호가 이미 존재합니다.");
        }

        //유저 변경중
        user.setPhoneNumber(dto.getPhone());
        user.setAdress(dto.getAdress());
        user.setNickname(dto.getNickname());

        // dto에 들어온 멀티파트 파일 처리
        Path localuploadpath= FileUtil.upload(dto.getImage(),FILEROOTPATH);
        FileUtil.fileUpload(dto.getImage(),localuploadpath);

        //user에 저장
        user.setImagePath(localuploadpath.toString());


        return true;



    }
}
