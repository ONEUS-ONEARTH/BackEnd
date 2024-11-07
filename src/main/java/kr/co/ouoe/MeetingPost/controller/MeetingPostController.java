package kr.co.ouoe.MeetingPost.controller;


import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.DiyPost.dto.PostModifyRequestDTO;
import kr.co.ouoe.DiyPost.dto.PostRequestDTO;
import kr.co.ouoe.MeetingPost.dto.MeetingListResponseDTO;
import kr.co.ouoe.MeetingPost.dto.MeetingPostModifyRequestDTO;
import kr.co.ouoe.MeetingPost.dto.MeetingPostRequest;
import kr.co.ouoe.MeetingPost.dto.MeetingResponseDTO;
import kr.co.ouoe.MeetingPost.service.MeetingPostService;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meeting")
public class MeetingPostController {

    @Autowired
    private MeetingPostService meetingPostService;

    // 모인 전체 불러오기
    @GetMapping("/")
    public ResponseEntity<?> searchAllMeeting(){
        log.info("searchAllMeeting");
        MeetingListResponseDTO meetingResponseDTO= meetingPostService.searchAllMeeting();
        return ResponseEntity.ok().body(meetingResponseDTO);
    }

    //모임 지역별로 불러오기
    //si-> 시
    //doo-> 도
    //gu-> 구
    @GetMapping("/search")
    public ResponseEntity<?> searchAllMeeting(@RequestParam String si, @RequestParam String doo, @RequestParam String gu){
        log.info("searchAllMeeting");
        //어떻게 받아오지...?


        return null;

    }



    //모임 포스트 작성하기
    @PostMapping("/createmeeting")
    public ResponseEntity<?> createPost(@RequestBody MeetingPostRequest meetingPostRequest, @AuthenticationPrincipal TokenUserInfo tokenUserInfo, BindingResult result){

        log.info("createPost 실행중");
        if(result.hasErrors()){
            log.warn("DTO 검증 에러 입니다: {}", result.getAllErrors());
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try{
            MeetingListResponseDTO meetingListResponseDTO=meetingPostService.createMeeting(meetingPostRequest,tokenUserInfo.getEmail());
            return ResponseEntity.ok().body(meetingListResponseDTO);
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //모임 포스트 수정하기

    @RequestMapping(method = {PUT,PATCH},path = "/modify")
    public ResponseEntity<?> UpdatePost(@RequestBody MeetingPostModifyRequestDTO modifyRequestDTO){

        try{
            boolean isupdate=meetingPostService.modifyPost(modifyRequestDTO);
            return ResponseEntity.ok().body(isupdate);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



    //모임 포스트 삭제하기

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        if (postId == null || postId.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(PostListResponseDTO.builder().error("postId는 공백 일 수 없습니다!").build());
        }
        try{
            MeetingListResponseDTO meetingListResponseDTO=meetingPostService.delete(tokenUserInfo.getEmail(),postId);
            return  ResponseEntity.ok().body(meetingListResponseDTO);
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(PostListResponseDTO.builder().error(e.getMessage()).build());
        }

    }


}