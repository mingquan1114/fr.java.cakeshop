package com.fr.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fr.javaBean.User;
import com.fr.service.UserService;
//@Controller代表是一个bean，将被解析成
@Controller
@RequestMapping("/user")
public class UserController {
	
	//控制层是调用服务层
	@Autowired
	UserService userService;
	
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();

		HttpSession session = request.getSession(true);
		System.out.println(session);

		String username = request.getParameter("username");
		String paswd = request.getParameter("password");

		if (username != null && paswd != null && !"".equals(username.trim()) && !"".equals(paswd.trim())) {

			QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
			userQueryWrapper.eq("username", username);
			userQueryWrapper.eq("password", paswd);


			List<User> users = userService.getUsers(userQueryWrapper);
			if (users == null || users.size() == 0) {
				session.invalidate();
				modelAndView.addObject("msg","用户名或密码错误！");
				modelAndView.setViewName("login");

			}else{
				session.setAttribute("user",users.get(0));
				modelAndView.addObject("user",users.get(0));

				User user = users.get(0);
				if ("1".equals(user.getIsadmin()) || "1".equals(String.valueOf(user.getIsadmin()))){
					modelAndView.setViewName("redirect:/welcome.html?admin=1");
				}else{
					modelAndView.setViewName("redirect:/welcome.html?admin=0");
				}
			}
		}
			return modelAndView;
		}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView showRegister() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("register");
		return modelAndView;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView registerUser(@Valid HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String username = request.getParameter("username");
		String email = request.getParameter("mail");
		String password = request.getParameter("password");
		String name = request.getParameter("receiveName");
		String phone = request.getParameter("receivePhone");
		String address = request.getParameter("receiveAddress");
		int succ = 0;

		User user = new User(0, username, email, password, name, phone, address, "0", "0");
		int result = userService.addUser(user);

		if (result > 0) {
			modelAndView.addObject("msg2","注册成功！");
			succ = 1;
			modelAndView.addObject("succ",succ);
			modelAndView.setViewName("login");
		}else{
			modelAndView.addObject("msg2","系统繁忙，请稍后再试！");
			modelAndView.addObject("succ",succ);
			modelAndView.setViewName("register");
		}

		return modelAndView;
	}
	
	@RequestMapping("/loginout")
	public String loginout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/login";
	}

}
