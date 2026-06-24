package com.fr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.*;
import com.fr.service.CartService;
import com.fr.service.LogService;
import com.fr.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    CartService cartService;
    @Autowired
    OrderService orderService;
    @Autowired
    LogService logService;

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

    @RequestMapping("/checkout")
    public ModelAndView checkout(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        String cartIds = request.getParameter("cartIds");
        if (cartIds == null || cartIds.isEmpty()) {
            modelAndView.setViewName("redirect:/cart/myCart");
            return modelAndView;
        }

        String[] ids = cartIds.split(",");
        List<Cart> cartList = new ArrayList<>();
        float totalAmount = 0;
        int totalCount = 0;

        for (String id : ids) {
            Cart cart = cartService.getById(Integer.parseInt(id.trim()));
            if (cart != null) {
                cartList.add(cart);
                totalAmount += cart.getPrice() * cart.getAmount();
                totalCount += cart.getAmount();
            }
        }

        PaymentData paymentData = new PaymentData();
        paymentData.setCartIds(cartIds);
        paymentData.setUserId(user.getId());
        paymentData.setUserName(user.getUsername());
        paymentData.setReceiverName(user.getName());
        paymentData.setReceiverPhone(user.getPhone());
        paymentData.setReceiverAddress(user.getAddress());
        paymentData.setTotalAmount(totalAmount);
        paymentData.setTotalCount(totalCount);

        request.getSession().setAttribute("paymentData", paymentData);

        modelAndView.addObject("user", user);
        modelAndView.addObject("cartList", cartList);
        modelAndView.addObject("totalAmount", totalAmount);
        modelAndView.addObject("totalCount", totalCount);
        modelAndView.addObject("cartIds", cartIds);
        modelAndView.setViewName("payment");
        return modelAndView;
    }

    @RequestMapping("/submit")
    public ModelAndView submitOrder(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        String receiverName = request.getParameter("receiverName");
        String receiverPhone = request.getParameter("receiverPhone");
        String receiverAddress = request.getParameter("receiverAddress");
        String payMethod = request.getParameter("payMethod");
        String cartIds = request.getParameter("cartIds");

        if (receiverName == null || receiverName.isEmpty() ||
                receiverPhone == null || receiverPhone.isEmpty() ||
                receiverAddress == null || receiverAddress.isEmpty()) {
            modelAndView.addObject("error", "请填写完整的收货信息");
            modelAndView.setViewName("payment");
            return modelAndView;
        }

        PaymentData paymentData = new PaymentData();
        paymentData.setCartIds(cartIds);
        paymentData.setUserId(user.getId());
        paymentData.setUserName(user.getUsername());
        paymentData.setReceiverName(receiverName);
        paymentData.setReceiverPhone(receiverPhone);
        paymentData.setReceiverAddress(receiverAddress);
        paymentData.setPayMethod(payMethod);

        float totalAmount = 0;
        int totalCount = 0;
        List<Cart> cartList = new ArrayList<>();
        String[] ids = cartIds.split(",");
        for (String id : ids) {
            Cart cart = cartService.getById(Integer.parseInt(id.trim()));
            if (cart != null) {
                cartList.add(cart);
                totalAmount += cart.getPrice() * cart.getAmount();
                totalCount += cart.getAmount();
            }
        }

        paymentData.setTotalAmount(totalAmount);
        paymentData.setTotalCount(totalCount);
        paymentData.setCartList(cartList);

        request.getSession().setAttribute("paymentData", paymentData);

        modelAndView.addObject("totalAmount", totalAmount);
        modelAndView.addObject("payMethod", payMethod);
        modelAndView.setViewName("payConfirm");
        return modelAndView;
    }

    @RequestMapping("/confirm")
    public ModelAndView confirmPayment(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        PaymentData paymentData = (PaymentData) session.getAttribute("paymentData");

        if (paymentData == null) {
            modelAndView.setViewName("redirect:/cart/myCart");
            return modelAndView;
        }

        Boolean isSubmitted = (Boolean) session.getAttribute("paymentSubmitted");
        if (isSubmitted != null && isSubmitted) {
            modelAndView.addObject("orderNo", session.getAttribute("lastOrderNo"));
            modelAndView.addObject("totalAmount", paymentData.getTotalAmount());
            modelAndView.setViewName("paymentSuccess");
            return modelAndView;
        }

        session.setAttribute("paymentSubmitted", true);
        
        String orderNo = generateOrderNo();
        session.setAttribute("lastOrderNo", orderNo);

        Order order = new Order();
        order.setId(orderNo);
        order.setUser_id(paymentData.getUserId());
        order.setTotal(paymentData.getTotalAmount());
        order.setAmount(paymentData.getTotalCount());
        order.setName(paymentData.getReceiverName());
        order.setPhone(paymentData.getReceiverPhone());
        order.setAddress(paymentData.getReceiverAddress());
        order.setPaytype("alipay".equals(paymentData.getPayMethod()) ? 2 : 1);
        order.setStatus(2);
        order.setDatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        orderService.saveOrder(order);

        List<OrderItem> orderItems = new ArrayList<>();
        String[] cartIds = paymentData.getCartIds().split(",");
        System.out.println("Processing " + cartIds.length + " cart items");
        
        for (String cartId : cartIds) {
            int id = Integer.parseInt(cartId.trim());
            Cart cart = cartService.getById(id);
            System.out.println("Cart id=" + id + ", cart=" + cart);
            
            if (cart == null) {
                System.out.println("Cart is null, skipping");
                continue;
            }
            
            String goodId = cart.getGood_id();
            System.out.println("goodId=" + goodId);
            
            if (goodId == null || goodId.isEmpty()) {
                System.out.println("goodId is null or empty, skipping");
                continue;
            }
            
            OrderItem item = new OrderItem();
            item.setOrderId(orderNo);
            int goodsIdInt = Integer.parseInt(goodId);
            item.setGoodsId(goodsIdInt);
            item.setPrice(cart.getPrice());
            item.setAmount(cart.getAmount());
            orderItems.add(item);
            System.out.println("Added OrderItem: order_id=" + orderNo + ", goods_id=" + goodsIdInt + ", item.getGoodsId()=" + item.getGoodsId() + ", price=" + cart.getPrice() + ", amount=" + cart.getAmount());
        }

        System.out.println("Total orderItems to save: " + orderItems.size());
        if (!orderItems.isEmpty()) {
            try {
                orderService.saveOrderItems(orderItems);
                System.out.println("OrderItems saved successfully");
                
                QueryWrapper<OrderItem> query = new QueryWrapper<>();
                query.eq("order_id", orderNo);
                List<OrderItem> savedItems = orderService.findOrderItems(query);
                System.out.println("Actually saved orderItems count: " + savedItems.size());
                
                for (OrderItem item : savedItems) {
                    System.out.println("Actually saved OrderItem: id=" + item.getId() + ", order_id=" + item.getOrderId() + ", goods_id=" + item.getGoodsId());
                }
            } catch (Exception e) {
                System.out.println("Error saving orderItems: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No orderItems to save");
        }

        for (String id : cartIds) {
            cartService.deleteById(Integer.parseInt(id.trim()));
        }

        session.removeAttribute("paymentData");
        session.removeAttribute("paymentSubmitted");
        session.removeAttribute("lastOrderNo");

        addLog(request, "创建订单", "订单", "创建订单: " + orderNo);

        modelAndView.addObject("orderNo", orderNo);
        modelAndView.addObject("totalAmount", paymentData.getTotalAmount());
        modelAndView.setViewName("paymentSuccess");
        return modelAndView;
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    @RequestMapping("/backToHome")
    public String backToHome() {
        return "redirect:/goods/goodsList";
    }

    public static class PaymentData {
        private String cartIds;
        private int userId;
        private String userName;
        private String receiverName;
        private String receiverPhone;
        private String receiverAddress;
        private String payMethod;
        private float totalAmount;
        private int totalCount;
        private List<Cart> cartList;

        public String getCartIds() { return cartIds; }
        public void setCartIds(String cartIds) { this.cartIds = cartIds; }
        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getReceiverName() { return receiverName; }
        public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
        public String getReceiverPhone() { return receiverPhone; }
        public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
        public String getReceiverAddress() { return receiverAddress; }
        public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }
        public String getPayMethod() { return payMethod; }
        public void setPayMethod(String payMethod) { this.payMethod = payMethod; }
        public float getTotalAmount() { return totalAmount; }
        public void setTotalAmount(float totalAmount) { this.totalAmount = totalAmount; }
        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
        public List<Cart> getCartList() { return cartList; }
        public void setCartList(List<Cart> cartList) { this.cartList = cartList; }
    }
}