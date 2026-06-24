package com.fr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


@Configuration
public class MyMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LocaleChangeInterceptor localeChangeInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/register.html").setViewName("register");
        registry.addViewController("/register").setViewName("register");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(new Interceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/login.html", "/register", "/register.html",
                        "/user/login", "/user/register", "/user/loginout",
                        "/knight/**",
                        "/css/**", "/js/**", "/images/**", "/picture/**", "/fonts/**", "/welcome.html");

        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns();

        registry.addInterceptor(new KnightInterceptor())
                .addPathPatterns("/knight/**")
                .excludePathPatterns("/knight/login", "/knight/doLogin", "/knight/logout");
    }
}