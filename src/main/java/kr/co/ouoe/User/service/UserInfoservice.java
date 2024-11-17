package kr.co.ouoe.User.service;


import kr.co.ouoe.DiyPost.dto.PostListResponseDTO;
import kr.co.ouoe.DiyPost.dto.PostResponseDTO;
import kr.co.ouoe.DiyPost.repository.DiyPostRepository;
import kr.co.ouoe.MeetingPost.dto.MeetingListResponseDTO;
import kr.co.ouoe.MeetingPost.dto.MeetingResponseDTO;
import kr.co.ouoe.MeetingPost.repository.MeetingPostRepository;
import kr.co.ouoe.User.account.BookMarkCategory;
import kr.co.ouoe.User.dto.ModifyPasswordDTO;
import kr.co.ouoe.User.dto.ModifyUserRequestDTO;
import kr.co.ouoe.User.entity.BookMark;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.exception.DuplicateEmailException;
import kr.co.ouoe.User.exception.NoMatchAccountException;
import kr.co.ouoe.User.repository.BookMarkRepository;
import kr.co.ouoe.User.repository.UserRepository;
import kr.co.ouoe.Util.FileUtil;
import kr.co.ouoe.Util.S3Uploader;
import kr.co.ouoe.Util.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j //로그
@Transactional// JPA 사용시 필수
public class UserInfoservice {

    private final UserRepository userRepository;
    private final DiyPostRepository diyPostRepository;
    private final MeetingPostRepository meetingPostRepository;
    private final UserService userService;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3Uploader s3Uploader;

    private static final String FILEROOTPATH="D:\\Study\\OneusOnearth_BackEnd\\src\\main\\resources\\static\\images";
    private final BookMarkRepository bookMarkRepository;

    public User getUserInfo(String email){

        return  userRepository.findByEmail(email);

    }
    //유저 정보를 수정합니다.
    public Boolean modifyUser(ModifyUserRequestDTO dto,TokenUserInfo tokenUserInfo) throws IOException {
        User user= userRepository.findByEmail(tokenUserInfo.getEmail());
        if (user == null) {
            throw new NoMatchAccountException("일치하는 계정이 없습니다");
        }
        if(userService.checkPhone(dto.getPhone())){
            throw  new DuplicateEmailException("휴대폰 번호가 이미 존재합니다.");
        }

        //유저 변경중
        user.setPhoneNumber(dto.getPhone());
        user.setAdress(dto.getAdress());
        user.setNickname(dto.getNickname());

        // dto에 들어온 멀티파트 파일 처리
        String url=s3Uploader.uploadFileToS3(dto.getImage());
        //Path localuploadpath= FileUtil.upload(dto.getImage(),FILEROOTPATH);
        //FileUtil.fileUpload(dto.getImage(),localuploadpath);

        //user에 저장
        user.setImagePath(url);


        return true;



    }

    //현재 비밀번호 일치 여부를 확인 합니다.
    public Boolean checkPassword(ModifyPasswordDTO dto){
        User user= userRepository.findByEmail(dto.getEmail());
        String encodedPassword=user.getPassword();
        String rawPassword=dto.getRawPassword();

        if(!bCryptPasswordEncoder.matches(rawPassword,encodedPassword)){
            return false;
        }

        return true;

    }


    //내 포스트 정보를 불러옵니다.
    public PostListResponseDTO getMyPost(User user){

        //유저  Id
        long userId=user.getId();
        //포스트 불러오기
        List<PostResponseDTO> postResponseDTOList=diyPostRepository.findAllByuserId(userId);
        for(PostResponseDTO postResponseDTO:postResponseDTOList){
            postResponseDTO.setAuthor(user.getNickname());
        }
        PostListResponseDTO postListResponseDTO=PostListResponseDTO.builder()
                .boards(postResponseDTOList)
                .build();
        return postListResponseDTO;

    }

    // 내가 북마크해둔 미팅 정보를 불러옵니다.
    public MeetingListResponseDTO getMyMeetingBookMarkList(TokenUserInfo tokenUserInfo){
        User user=userRepository.findByEmail(tokenUserInfo.getEmail());
        List<BookMark> bookMarkList=bookMarkRepository.findAllByUserIdAndBookMarkCategory(user.getId(), BookMarkCategory.MEETING.toString());
        List<MeetingResponseDTO> meetingResponseDTOList=new ArrayList<>();

        for(BookMark bookMark:bookMarkList){
            MeetingResponseDTO meetingResponseDTO=new MeetingResponseDTO();
            meetingPostRepository.findById(bookMark.getPostId()).ifPresent(post->{
                meetingResponseDTO.setMeetingId(post.getId());
                meetingResponseDTO.setUserId(post.getUserId());
                meetingResponseDTO.setEditable(true);
                meetingResponseDTO.setCreateDate(post.getCreatedAt());
                meetingResponseDTO.setTitle(post.getTitle());
                meetingResponseDTO.setContent(post.getContent());
                meetingResponseDTO.setAuthor(user.getNickname());
                meetingResponseDTO.setThumbnailUrl(post.getThumbNail());
                meetingResponseDTO.setOption(post.getOption().toString());
            });
            meetingResponseDTOList.add(meetingResponseDTO);
        }
        MeetingListResponseDTO meetingListResponseDTO=MeetingListResponseDTO.builder().boards(meetingResponseDTOList).build();
        return meetingListResponseDTO;

    }

    //내가 쓴 미팅정보 게시물은 불러옵니다.
    public MeetingListResponseDTO  getMyMeetingPost(User user){
        List<MeetingResponseDTO> meetingResponseDTOList=meetingPostRepository.findAllMeetingResponseDTOByUserId(user.getId());
        MeetingListResponseDTO meetingListResponseDTO=MeetingListResponseDTO.builder().boards(meetingResponseDTOList).build();
        return meetingListResponseDTO;
    }
}
