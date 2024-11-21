package kr.co.ouoe.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig implements WebMvcConfigurer {


        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")  // 모든 경로에 CORS 설정
                    .allowedOrigins("http://localhost:3000","http://localhost:8080","http://ouoereact.s3-website.ap-northeast-2.amazonaws.com/")  // 허용할 도메인
                    .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                    .allowedHeaders("*")  // 허용할 헤더
                    .allowCredentials(true)  // 쿠키 등 인증정보 허용 여부
                    .maxAge(3600);  // pre-flight 요청 캐싱 시간 (초 단위)
        }



}