package kr.co.ouoe.User.service;


import kr.co.ouoe.Util.FileUtil;
import kr.co.ouoe.Util.TokenProvider;
import kr.co.ouoe.common.BaseException;
import kr.co.ouoe.common.jwt.JwtTokenProvider;
import kr.co.ouoe.common.jwt.dto.TokenDto;
import kr.co.ouoe.User.dto.AddUserRequest;
import kr.co.ouoe.User.dto.LoginUserResponseDTO;
import kr.co.ouoe.User.entity.User;
import kr.co.ouoe.User.exception.IncorrectPasswordException;
import kr.co.ouoe.User.exception.NoLoginArgumentsException;
import kr.co.ouoe.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;



@Service
@Slf4j //로그
@Transactional// JPA 사용시 필수
public class UserService {

    //토큰 기간 설정
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
    //여기경로를 자기 컴퓨터에 맞게 바꿔주세요!
    private static final String FILEROOTPATH="D:\\Study\\OneusOnearth_BackEnd\\src\\main\\resources\\static\\images";
    //기본 프로필 이미지 경로에 맞게 바꾸기!
    private static final String basicProFilePath="D:\\Study\\FrontEnd\\public\\assets";

    private final UserRepository userRepository;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;//-> 순환참조의 원인 근데 이게 없으면 어떻게..?
    private final TokenProvider tokenProvider;
    public UserService(UserRepository userRepository, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider /* other dependencies */) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
    }


    /**
     *
     * 일반회원가입
     * @param addUserRequestDTO
     * @return Bolean
     */
    public Boolean singUp(AddUserRequest addUserRequestDTO) throws BaseException {

        Boolean isVaild=checkDTOValid(addUserRequestDTO);
        log.info("isVaild:{}",isVaild);

        if(isVaild){
            try{
                log.info(FILEROOTPATH);
                Path localuploadpath= FileUtil.upload(addUserRequestDTO.getImageFile(),FILEROOTPATH);
                log.info("localuploadpath:{}",localuploadpath);
                FileUtil.fileUpload(addUserRequestDTO.getImageFile(),localuploadpath);
               User save = userRepository.save(addUserRequestDTO.toEntity(bCryptPasswordEncoder,localuploadpath.toString()));
                log.info("회원가입 성공, 이미지 저장 버전");
                return true;
            }catch (NullPointerException e){
                userRepository.save(addUserRequestDTO.toEntity(bCryptPasswordEncoder,basicProFilePath));
                log.info("회원가입성공");
                return  true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        return true;

    }
    public LoginUserResponseDTO login(String email, String password) throws Exception {

        User user = userRepository.findByEmail(email);
        if(user==null){
            log.info("등록된 사용자가 없습니다! 회원가입 후 로그인 해주세요!");
            new NoLoginArgumentsException("로그인 정보가 없습니다");

        }
        String encodedPassword=user.getPassword();
        if(!bCryptPasswordEncoder.matches(password,encodedPassword)) {
            log.info("비민 번호가 틀렸습니다");
            throw new IncorrectPasswordException("비밀번호가 틀렸습니다.");
        }

//        //토큰 dto설정
        TokenDto tokenDto=new TokenDto("Bearer",tokenProvider.createToken(user), tokenProvider.generateRefreshToken(user));
        //token dto에 있는 리프레시토큰 db에 저장
        user.setRefreshToken(tokenDto.getRefreshToken());
//
        return new LoginUserResponseDTO(user,tokenDto);
//        return null;
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
        if(userRepository.existsByEmail(addUserRequest.getEmail())){
            System.out.println("여기로옴");
            log.warn("이메일이 중복되었습니다!");
            return false;
        }
        return true;

    }

    public Boolean checkEmail(String email) {
        log.info("중복여부 {}",userRepository.existsByEmail(email));
        return userRepository.existsByEmail(email);
    }


    //user_id(emailX)로 유저찾기
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    public User findByEmaild(String email) {
        return userRepository.findByEmail(email);
    }

    public Boolean checkPhone(String phone) {
        return userRepository.existsByEmail(phone);
    }
}
