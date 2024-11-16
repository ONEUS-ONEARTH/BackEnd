package kr.co.ouoe.User.controller;

import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.MeetingPost.dto.MeetingListResponseDTO;
import kr.co.ouoe.User.account.UserAccount;
import kr.co.ouoe.User.dto.LoginUserResponseDTO;
import kr.co.ouoe.User.dto.ModifyPasswordDTO;
import kr.co.ouoe.User.dto.ModifyUserRequestDTO;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.exception.DuplicateEmailException;
import kr.co.ouoe.User.exception.NoMatchAccountException;
import kr.co.ouoe.User.service.UserInfoservice;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

//내정보 페이지 관련 컨트롤러를 다룹니다.
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
    public ResponseEntity<?> compareTo(@AuthenticationPrincipal TokenUserInfo userAccount){
       // log.info(user.toString());
        User user = userInfoservice.getUserInfo(userAccount.getEmail());
        log.info("user info : {}", user.getName());
        log.info("user info : {}", user.getImagePath());
        log.info("user info : {}", user.getAdress());
        log.info("user info : {}", user.getPassword());
        log.info("user info : {}", user.getPhoneNumber());
        log.info("user info : {}", user.getNickname());
        LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO(user);
        log.info("loginUserResponseDTO: {}", loginUserResponseDTO);
        return ResponseEntity.ok().body(loginUserResponseDTO);
        //return null;
    }


    @GetMapping("/myupcyclePost")
    public ResponseEntity<?> searchMyPostList(@AuthenticationPrincipal TokenUserInfo userAccount){
        User user = userInfoservice.getUserInfo(userAccount.getEmail());
        //유저 아이디 이용해서 포스트 전부 검색
        log.info("user info : {}", user.getName());
        PostListResponseDTO postListResponseDTO=userInfoservice.getMyPost(user);
        return ResponseEntity.ok().body(postListResponseDTO);
    }
    //내가쓴 모임 게시물을 불러옵니다.
    @GetMapping("/mymeetingPost")
    public ResponseEntity<?> searchMyMeetingList(@AuthenticationPrincipal TokenUserInfo userAccount){
        User user = userInfoservice.getUserInfo(userAccount.getEmail());
        MeetingListResponseDTO meetingListResponseDTO=userInfoservice.getMyMeetingPost(user);
        return ResponseEntity.ok().body(meetingListResponseDTO);
    }

    // 유저가 찜한 미팅정보를 불러옵니다.
    @GetMapping("/mybookmark")
    public ResponseEntity<?> searchMyBookmarkList(@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        MeetingListResponseDTO meetingListResponseDTO=userInfoservice.getMyMeetingBookMarkList(tokenUserInfo);
        return ResponseEntity.ok().body(meetingListResponseDTO);
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

    @RequestMapping(method = {PUT,PATCH},path = "/modify/checkpassword")
    public ResponseEntity<?> checkCurrentEmail(@RequestBody  ModifyPasswordDTO dto, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.toString());
        }
        Boolean flag=userInfoservice.checkPassword(dto);

        return ResponseEntity.ok().body(flag);
    }





}
