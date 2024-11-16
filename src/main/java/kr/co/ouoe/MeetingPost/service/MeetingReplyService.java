package kr.co.ouoe.MeetingPost.service;

import kr.co.ouoe.MeetingPost.domain.MeetingReply;
import kr.co.ouoe.MeetingPost.dto.reply.MeetingReplyModifyRequestDTO;
import kr.co.ouoe.MeetingPost.dto.reply.MeetingReplyRequestDTO;
import kr.co.ouoe.MeetingPost.dto.reply.MeetingReplyResponseDTO;
import kr.co.ouoe.MeetingPost.dto.reply.MeetingReplyResponseList;
import kr.co.ouoe.MeetingPost.repository.MeetingReplyRepository;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.repository.UserRepository;
import kr.co.ouoe.User.service.UserService;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingReplyService {
    // 모임 댓글에 대한 메서드를 게시 합니다.

    private final MeetingReplyRepository replyRepository;
    private final UserRepository userRepository;

    //모임에 달려있는 모든 댓글을  불러옵니다.
    public MeetingReplyResponseList getAllReplysByPostId(long postId) {

        List<MeetingReply> replys=replyRepository.findByPostId(postId);
        List<MeetingReplyResponseDTO> replyResponseList=new ArrayList<>();
        for(MeetingReply reply : replys){
            User user=userRepository.findOne(reply.getUserId());
            MeetingReplyResponseDTO meetingReplyResponseDTO=
                    MeetingReplyResponseDTO.builder()
                            .author(user.getNickname())
                            .content(reply.getContent())
                            .createDate(reply.getCreateDate())
                            .build();
            replyResponseList.add(meetingReplyResponseDTO);
        }

        MeetingReplyResponseList meetingReplyResponseList=new MeetingReplyResponseList();
        meetingReplyResponseList.setReplyList(replyResponseList);


        return meetingReplyResponseList;
    }

    //댓글을 게시합니다
    public MeetingReplyResponseList createReply(MeetingReplyRequestDTO dto, TokenUserInfo userInfo){

        // 댓글을 게시하는 유저 찾기
        User user=userRepository.findByEmail(userInfo.getEmail());
        LocalDateTime localDateTime=LocalDateTime.now();

        MeetingReply meetingReply=MeetingReply.builder()
                .userId(user.getId())
                .content(dto.getContent())
                .postId(dto.getPostId())
                .createDate(localDateTime)
                .build();
        replyRepository.save(meetingReply);
        // 댓글전체 다시로딩
        return getAllReplysByPostId(dto.getPostId());

    }

    //댓글을 수정합니다
    public MeetingReplyResponseList modifyReply(MeetingReplyModifyRequestDTO dto){
        User user=userRepository.findByEmail(dto.getEmail());
        LocalDateTime updateTime=LocalDateTime.now();
        Optional<MeetingReply> meetingReply=replyRepository.findById(dto.getReplyId());
        meetingReply.orElseThrow();

        meetingReply.get().setUserId(user.getId());
        meetingReply.get().setContent(dto.getContent());

        return getAllReplysByPostId(dto.getPostId());

    }

    //댓글을 삭제합니다
    public MeetingReplyResponseList deleteReply(long replyId, TokenUserInfo userInfo){
        Optional<MeetingReply> meetingReply= Optional.of(replyRepository.findById(replyId).orElseThrow());
        long postId=meetingReply.get().getPostId();
        replyRepository.delete(meetingReply.get());
        return getAllReplysByPostId(postId);
    }

}
