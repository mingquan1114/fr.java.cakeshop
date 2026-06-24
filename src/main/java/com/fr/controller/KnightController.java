package com.fr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.*;
import com.fr.service.KnightService;
import com.fr.service.LogService;
import com.fr.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/knight")
public class KnightController {

    @Autowired
    private KnightService knightService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LogService logService;

    private static final int PAGE_SIZE = 10;

    private void saveKnightLog(Knight knight, String operation, String details) {
        Log log = new Log("rider", knight.getName(), operation, "骑士端", details);
        logService.saveLog(log);
    }

    // ========== 页面跳转 ==========

    @RequestMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("knight/login");
    }

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight == null) {
            modelAndView.setViewName("redirect:/knight/login");
            return modelAndView;
        }

        // 统计各状态订单数量
        QueryWrapper<Order> pendingQuery = new QueryWrapper<>();
        pendingQuery.eq("status", 2);
        int pendingCount = orderService.findAll(pendingQuery).size();

        QueryWrapper<Order> pickupQuery = new QueryWrapper<>();
        pickupQuery.eq("status", 3);
        pickupQuery.eq("knight_id", knight.getId());
        int pickupCount = orderService.findAll(pickupQuery).size();

        QueryWrapper<Order> deliveringQuery = new QueryWrapper<>();
        deliveringQuery.eq("status", 4);
        deliveringQuery.eq("knight_id", knight.getId());
        int deliveringCount = orderService.findAll(deliveringQuery).size();

        QueryWrapper<Order> completedQuery = new QueryWrapper<>();
        completedQuery.eq("status", 5);
        completedQuery.eq("knight_id", knight.getId());
        int completedCount = orderService.findAll(completedQuery).size();

        // 默认显示待接单列表
        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "pending";
        }

        QueryWrapper<Order> orderQuery = new QueryWrapper<>();
        if ("pending".equals(tab)) {
            orderQuery.eq("status", 2);
        } else if ("pickup".equals(tab)) {
            orderQuery.eq("status", 3);
            orderQuery.eq("knight_id", knight.getId());
        } else if ("delivering".equals(tab)) {
            orderQuery.eq("status", 4);
            orderQuery.eq("knight_id", knight.getId());
        } else if ("completed".equals(tab)) {
            orderQuery.eq("status", 5);
            orderQuery.eq("knight_id", knight.getId());
        }
        orderQuery.orderByDesc("datetime");

        List<Order> allOrders = orderService.findAll(orderQuery);
        int totalCount = allOrders.size();
        int pageCount = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        int currentPage = 0;
        String pageStr = request.getParameter("currentPage");
        if (pageStr != null && !pageStr.isEmpty()) {
            currentPage = Integer.parseInt(pageStr);
        }

        int fromIndex = currentPage * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalCount);
        List<Order> orderList = allOrders.subList(fromIndex, toIndex);

        for (Order order : orderList) {
            order.setDeliveryFee(order.calculateDeliveryFee());
        }

        modelAndView.addObject("knight", knight);
        modelAndView.addObject("pendingCount", pendingCount);
        modelAndView.addObject("pickupCount", pickupCount);
        modelAndView.addObject("deliveringCount", deliveringCount);
        modelAndView.addObject("completedCount", completedCount);
        modelAndView.addObject("orderList", orderList);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalCount", totalCount);
        modelAndView.addObject("pageCount", pageCount);
        modelAndView.addObject("currentTab", tab);

        modelAndView.setViewName("knight/index");
        return modelAndView;
    }

    @RequestMapping("/orderDetail")
    public ModelAndView orderDetail(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight == null) {
            modelAndView.setViewName("redirect:/knight/login");
            return modelAndView;
        }

        String orderId = request.getParameter("id");
        if (orderId == null || orderId.isEmpty()) {
            modelAndView.setViewName("redirect:/knight/index");
            return modelAndView;
        }

        QueryWrapper<Order> orderQuery = new QueryWrapper<>();
        orderQuery.eq("id", orderId);
        List<Order> orders = orderService.findAll(orderQuery);
        if (orders.isEmpty()) {
            modelAndView.setViewName("redirect:/knight/index");
            return modelAndView;
        }

        Order order = orders.get(0);
        List<OrderDetailDTO> orderDetails = orderService.findOrderDetails(orderId);

        modelAndView.addObject("order", order);
        modelAndView.addObject("orderDetails", orderDetails);
        modelAndView.setViewName("knight/orderDetail");
        return modelAndView;
    }

    @RequestMapping("/income")
    public ModelAndView income(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight == null) {
            modelAndView.setViewName("redirect:/knight/login");
            return modelAndView;
        }

        // 统计收入
        QueryWrapper<Order> todayQuery = new QueryWrapper<>();
        todayQuery.eq("knight_id", knight.getId());
        todayQuery.eq("status", 5);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        todayQuery.like("datetime", today);
        List<Order> todayOrders = orderService.findAll(todayQuery);
        for (Order order : todayOrders) {
            order.setDeliveryFee(order.calculateDeliveryFee());
        }
        double todayIncome = todayOrders.stream().mapToDouble(Order::getDeliveryFee).sum();

        QueryWrapper<Order> allCompletedQuery = new QueryWrapper<>();
        allCompletedQuery.eq("knight_id", knight.getId());
        allCompletedQuery.eq("status", 5);
        allCompletedQuery.orderByDesc("datetime");
        List<Order> completedOrders = orderService.findAll(allCompletedQuery);
        for (Order order : completedOrders) {
            order.setDeliveryFee(order.calculateDeliveryFee());
        }
        double totalIncome = completedOrders.stream().mapToDouble(Order::getDeliveryFee).sum();

        // 本周收入
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String weekStart = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        double weekIncome = completedOrders.stream()
                .filter(o -> o.getDatetime() != null && o.getDatetime().compareTo(weekStart) >= 0)
                .mapToDouble(Order::getDeliveryFee).sum();

        // 本月收入
        String monthStart = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01";
        double monthIncome = completedOrders.stream()
                .filter(o -> o.getDatetime() != null && o.getDatetime().compareTo(monthStart) >= 0)
                .mapToDouble(Order::getDeliveryFee).sum();

        int orderCount = completedOrders.size();
        int avgOrder = orderCount > 0 ? orderCount / 30 : 0;
        double avgIncome = orderCount > 0 ? totalIncome / orderCount : 0;

        modelAndView.addObject("todayIncome", todayIncome);
        modelAndView.addObject("weekIncome", weekIncome);
        modelAndView.addObject("monthIncome", monthIncome);
        modelAndView.addObject("totalIncome", totalIncome);
        modelAndView.addObject("orderCount", orderCount);
        modelAndView.addObject("avgOrder", avgOrder);
        modelAndView.addObject("avgIncome", String.format("%.2f", avgIncome));
        modelAndView.addObject("completedOrders", completedOrders);
        modelAndView.setViewName("knight/income");
        return modelAndView;
    }

    @RequestMapping("/message")
    public ModelAndView message(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight == null) {
            modelAndView.setViewName("redirect:/knight/login");
            return modelAndView;
        }
        modelAndView.setViewName("knight/message");
        return modelAndView;
    }

    @RequestMapping("/profile")
    public ModelAndView profile(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight == null) {
            modelAndView.setViewName("redirect:/knight/login");
            return modelAndView;
        }
        modelAndView.addObject("knight", knight);
        modelAndView.setViewName("knight/profile");
        return modelAndView;
    }

    // ========== 认证接口 ==========

    @PostMapping("/doLogin")
    public ModelAndView doLogin(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Knight knight = knightService.login(username, password);
        if (knight != null) {
            request.getSession().setAttribute("knight", knight);
            saveKnightLog(knight, "登录", "骑士登录系统，账号: " + username);
            modelAndView.setViewName("redirect:/knight/index");
        } else {
            redirectAttributes.addFlashAttribute("error", "账号或密码错误");
            modelAndView.setViewName("redirect:/knight/login");
        }
        return modelAndView;
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight != null) {
            saveKnightLog(knight, "退出", "骑士退出系统");
        }
        session.removeAttribute("knight");
        return new ModelAndView("redirect:/knight/login");
    }

    // ========== 订单操作接口 ==========

    @RequestMapping("/acceptOrder")
    public ModelAndView acceptOrder(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight == null) {
            modelAndView.setViewName("redirect:/knight/login");
            return modelAndView;
        }

        String orderId = request.getParameter("id");
        if (orderId != null && !orderId.isEmpty()) {
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", orderId);
            queryWrapper.eq("status", 2);
            List<Order> orders = orderService.findAll(queryWrapper);
            if (!orders.isEmpty()) {
                Order order = orders.get(0);
                order.setStatus(3);
                order.setKnightId(knight.getId());
                order.setKnightName(knight.getName());
                order.setKnightPhone(knight.getPhone());
                orderService.updateOrder(order);
                saveKnightLog(knight, "接单", "骑士接单，订单号: " + orderId);
            }
        }
        modelAndView.setViewName("redirect:/knight/index?tab=pending");
        return modelAndView;
    }

    @RequestMapping("/pickupOrder")
    public ModelAndView pickupOrder(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight == null) {
            modelAndView.setViewName("redirect:/knight/login");
            return modelAndView;
        }

        String orderId = request.getParameter("id");
        if (orderId != null && !orderId.isEmpty()) {
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", orderId);
            queryWrapper.eq("knight_id", knight.getId());
            queryWrapper.eq("status", 3);
            List<Order> orders = orderService.findAll(queryWrapper);
            if (!orders.isEmpty()) {
                Order order = orders.get(0);
                order.setStatus(4);
                order.setPickupTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                orderService.updateOrder(order);
                saveKnightLog(knight, "取货", "骑士取货，订单号: " + orderId);
            }
        }
        modelAndView.setViewName("redirect:/knight/index?tab=pickup");
        return modelAndView;
    }

    @RequestMapping("/deliverOrder")
    public ModelAndView deliverOrder(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight == null) {
            modelAndView.setViewName("redirect:/knight/login");
            return modelAndView;
        }

        String orderId = request.getParameter("id");
        if (orderId != null && !orderId.isEmpty()) {
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", orderId);
            queryWrapper.eq("knight_id", knight.getId());
            queryWrapper.eq("status", 4);
            List<Order> orders = orderService.findAll(queryWrapper);
            if (!orders.isEmpty()) {
                Order order = orders.get(0);
                order.setStatus(5);
                order.setDeliveryTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                orderService.updateOrder(order);
                saveKnightLog(knight, "送达", "骑士送达，订单号: " + orderId);
            }
        }
        modelAndView.setViewName("redirect:/knight/index?tab=delivering");
        return modelAndView;
    }

    // ========== 个人中心接口 ==========

    @PostMapping("/updateProfile")
    public ModelAndView updateProfile(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight == null) {
            return new ModelAndView("redirect:/knight/login");
        }
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        if (name != null && !name.isEmpty()) {
            knight.setName(name);
        }
        if (phone != null && !phone.isEmpty()) {
            knight.setPhone(phone);
        }
        knightService.update(knight);
        session.setAttribute("knight", knight);
        saveKnightLog(knight, "修改资料", "骑士修改个人资料");
        return new ModelAndView("redirect:/knight/profile");
    }

    @PostMapping("/updatePassword")
    public ModelAndView updatePassword(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        Knight knight = (Knight) session.getAttribute("knight");
        if (knight == null) {
            return new ModelAndView("redirect:/knight/login");
        }
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        knightService.updatePassword(knight.getId(), oldPassword, newPassword);
        saveKnightLog(knight, "修改密码", "骑士修改密码");
        redirectAttributes.addFlashAttribute("success", "密码修改成功");
        return new ModelAndView("redirect:/knight/profile");
    }
}