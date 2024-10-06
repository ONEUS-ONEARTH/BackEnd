package kr.co.oneusonearth.User.controller;

import kr.co.oneusonearth.User.dto.AddUserRequest;
import kr.co.oneusonearth.User.service.UserService;
import kr.co.oneusonearth.exception.DuplicatedAccountException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class userController {

    private static final Logger log = LoggerFactory.getLogger(userController.class);
    @Autowired
    private UserService service;
    @PostMapping("/signup")
    public ResponseEntity<?> SignUp(@RequestBody @Validated  AddUserRequest addUserRequest, BindingResult result) {

        if(result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try {

            Boolean flag=service.singUp(addUserRequest);
            return ResponseEntity.ok().body(flag);

        }catch (DuplicatedAccountException e){
            log.warn("계정이 중복되었습니다");
            return  ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
