package com.fr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.*;
import com.fr.service.CartService;
import com.fr.service.GoodsService;
import com.fr.service.LogService;
import com.fr.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private LogService logService;

    private void addLog(HttpServletRequest request, String operation, String module, String details) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            String isAdmin = ("1".equals(user.getIsadmin()) || "1".equals(String.valueOf(user.getIsadmin()))) ? "1" : "0";
            Log log = new Log(isAdmin, user.getUsername(), operation, module, details);
            try {
                logService.saveLog(log);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final int PAGE_SIZE = 5;

    @RequestMapping("/orderList")
    public ModelAndView orderList(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        int currentPage = 0;
        String pageStr = request.getParameter("currentPage");
        if (pageStr != null && !pageStr.isEmpty()) {
            currentPage = Integer.parseInt(pageStr);
        }

        String searchKeyword = request.getParameter("searchKeyword");

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like("id", searchKeyword).or().like("name", searchKeyword));
            modelAndView.addObject("searchKeyword", searchKeyword);
        }
        
        queryWrapper.orderByDesc("datetime");

        List<Order> allOrders = orderService.findAll(queryWrapper);
        int totalCount = allOrders.size();
        int pageCount = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        int fromIndex = currentPage * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalCount);
        List<Order> orderList = allOrders.subList(fromIndex, toIndex);

        modelAndView.addObject("orderList", orderList);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalCount", totalCount);
        modelAndView.addObject("pageCount", pageCount);

        modelAndView.setViewName("myOrder");
        return modelAndView;
    }

    @RequestMapping("/orderDetail")
    public ModelAndView orderDetail(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        String orderId = request.getParameter("id");
        if (orderId == null || orderId.isEmpty()) {
            modelAndView.setViewName("redirect:/order/orderList");
            return modelAndView;
        }

        QueryWrapper<Order> orderQuery = new QueryWrapper<>();
        orderQuery.eq("id", orderId);
        orderQuery.eq("user_id", user.getId());
        List<Order> orders = orderService.findAll(orderQuery);

        if (orders.isEmpty()) {
            modelAndView.setViewName("redirect:/order/orderList");
            return modelAndView;
        }

        Order order = orders.get(0);
        
        System.out.println("OrderController.orderDetail: order=" + order);
        System.out.println("OrderController.orderDetail: order.name=" + order.getName());
        System.out.println("OrderController.orderDetail: order.phone=" + order.getPhone());
        System.out.println("OrderController.orderDetail: order.address=" + order.getAddress());
        System.out.println("OrderController.orderDetail: order.total=" + order.getTotal());
        System.out.println("OrderController.orderDetail: order.status=" + order.getStatus());

        String actualOrderId = order.getId() != null ? order.getId().trim() : "";
        System.out.println("OrderController.orderDetail: orderId from request=" + orderId + ", actualOrderId=" + actualOrderId);
        
        List<OrderDetailDTO> orderDetails = orderService.findOrderDetails(actualOrderId);
        System.out.println("OrderController.orderDetail: orderDetails size=" + orderDetails.size());
        
        for (OrderDetailDTO detail : orderDetails) {
            System.out.println("OrderController.orderDetail: detail - goodsId=" + detail.getGoodsId() + ", goodsName=" + detail.getGoodsName() + 
                    ", goodsCover=" + detail.getGoodsCover() + ", goodsIntro=" + detail.getGoodsIntro() + ", amount=" + detail.getAmount() + ", price=" + detail.getPrice());
        }

        modelAndView.addObject("order", order);
        modelAndView.addObject("orderDetails", orderDetails);
        modelAndView.setViewName("orderDetail");
        return modelAndView;
    }

    @RequestMapping("/cancelOrder")
    public ModelAndView cancelOrder(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        String orderId = request.getParameter("id");
        if (orderId == null || orderId.isEmpty()) {
            modelAndView.setViewName("redirect:/order/orderList");
            return modelAndView;
        }

        QueryWrapper<Order> orderQuery = new QueryWrapper<>();
        orderQuery.eq("id", orderId);
        orderQuery.eq("user_id", user.getId());
        List<Order> orders = orderService.findAll(orderQuery);

        if (orders.isEmpty()) {
            modelAndView.setViewName("redirect:/order/orderList");
            return modelAndView;
        }

        Order order = orders.get(0);
        if (order.getStatus() != 2) {
            modelAndView.setViewName("redirect:/order/orderList");
            return modelAndView;
        }

        String actualOrderId = order.getId() != null ? order.getId().trim() : "";
        System.out.println("OrderController.cancelOrder: actualOrderId from database=" + actualOrderId + ", length=" + actualOrderId.length());
        
        QueryWrapper<OrderItem> itemQuery = new QueryWrapper<>();
        itemQuery.like("order_id", actualOrderId);
        List<OrderItem> orderItems = orderService.findOrderItems(itemQuery);

        System.out.println("OrderController.cancelOrder: orderId=" + orderId + ", actualOrderId=" + actualOrderId + ", orderItems size=" + orderItems.size());
        
        if (orderItems.isEmpty()) {
            System.out.println("OrderController.cancelOrder: Trying exact match with trimmed ID");
            itemQuery = new QueryWrapper<>();
            itemQuery.eq("order_id", actualOrderId);
            orderItems = orderService.findOrderItems(itemQuery);
            System.out.println("OrderController.cancelOrder: after exact match, orderItems size=" + orderItems.size());
        }
        
        for (OrderItem item : orderItems) {
            System.out.println("OrderController.cancelOrder: processing item, goods_id=" + item.getGoodsId() + ", amount=" + item.getAmount());
            
            if (item == null || item.getGoodsId() == null) {
                System.out.println("OrderController.cancelOrder: item or goods_id is null, skipping");
                continue;
            }
            
            Goods goods = goodsService.getById(item.getGoodsId());
            System.out.println("OrderController.cancelOrder: goods=" + goods);
            
            if (goods == null) {
                System.out.println("OrderController.cancelOrder: goods is null, skipping");
                continue;
            }
            
            System.out.println("OrderController.cancelOrder: goods.id=" + goods.getId() + ", goods.name=" + goods.getName() + ", goods.price=" + goods.getPrice());
            
            Cart cart = new Cart();
            cart.setGood_id(String.valueOf(goods.getId()));
            cart.setUser_name(user.getUsername());
            cart.setName(goods.getName());
            cart.setIntro(goods.getIntro());
            cart.setAmount(item.getAmount());
            
            float price = 0;
            if (goods.getPrice() != null && !goods.getPrice().isEmpty()) {
                try {
                    price = Float.parseFloat(goods.getPrice());
                } catch (Exception e) {
                    System.out.println("OrderController.cancelOrder: Error parsing price: " + e.getMessage());
                }
            }
            cart.setPrice(price);
            cart.setTotal_price(cart.getPrice() * cart.getAmount());
            cart.setCover(goods.getCover());
            
            System.out.println("OrderController.cancelOrder: creating cart - good_id=" + cart.getGood_id() + ", user_name=" + cart.getUser_name() + ", amount=" + cart.getAmount() + ", price=" + cart.getPrice());
            
            QueryWrapper<Cart> cartQuery = new QueryWrapper<>();
            cartQuery.eq("good_id", goods.getId());
            cartQuery.eq("user_name", user.getUsername());
            List<Cart> existingCarts = cartService.findAll(cartQuery);
            
            System.out.println("OrderController.cancelOrder: existingCarts size=" + existingCarts.size());
            
            if (!existingCarts.isEmpty()) {
                Cart existingCart = existingCarts.get(0);
                existingCart.setAmount(existingCart.getAmount() + item.getAmount());
                existingCart.setTotal_price(existingCart.getPrice() * existingCart.getAmount());
                cartService.update(existingCart);
                System.out.println("OrderController.cancelOrder: updated existing cart, cart id=" + existingCart.getId());
                
                QueryWrapper<Cart> verifyQuery = new QueryWrapper<>();
                verifyQuery.eq("id", existingCart.getId());
                List<Cart> verifyCarts = cartService.findAll(verifyQuery);
                System.out.println("OrderController.cancelOrder: verified updated cart size=" + verifyCarts.size());
            } else {
                cartService.save(cart);
                System.out.println("OrderController.cancelOrder: saved new cart, cart.id after save=" + cart.getId());
                
                QueryWrapper<Cart> verifyQuery = new QueryWrapper<>();
                verifyQuery.eq("good_id", goods.getId());
                verifyQuery.eq("user_name", user.getUsername());
                List<Cart> verifyCarts = cartService.findAll(verifyQuery);
                System.out.println("OrderController.cancelOrder: verified new cart size=" + verifyCarts.size());
                if (!verifyCarts.isEmpty()) {
                    System.out.println("OrderController.cancelOrder: verified cart - id=" + verifyCarts.get(0).getId() + ", amount=" + verifyCarts.get(0).getAmount());
                }
            }
        }

        orderService.deleteOrderItems(actualOrderId);
        orderService.deleteOrder(actualOrderId);

        addLog(request, "取消订单", "订单", "取消订单: " + orderId);

        modelAndView.setViewName("redirect:/order/orderList");
        return modelAndView;
    }

    @RequestMapping("/orderUpdate")
    public ModelAndView orderUpdate(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        String orderId = request.getParameter("id");
        String statusStr = request.getParameter("status");

        if (orderId == null || orderId.isEmpty() || statusStr == null || statusStr.isEmpty()) {
            modelAndView.setViewName("redirect:/order/orderList");
            return modelAndView;
        }

        int status = Integer.parseInt(statusStr);

        QueryWrapper<Order> orderQuery = new QueryWrapper<>();
        orderQuery.eq("id", orderId);
        orderQuery.eq("user_id", user.getId());
        List<Order> orders = orderService.findAll(orderQuery);

        if (orders.isEmpty()) {
            modelAndView.setViewName("redirect:/order/orderList");
            return modelAndView;
        }

        Order order = orders.get(0);
        
        if (status == 4 && order.getStatus() == 3) {
            order.setStatus(4);
            orderService.updateOrder(order);
        }

        modelAndView.setViewName("redirect:/order/orderList");
        return modelAndView;
    }
}