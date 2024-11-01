package kr.co.ouoe.User.controller;

import kr.co.ouoe.User.dto.LoginUserResponseDTO;
import kr.co.ouoe.User.dto.ModifyUserRequestDTO;
import kr.co.ouoe.User.dto.TokenUserInfo;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.exception.DuplicateEmailException;
import kr.co.ouoe.User.exception.NoMatchAccountException;
import kr.co.ouoe.User.service.UserInfoservice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class userInfoController {

    @Autowired
    private UserInfoservice userInfoservice;

    // 유저 정보를 불러옵니다.
    @GetMapping("/myprofile")
    public ResponseEntity<?> compareTo(@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        User user = userInfoservice.getUserInfo(tokenUserInfo.getEmail());
        LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO(user);
        return ResponseEntity.ok().body(loginUserResponseDTO);
    }

    //유저정보를 수정합니다.(비밀번호, 이메일 제외)
    @RequestMapping(method = {PUT, PATCH}, path = "/modify")
    public ResponseEntity<?> updateUser(@Validated ModifyUserRequestDTO dto, BindingResult result){
        log.info("dto: {}", dto.toString());
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
        }
        try{
            boolean flag = userInfoservice.modifyUser(dto);
            return ResponseEntity.ok().body(flag);
        }catch (NoMatchAccountException | DuplicateEmailException  e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
