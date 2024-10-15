package kr.co.oneusonearth.user.service;


import kr.co.oneusonearth.Util.FileUtil;
import kr.co.oneusonearth.common.BaseException;
import kr.co.oneusonearth.common.jwt.dto.TokenDto;
import kr.co.oneusonearth.user.entity.User;
import kr.co.oneusonearth.user.dto.AddUserRequest;
import kr.co.oneusonearth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;


@RequiredArgsConstructor
@Service
@Slf4j //로그
//@Transactional// JPA 사용시 필수
public class UserService {

    //토큰 기간 설정
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
    //여기경로를 자기 컴퓨터에 맞게 바꿔주세요!
    private static final String FILEROOTPATH="D:\\Study\\";

    private final UserRepository userRepository;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
   // private final JwtTokenProvider jwtTokenProvider;

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
                Path localuploadpath=FileUtil.upload(addUserRequestDTO.getProfile(),FILEROOTPATH);
                log.info("localuploadpath:{}",localuploadpath);
                FileUtil.fileUpload(addUserRequestDTO.getProfile(),localuploadpath);
                User save = userRepository.save(addUserRequestDTO.toEntity(bCryptPasswordEncoder,localuploadpath.toString()));
                log.info("회원가입 성공, 이미지 저장 버전");
                return true;
            }catch (NullPointerException e){
                userRepository.save(addUserRequestDTO.toEntity(bCryptPasswordEncoder,""));
                log.info("회원가입성공");
                return  true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        return true;

    }
   /* public TokenDto login(String email, String password) {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        //TokenDto tokenDto= jw
    }*

    */

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
