package kr.co.oneusonearth.user.controller;

import kr.co.oneusonearth.common.BaseException;
import kr.co.oneusonearth.user.dto.AddUserRequest;
import kr.co.oneusonearth.user.dto.UserLoginRequestDto;
import kr.co.oneusonearth.user.entity.User;
import kr.co.oneusonearth.user.exception.NoDuplicateCheckArgumentException;
import kr.co.oneusonearth.user.service.UserService;
import kr.co.oneusonearth.exception.DuplicatedAccountException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class userController {

    private static final Logger log = LoggerFactory.getLogger(userController.class);
    @Autowired
    private UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<?> SignUp(@RequestBody AddUserRequest addUserRequestDTO, BindingResult result) {

        log.info("비밀번호{}",addUserRequestDTO.getPassword());
        if(result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try {
            Boolean flag=userService.singUp(addUserRequestDTO);
            return ResponseEntity.ok().body(flag);

        }catch (DuplicatedAccountException e){
            log.warn("계정이 중복되었습니다");
            return  ResponseEntity.badRequest().body(e.getMessage());
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }


    }

   @PostMapping("/login")
    public  ResponseEntity<?> login(@RequestBody @Validated UserLoginRequestDto loginRequestDto){
        String email= loginRequestDto.getEmail();
        String password= loginRequestDto.getPassword();

        return ResponseEntity.ok().body(userService.login(email,password));

    }

    // check:  //phcheck

    @PostMapping("/emailcheck")
    public ResponseEntity<?> emailCheck(@RequestBody String email,BindingResult result){
        log.info("email {}",email);
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());

        }
        try{
            Boolean isEmailExsist=userService.checkEmail(email);
            return ResponseEntity.ok().body(isEmailExsist);

        }catch (NoDuplicateCheckArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/phonecheck")
    public ResponseEntity<?> phoneCheck(@RequestBody String phone,BindingResult result){
        log.info("email {}",phone);
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());

        }
        try{
            Boolean isEmailExsist=userService.checkPhone(phone);
            return ResponseEntity.ok().body(isEmailExsist);

        }catch (NoDuplicateCheckArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
