package com.project.llol.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${spring.servlet.multipart.location}")
    private String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // URI가 'upload/' 이하로 요청 될 경우, 로컬 disk의 특정 위치 경로로 파일을 사용자에게 제공하겠다 라는 뜻
        registry.addResourceHandler("upload/**").addResourceLocations("file://" + filePath);
    }
}
