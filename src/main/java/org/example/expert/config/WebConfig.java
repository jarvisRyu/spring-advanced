package org.example.expert.config;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.aop.interceptor.AdminAccessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // ArgumentResolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver());
    }

    //1.인터셉터 레지스트리 오버라이드
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        WebMvcConfigurer.super.addInterceptors(registry); 기본구현 비어있음
        registry.addInterceptor(new AdminAccessInterceptor()) //어떤 레지스트리를 등록할지 지정
                .addPathPatterns("/admin/**"); //인터셉터를 적용시킬 경로
                // .excludePathPatterns -제외시킬 경로

    }
}
