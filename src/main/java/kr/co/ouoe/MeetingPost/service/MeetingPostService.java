package kr.co.ouoe.MeetingPost.service;


import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.DiyPost.dto.PostRequestDTO;
import kr.co.ouoe.DiyPost.dto.PostResponseDTO;
import kr.co.ouoe.DiyPost.entity.DiyPost;
import kr.co.ouoe.MeetingPost.domain.MeetingLocate;
import kr.co.ouoe.MeetingPost.domain.MeetingPost;
import kr.co.ouoe.MeetingPost.domain.Option;
import kr.co.ouoe.MeetingPost.dto.MeetingListResponseDTO;
import kr.co.ouoe.MeetingPost.dto.MeetingPostModifyRequestDTO;
import kr.co.ouoe.MeetingPost.dto.MeetingPostRequest;
import kr.co.ouoe.MeetingPost.dto.MeetingResponseDTO;
import kr.co.ouoe.MeetingPost.repository.MeetingLocateRepository;
import kr.co.ouoe.MeetingPost.repository.MeetingPostRepository;
import kr.co.ouoe.User.account.BookMarkCategory;
import kr.co.ouoe.User.entity.BookMark;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.repository.BookMarkRepository;
import kr.co.ouoe.User.repository.UserRepository;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingPostService {
    private final MeetingPostRepository meetingPostRepository;
    private final UserRepository userRepository;
    private final MeetingLocateRepository meetingLocateRepository;
    private final BookMarkRepository bookMarkRepository;

    public MeetingListResponseDTO searchAllMeeting() {
        List<MeetingResponseDTO> postList = meetingPostRepository.findAllMeetingResponseDTO();

        //닉네임 찾아서 넣어주기
        for (MeetingResponseDTO meetingPost : postList) {
            log.info(meetingPost.toString());
            long userId=meetingPost.getUserId();
            log.info(userId+"유저 아이디");
            String nickname=userRepository.findById(userId).get().getNickname();
            meetingPost.setAuthor(nickname);
        }

        return MeetingListResponseDTO.builder()
                .boards(postList)
                .build();

    }


    // 페이징 처리된 보드 불러오기
    public List<MeetingResponseDTO> searchPostListWithPage(int pageNo) {
        PageRequest pageRequest = PageRequest.of(pageNo, 10, Sort.by("postUpdateDateTime").descending());
        Page<MeetingPost> result = meetingPostRepository.findAll(pageRequest);
        int totalPage = result.getTotalPages();
        List<MeetingResponseDTO> list = new ArrayList<>();
        list.forEach(meetingPost -> {
            MeetingResponseDTO meetingResponseDTO = new MeetingResponseDTO();
            meetingResponseDTO.setId(meetingPost.getId());
            meetingResponseDTO.setTitle(meetingPost.getTitle());
            meetingResponseDTO.setContent(meetingPost.getContent());
            meetingResponseDTO.setThumbnailUrl(meetingPost.getThumbnailUrl());
            meetingResponseDTO.setAuthor(meetingPost.getAuthor());
            meetingResponseDTO.setCreateDate(meetingPost.getCreateDate());
            meetingResponseDTO.setUserId(meetingPost.getUserId());
            meetingResponseDTO.setMeetingId(meetingPost.getMeetingId());
            list.add(meetingResponseDTO);


        });

        return list;
    }

    public int getAllPageNo(int pageNo) {
        PageRequest pageRequest = PageRequest.of(pageNo, 6, Sort.by("postUpdateDateTime").descending());
        Page<MeetingPost> result = meetingPostRepository.findAll(pageRequest); // 해당 페이지 리스트
        return result.getTotalPages();
    }


    //미팅포스트 작성
    public MeetingListResponseDTO createMeeting(MeetingPostRequest meetingPostRequest, String email) {

        log.info(meetingPostRequest.getTitle());
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }


        //1.MeetingPost저장 저장,태그까지 저장
        LocalDateTime createDateTime = LocalDateTime.now();
        MeetingLocate meetingLocate = MeetingLocate.builder().x(meetingPostRequest.getX()).y(meetingPostRequest.getY()).build();

        MeetingPost newMeetingPost = new MeetingPost(meetingPostRequest.getTitle(), meetingPostRequest.getContent(), createDateTime, meetingPostRequest.getThumbnailUrl(), user.getId(),
                meetingPostRequest.getOption().equals("개인") ? Option.개인 : Option.회사, meetingLocate);
        meetingPostRepository.save(newMeetingPost);
        // long meetingPostId= meetingPostRepository.save(newMeetingPost).getId();


        return searchAllMeeting();

    }

    //미팅 포스트 수정
    public boolean modifyPost(MeetingPostModifyRequestDTO modifyRequestDTO) {
        MeetingPost meetingPost = meetingPostRepository.getOne(modifyRequestDTO.getMeetingPostId());
        //1. 포스트 수정
        meetingPost.setTitle(modifyRequestDTO.getTitle());
        meetingPost.setContent(modifyRequestDTO.getContent());
        meetingPost.setThumbNail(modifyRequestDTO.getThumbnailUrl());
        //2 . 위치(경도,위도) 수정
//        MeetingLocate meetingLocate=meetingLocateRepository.getOne(meetingPost.getMeetingLocateId());
//        meetingLocate.setLantitude(modifyRequestDTO.getX());
//        meetingLocate.setLongitude(modifyRequestDTO.getY());

        return true;
    }


    //미팅 포스트 삭제
    public MeetingListResponseDTO delete(String useremail, Long boardNo) {
        try {
            //유저 찾기
            User user = userRepository.findByEmail(useremail);
            // 보드를 찾기
            MeetingPost post = meetingPostRepository.findByIdAndUserId(boardNo, user.getId());
            if (post == null) {
                log.warn("삭제할 보드를 찾을 수 없습니다. 계정: {}, 보드 번호: {}", useremail, boardNo);
                return null;
            }

            // 보드를 삭제
            meetingPostRepository.deleteById(boardNo);

            log.info("보드 삭제 성공. 계정: {}, 보드 번호: {}", useremail, boardNo);
        } catch (Exception e) {
            log.error("보드 삭제 중 오류 발생. 계정: {}, 보드 번호: {}", useremail, boardNo, e);
        }
        return searchAllMeeting();
    }

    //모임 북마크 기능
    public Boolean bookmark(String useremail, Long postNo) {

        User user = userRepository.findByEmail(useremail);

        // 북마크가 이미 존재하는지 확인
        boolean isExists = bookMarkRepository.existsBookMarkByUserIdAndAndPostId(user.getId(), postNo);

        if (isExists) {
            return false;
        }
        BookMark bookMark = new BookMark();
        bookMark.setPostId(postNo);
        bookMark.setBookMarkCategory(BookMarkCategory.MEETING);
        bookMark.setUserId(user.getId());
        bookMarkRepository.save(bookMark);
        return true;


    }


    //포스트 디테일 불러오기
    public MeetingResponseDTO searchPostById(Long id, TokenUserInfo tokenUserInfo) {

        MeetingPost meetingPost = meetingPostRepository.getOne(id);
        User user = userRepository.getOne(meetingPost.getUserId());
        MeetingResponseDTO meetingResponseDTO;

        if (tokenUserInfo == null) {
            // 토큰이 없으면 로그인한 사용자가 아님
            meetingResponseDTO = new MeetingResponseDTO();
            meetingResponseDTO.setId(meetingPost.getId());
            meetingResponseDTO.setTitle(meetingPost.getTitle());
            meetingResponseDTO.setContent(meetingPost.getContent());
            meetingResponseDTO.setCreateDate(meetingPost.getCreatedAt());
            meetingResponseDTO.setEditable(false);
            meetingResponseDTO.setAuthor(user.getNickname());
            meetingResponseDTO.setUserId(user.getId());
            meetingResponseDTO.setThumbnailUrl(meetingPost.getThumbNail());
            //meetingResponseDTO.setMeetingId(meetingPost.getMeetingLocateId());

        } else {
            if (tokenUserInfo.getEmail().equals(user.getEmail())) {
                meetingResponseDTO = new MeetingResponseDTO();
                meetingResponseDTO.setId(meetingPost.getId());
                meetingResponseDTO.setTitle(meetingPost.getTitle());
                meetingResponseDTO.setContent(meetingPost.getContent());
                meetingResponseDTO.setCreateDate(meetingPost.getCreatedAt());
                meetingResponseDTO.setEditable(true);
                meetingResponseDTO.setAuthor(user.getNickname());
                meetingResponseDTO.setUserId(user.getId());
                meetingResponseDTO.setThumbnailUrl(meetingPost.getThumbNail());
                //meetingResponseDTO.setMeetingId(meetingPost.getMeetingLocateId());

            } else {
                meetingResponseDTO = new MeetingResponseDTO();
                meetingResponseDTO.setId(meetingPost.getId());
                meetingResponseDTO.setTitle(meetingPost.getTitle());
                meetingResponseDTO.setContent(meetingPost.getContent());
                meetingResponseDTO.setCreateDate(meetingPost.getCreatedAt());
                meetingResponseDTO.setEditable(false);
                meetingResponseDTO.setAuthor(user.getNickname());
                meetingResponseDTO.setUserId(user.getId());
                meetingResponseDTO.setThumbnailUrl(meetingPost.getThumbNail());
                // meetingResponseDTO.setMeetingId(meetingPost.getMeetingLocateId());
            }
        }

        return meetingResponseDTO;
    }


}
