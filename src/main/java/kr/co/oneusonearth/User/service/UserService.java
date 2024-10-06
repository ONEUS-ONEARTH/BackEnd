package kr.co.oneusonearth.User.service;


import kr.co.oneusonearth.User.domain.User;
import kr.co.oneusonearth.User.dto.AddUserRequest;
import kr.co.oneusonearth.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Slf4j //로그
//@Transactional// JPA 사용시 필수
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     *
     * 일반회원가입
     * @param addUserRequest
     * @return Bolean
     */
    public Boolean singUp(AddUserRequest addUserRequest) {

        Boolean isVaild=checkDTOValid(addUserRequest);
        log.info("isVaild:{}",isVaild);

        if(isVaild){
            try{
                userRepository.save(
                        User.builder()
                                .email(addUserRequest.getEmail())
                                .nickname(addUserRequest.getNickname())
                                .name(addUserRequest.getName())
                                .password(bCryptPasswordEncoder.encode(addUserRequest.getPassword()))
                                .loginMethod("일반")
                                .phoneNumber(addUserRequest.getPhoneNumber())
                                .adress(addUserRequest.getAdress())
                                .build());
                //이미지 uri
                //String upload=FileUtl.convertNewPath(addUserRequest.getProfileImage);
                //aws 처리하고 파일패스 돌려받기
               // String aws_path=s3Service.uploadToS3Bucket(dto.getProfileImage().getBytes(), upload);
                log.info("회원가입 성공");
                return true;
            }catch (NullPointerException e){
                userRepository.save(
                        User.builder()
                                .email(addUserRequest.getEmail())
                                .nickname(addUserRequest.getNickname())
                                .name(addUserRequest.getName())
                                .password(bCryptPasswordEncoder.encode(addUserRequest.getPassword()))
                                .loginMethod("일반")
                                .phoneNumber(addUserRequest.getPhoneNumber())
                                .adress(addUserRequest.getAdress())
                                .build());

                log.info("회원가입성공");
                return  true;
            }


        }

        return true;

    }

    /**
     *
     * @return
     */
    /*
    public  LoginUserResponseDTO singIn(LogInDTO){
        //LoginDTO에 로그인 메서드 이용해서...
        boolean isVaild= checkLogInDTO(LogINDTO);
        if(isVaild){
            User loginUser=userRepository.findByEmail(LoginDTO.getEmail());
        }
    }
    */




    /**
     * 회원가입 dto의 유효성확인/dto null값,이메일중복여부 체크
     * @param addUserRequest
     * @return
     */
    private Boolean checkDTOValid(AddUserRequest addUserRequest) {
        if(addUserRequest==null){
            log.warn("회원정보가 업습니다");
            return false;

        }
        if(userRepository.existsUserByEmail(addUserRequest.getEmail())){
            System.out.println("여기로옴");
            log.warn("이메일이 중복되었습니다!");
            return false;
        }
        return true;

    }

}
