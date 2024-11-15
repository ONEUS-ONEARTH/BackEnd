package kr.co.ouoe.MeetingPost.controller;


import jakarta.transaction.Transactional;
import kr.co.ouoe.MeetingPost.dto.MeetingListResponseDTO;
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

    @PostMapping("/createReply")
    public ResponseEntity<?> createReplys(@RequestBody MeetingReplyRequestDTO  meetingReplyRequestDTO, @AuthenticationPrincipal TokenUserInfo tokenUserInfo) {

        return null;

    }
}
