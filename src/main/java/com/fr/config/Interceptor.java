package com.fr.config;

import com.fr.javaBean.User;
import io.micrometer.core.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Interceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        String requestUri = request.getRequestURI();
        
        if (requestUri.startsWith("/knight/")) {
            return true;
        }

        User user = (User) request .getSession().getAttribute("user");
        System.out.println(user);
        if (user!=null){
            System.out.println("请求之前，放行");

            return true;
        }else{
            System.out.println("拦截");
            request.setAttribute("msg","您还未登录");
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charest=UTF-8");
            response.sendRedirect("/login.html");
            return false;
        }

    }
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception{
        System.out.println("请求之后");
    }
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception{
        System.out.println("整个请求结束之后");
    }
}
