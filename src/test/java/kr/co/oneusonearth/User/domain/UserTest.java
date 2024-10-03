package kr.co.oneusonearth.User.domain;

import kr.co.oneusonearth.User.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserTest {

    private static final Logger log = LoggerFactory.getLogger(UserTest.class);
    @Autowired
    protected UserRepository userRepository;

    protected BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    @BeforeEach
    public void mockMvcsetUp() {
        userRepository.deleteAll();
    }

    @DisplayName("addUser: 유저 등록에 성공한다")
    @Test
    void addUser() throws Exception {
        //given
        final String email = "seon@naver.com";
        final String password = "123456";
        final String nickName = "선선";
        final String name = "장선경";
        final String loginMethod = "일반";
        final String address = "일반";

        User seonUser = new User();
        seonUser = seonUser.builder()
                .name(name)
                .nickname(nickName)
                .email(email)
                .loginMethod(loginMethod)
                .password(encoder.encode(password))
                .adress(address)
                .build();
        userRepository.save(seonUser);
        assertEquals(1, userRepository.findAll().size());
        Optional<User> user=userRepository.findByEmail(email);
        log.info(user.get().getPassword());


    }


}