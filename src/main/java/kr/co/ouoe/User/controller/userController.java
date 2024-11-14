package kr.co.ouoe.User.controller;

import jakarta.transaction.Transactional;
import kr.co.ouoe.User.dto.*;
import kr.co.ouoe.User.exception.IncorrectPasswordException;
import kr.co.ouoe.User.exception.NoLoginArgumentsException;
import kr.co.ouoe.common.BaseException;
import kr.co.ouoe.User.exception.DuplicateEmailException;
import kr.co.ouoe.User.exception.NoDuplicateCheckArgumentException;
import kr.co.ouoe.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Transactional
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class userController {

    private static final Logger log = LoggerFactory.getLogger(userController.class);
    @Autowired
    private UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<?> SignUp(AddUserRequest addUserRequestDTO, BindingResult result) {

        log.info("비밀번호{}",addUserRequestDTO.getPassword());
        if(result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try {
            Boolean flag=userService.singUp(addUserRequestDTO);
            return ResponseEntity.ok().body(flag);

        }catch (DuplicateEmailException e){
            log.warn("계정이 중복되었습니다");
            return  ResponseEntity.badRequest().body(e.getMessage());
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }


    }

   @PostMapping("/login")
    public  ResponseEntity<?> login(@RequestBody @Validated UserLoginRequestDto loginRequestDto) throws Exception {
        String email= loginRequestDto.getEmail();
        String password= loginRequestDto.getPassword();

        try{
            LoginUserResponseDTO loginUserResponseDTO=userService.login(email,password);
            return ResponseEntity.ok().body(loginUserResponseDTO);
        }catch (NoLoginArgumentsException| IncorrectPasswordException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    // check:  //phcheck

    @PostMapping("/emailcheck")
    public ResponseEntity<?> emailCheck(@RequestBody EmailCheckRequest emailCheckRequest, BindingResult result){
        log.info("email {}",emailCheckRequest.getEmail());
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());

        }
        try{
            Boolean isEmailExsist=userService.checkEmail(emailCheckRequest.getEmail());
            return ResponseEntity.ok().body(isEmailExsist);

        }catch (NoDuplicateCheckArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/phonecheck")
    public ResponseEntity<?> phoneCheck(@RequestBody PhoneCheckRequest phoneCheckRequest, BindingResult result){
        log.info("email {}",phoneCheckRequest.getPhoneNumber());
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());

        }
        try{
            Boolean isEmailExsist=userService.checkPhone(phoneCheckRequest.getPhoneNumber());
            return ResponseEntity.ok().body(isEmailExsist);

        }catch (NoDuplicateCheckArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
