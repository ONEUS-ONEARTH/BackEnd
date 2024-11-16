package kr.co.ouoe.MeetingPost.controller;


import jakarta.transaction.Transactional;
import kr.co.ouoe.DiyPost.dto.*;
import kr.co.ouoe.MeetingPost.dto.*;
import kr.co.ouoe.MeetingPost.service.MeetingPostService;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@RequiredArgsConstructor
@RestController
@Transactional
@RequestMapping("/api/meeting")
public class MeetingPostController {

    @Autowired
    private MeetingPostService meetingPostService;

    // 모인 전체 불러오기
    @GetMapping("/posts")
    public ResponseEntity<?> searchAllMeeting(){
        log.info("searchAllMeeting, 들어옴??");
        MeetingListResponseDTO meetingResponseDTO= meetingPostService.searchAllMeeting();
        return ResponseEntity.ok().body(meetingResponseDTO);
    }

    // 모임 페이징 처리
    @GetMapping("/pageNo/{pageNo}")
    public ResponseEntity<?> searchMeetingPostListBypageNo(@PathVariable int pageNo){
        List<MeetingResponseDTO> postResponseDTOList=meetingPostService.searchPostListWithPage(pageNo-1);
        int allPageNo=meetingPostService.getAllPageNo(pageNo-1);
        PageMeetingPostResponseDTO postListResponseDTO= PageMeetingPostResponseDTO.builder().list(postResponseDTOList).allPageNo(allPageNo).build();
        return ResponseEntity.ok().body(postListResponseDTO);

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

    // 포스트 북마크 하기
    @GetMapping("/bookmark")
    public ResponseEntity<?> BookmarkOnPost(@AuthenticationPrincipal TokenUserInfo tokenUserInfo,@RequestParam long postId){
        if(tokenUserInfo==null){
            return ResponseEntity.badRequest().body("로그인한 사용자만 이용할수 있어요!");
        }
        boolean isbookmarked=meetingPostService.bookmark(tokenUserInfo.getEmail(),postId);


        return ResponseEntity.ok().body(isbookmarked);
    }



    //모임 포스트 작성하기
    @PostMapping("/createmeeting")
    public ResponseEntity<?> createPost(MeetingPostRequest meetingPostRequest, @AuthenticationPrincipal TokenUserInfo tokenUserInfo, BindingResult result){

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

    //모임 포스트 디테일 불러오기
    //업싸이클 디테일 보여주는 로직
    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> showPostDeatil (@PathVariable Long postId,@AuthenticationPrincipal TokenUserInfo tokenUserInfo){
        if (postId == null || postId.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(PostListResponseDTO.builder().error("postId는 공백 일 수 없습니다!").build());
        }try{
            MeetingResponseDTO postResponseDTO=meetingPostService.searchPostById(postId,tokenUserInfo);
            return ResponseEntity.ok().body(postResponseDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }



}
