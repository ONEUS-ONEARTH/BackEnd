package kr.co.ouoe.common.config;

import kr.co.ouoe.common.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    //oaut
    /*
    시큐리티가 로그인 과정에서 password를 가로챌때 어떤 해쉬로 암호화 했는지 확인
     */

//    protected void  configure (AuthenticationManagerBuilder auth) throws  Exception{
//        auth.userDetailsService(customUserDetailService).passwordEncoder(encoder());
//    }

    @Bean // 라이브러리 클래스같은 내가만들지 않은 객체를 등록해서 주입받기 위한 아노테이션
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // CSRF 토큰공격을 방지하기위한 장치 해제
                // 모든요청에 대해서 인증을 하지 않겠다.
                .authorizeRequests( auth->auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/user/**"),
                                new AntPathRequestMatcher("/api/upcycle/modify"),
                                new AntPathRequestMatcher("/api/upcycle/posts/**"),
                                new AntPathRequestMatcher("/api/meeting/posts/**"),
                                new AntPathRequestMatcher("/api/meeting/modify"),
                                new AntPathRequestMatcher("/api/meeting/map/**")

                        ).permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutSuccessUrl("/api/user/login")
                        .invalidateHttpSession(true)
                );
//                .and()//ㅐoauth
//                .oauth2Login()
//                .userInfoEndpoint()//oauth로그인 성공후 가져올 설정들;//서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
        ;
        // 토큰 인증 필터 연결하기
        http.addFilterAfter(jwtAuthFilter, CorsFilter.class);
        return http.build();
    }

    // 비밀번호 암호화 객체를 빈 등록
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
