package com.fr.controller;

import com.fr.javaBean.Cart;
import com.fr.javaBean.Goods;
import com.fr.javaBean.Log;
import com.fr.javaBean.User;
import com.fr.service.CartService;
import com.fr.service.GoodsService;
import com.fr.service.LogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    GoodsService goodsService;
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

    @RequestMapping("/addToCart")
    public String addToCart(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception{
        String id = request.getParameter("id");
        String from = request.getParameter("from");
        String msg = "";
        
        if (id != null && !"".equals(id.trim())) {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                return "redirect:/login";
            }
            
            QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
            goodsQueryWrapper.eq("id", id);
            List<Goods> goodsList = goodsService.findGoods(goodsQueryWrapper);
            
            if (goodsList != null && !goodsList.isEmpty()) {
                Goods goods = goodsList.get(0);
                
                if (goods.getStock() <= 0) {
                    msg = "该商品库存不足！";
                } else {
                    QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
                    cartQueryWrapper.eq("good_id", id);
                    cartQueryWrapper.eq("user_name", user.getUsername());
                    List<Cart> existingCart = cartService.findAll(cartQueryWrapper);
                    
                    Cart cart;
                    if (existingCart != null && !existingCart.isEmpty()) {
                        cart = existingCart.get(0);
                        cart.setAmount(cart.getAmount() + 1);
                        cart.setTotal_price(cart.getPrice() * cart.getAmount());
                        cartService.update(cart);
                    } else {
                        cart = new Cart();
                        cart.setGood_id(id);
                        cart.setUser_name(user.getUsername());
                        cart.setIntro(goods.getIntro());
                        cart.setAmount(1);
                        cart.setPrice(Float.parseFloat(goods.getPrice()));
                        cart.setTotal_price(Float.parseFloat(goods.getPrice()));
                        cart.setCover(goods.getCover());
                        cartService.save(cart);
                    }
                    
                    goods.setStock(goods.getStock() - 1);
                    goodsService.updateGoods(goods);
                    
                    addLog(request, "添加购物车", "购物车", "添加商品到购物车: " + goods.getName());
                    msg = "添加购物车成功！";
                }
            }
        }
        
        redirectAttributes.addFlashAttribute("msg", msg);

        if ("detail".equals(from)) {
            return "redirect:/goods/goodsList?id=" + id;
        } else if ("newProducts".equals(from)) {
            return "redirect:/goods/newProducts";
        } else if ("hotSales".equals(from)) {
            return "redirect:/goods/hotSales";
        } else {
            return "redirect:/goods/goodsList";
        }
    }

    @RequestMapping("/myCart")
    public ModelAndView myCart(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        
        List<Cart> cartList = cartService.findCartWithGoodsName(user.getUsername());
        
        float totalAmount = 0;
        int totalCount = 0;
        for (Cart cart : cartList) {
            float calculatedTotal = cart.getPrice() * cart.getAmount();
            cart.setTotal_price(calculatedTotal);
            totalAmount += calculatedTotal;
            totalCount += cart.getAmount();
        }
        
        modelAndView.addObject("cartList", cartList);
        modelAndView.addObject("totalAmount", totalAmount);
        modelAndView.addObject("totalCount", totalCount);
        modelAndView.setViewName("cart");
        
        return modelAndView;
    }
    
    @RequestMapping("/updateAmount")
    public String updateAmount(HttpServletRequest request) throws Exception{
        String cartId = request.getParameter("cartId");
        String action = request.getParameter("action");
        
        if (cartId != null && !"".equals(cartId.trim())) {
            Cart cart = cartService.getById(Integer.parseInt(cartId));
            if (cart != null) {
                if ("increase".equals(action)) {
                    cart.setAmount(cart.getAmount() + 1);
                    cart.setTotal_price(cart.getPrice() * cart.getAmount());
                    cartService.update(cart);
                } else if ("decrease".equals(action) && cart.getAmount() > 1) {
                    cart.setAmount(cart.getAmount() - 1);
                    cart.setTotal_price(cart.getPrice() * cart.getAmount());
                    cartService.update(cart);
                }
            }
        }
        
        return "redirect:/cart/myCart";
    }
    
    @RequestMapping("/deleteCart")
    public String deleteCart(HttpServletRequest request) throws Exception{
        String cartId = request.getParameter("cartId");
        
        if (cartId != null && !"".equals(cartId.trim())) {
            addLog(request, "删除购物车", "购物车", "删除购物车项ID: " + cartId);
            cartService.deleteById(Integer.parseInt(cartId));
        }
        
        return "redirect:/cart/myCart";
    }
}
