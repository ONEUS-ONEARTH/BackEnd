package kr.co.ouoe.MeetingPost.service;


import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.DiyPost.dto.PostRequestDTO;
import kr.co.ouoe.DiyPost.dto.PostResponseDTO;
import kr.co.ouoe.DiyPost.entity.DiyPost;
import kr.co.ouoe.MeetingPost.domain.MeetingLocate;
import kr.co.ouoe.MeetingPost.domain.MeetingPost;
import kr.co.ouoe.MeetingPost.dto.MeetingListResponseDTO;
import kr.co.ouoe.MeetingPost.dto.MeetingPostRequest;
import kr.co.ouoe.MeetingPost.dto.MeetingResponseDTO;
import kr.co.ouoe.MeetingPost.repository.MeetingLocateRepository;
import kr.co.ouoe.MeetingPost.repository.MeetingPostRepository;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingPostService {
    private final MeetingPostRepository meetingPostRepository;
    private final UserRepository userRepository;
    private final MeetingLocateRepository meetingLocateRepository;

    public MeetingListResponseDTO searchAllMeeting(){
        List<MeetingResponseDTO> postList=meetingPostRepository.findAllMeetingResponseDTO();

        //닉네임 찾아서 넣어주기
        for(MeetingResponseDTO meetingPost:postList){
            String nickname=userRepository.findById(meetingPost.getUserId()).get().getNickname();
            meetingPost.setAuthor(nickname);
        }

        return MeetingListResponseDTO.builder()
                .boards(postList)
                .build();

    }


    //미팅포스트 작성
    public MeetingListResponseDTO createMeeting(MeetingPostRequest meetingPostRequest, String email) {

        log.info(meetingPostRequest.getTitle());
        User user=userRepository.findByEmail(email);
        if(user==null){
            return null;
        }

        //1.MeetingPost저장 저장,태그까지 저장
        LocalDateTime createDateTime=LocalDateTime.now();
        MeetingPost newMeetingPost= new MeetingPost(meetingPostRequest.getTitle(), meetingPostRequest.getContent(),createDateTime, meetingPostRequest.getThumbnailUrl(),user.getId());
        long meetingPostId= meetingPostRepository.save(newMeetingPost).getId();
        //2. Meeting Locate 저장
        MeetingLocate newMeetingLocate=MeetingLocate.builder().lantitude(meetingPostRequest.getX()).longitude(meetingPostRequest.getY()).build();
        long meetingLocateId=meetingPostRepository.save(newMeetingPost).getId();
        // 3. Meeting Locate 아이디 MetingPost에 저장
        meetingPostRepository.findById(meetingPostId).get().setMeetingLocateId(meetingLocateId);
        return searchAllMeeting();

    }


}
