package com.minjae.my_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//Cross-Origin Resource Sharing  - Same Origin Policy - 해당 도메인만 허용 (CSRF 방지)
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 백엔드의 /api/로 시작하는 주소는 다음 코드의 CORS 규칙의 검사를 받는다.
                .allowedOrigins("http://localhost:5173") //Access-Control-Allow-Origin: http://localhost:5173
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/api/admin/**") // /api/admin/
                .allowedOrigins("http://localhost:5173") // 나중에 변경
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
}
}