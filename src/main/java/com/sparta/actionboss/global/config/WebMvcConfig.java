package com.sparta.actionboss.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowedOrigins("http://localhost:3000")
//                TODO : refresh token 추가 시
//                .exposedHeaders("Access", "Refresh")
                .allowedMethods("*")
                .maxAge(3000);
    }
}
