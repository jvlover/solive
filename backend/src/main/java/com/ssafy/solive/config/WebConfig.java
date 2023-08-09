package com.ssafy.solive.config;

import com.ssafy.solive.common.util.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Autowired
    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new JwtInterceptor())
            .addPathPatterns("/**")
//            .excludePathPatterns("/user/auth", "/article/auth");
            .excludePathPatterns("/user/auth/**");
//            .excludePathPatterns("/user/auth/**", "/article/auth/**")
//            .excludePathPatterns("/auth")
//            .excludePathPatterns("/auth/**")
//            .excludePathPatterns("/**/auth")
//            .excludePathPatterns("/**/auth/**");
    }

}
