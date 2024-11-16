package kr.co.ouoe.MeetingPost.controller;


import jakarta.transaction.Transactional;
import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.MeetingPost.dto.MeetingListResponseDTO;
import kr.co.ouoe.MeetingPost.dto.reply.MeetingReplyModifyRequestDTO;
import kr.co.ouoe.MeetingPost.dto.reply.MeetingReplyRequestDTO;
import kr.co.ouoe.MeetingPost.dto.reply.MeetingReplyResponseList;
import kr.co.ouoe.MeetingPost.service.MeetingReplyService;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@Slf4j
@RequiredArgsConstructor
@RestController
@Transactional
@RequestMapping("/api/meeting")
public class MeetingPostReplyController {
    // 모임 댓글에 대한 기능을 게시 합니다.

    @Autowired
    private MeetingReplyService meetingReplyService;

    /**
     * postId에 소속 되어있는 모든 댓글을 불러옵니다.
     * @param postId
     * @return
     */
    @GetMapping("{postId}/replys")
    public ResponseEntity<?> getReplys(@PathVariable Long postId) {
        log.info("Get replys for postId: {}", postId);
        MeetingReplyResponseList meetingReplyResponseList=meetingReplyService.getAllReplysByPostId(postId);

        return ResponseEntity.ok().body(meetingReplyResponseList);
    }

    //댓글을 생성합니다.
    @PostMapping("/createreply")
    public ResponseEntity<?> createReplys(@RequestBody MeetingReplyRequestDTO  meetingReplyRequestDTO, @AuthenticationPrincipal TokenUserInfo tokenUserInfo) {

        MeetingReplyResponseList meetingReplyResponseList=meetingReplyService.createReply(meetingReplyRequestDTO,tokenUserInfo);
        return ResponseEntity.ok().body(meetingReplyResponseList);

    }

    // 댓글을 수정합니다.
    @RequestMapping(method = {PUT, PATCH},name = "/modifyreply")
    public ResponseEntity<?> modifyReply(@RequestBody MeetingReplyModifyRequestDTO meetingReplyRequestDTO ){
       MeetingReplyResponseList meetingReplyResponseList= meetingReplyService.modifyReply(meetingReplyRequestDTO);
        return ResponseEntity.ok().body(meetingReplyResponseList);

    }

    //댓글을 삭제 합니다.
    @DeleteMapping("/deletereply/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable Long replyId, @AuthenticationPrincipal TokenUserInfo tokenUserInfo) {
        if (replyId == null || replyId.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(PostListResponseDTO.builder().error("postId는 공백 일 수 없습니다!").build());
        }

        try{
            MeetingReplyResponseList meetingReplyResponseList=meetingReplyService.deleteReply(replyId,tokenUserInfo);
            return ResponseEntity.ok().body(meetingReplyResponseList);
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(PostListResponseDTO.builder().error(e.getMessage()).build());
        }

    }


}
