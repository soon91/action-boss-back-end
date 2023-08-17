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
                .allowedOrigins("http://localhost:3000",
                        "https://front-end-tau-henna.vercel.app",
                        "https://dev-front-end-omega-henna-44.vercel.app",
                        "https://test-eta-khaki.vercel.app",
                        "https://hdaejang.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .maxAge(3000);
    }
}
