package com.fr.config;

import com.fr.javaBean.Knight;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KnightInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Knight knight = (Knight) request.getSession().getAttribute("knight");
        if (knight != null) {
            return true;
        }
        response.sendRedirect("/knight/login");
        return false;
    }
}