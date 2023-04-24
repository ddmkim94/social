package com.ll.social.app.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/gen/**") // 해당 url 로 요청이 오는경우 -> localhost:8010/gen/**
                .addResourceLocations("file:///" + fileDirPath); // 이 위치에서 리소스를 찾을거임
    }
}
