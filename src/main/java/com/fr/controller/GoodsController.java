package com.fr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Cart;
import com.fr.javaBean.Goods;
import com.fr.javaBean.SalesDTO;
import com.fr.javaBean.Type;
import com.fr.javaBean.User;
import com.fr.service.CartService;
import com.fr.service.GoodsService;
import com.fr.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    TypeService typeService;
    @Autowired
    CartService cartService;

    @RequestMapping("/goodsList")
    public ModelAndView goodsList(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        String typeid = request.getParameter("typeid");
        String name = request.getParameter("name");
        String id = request.getParameter("id");

        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();

        if (typeid!=null && !"".equals(typeid.trim())){
            goodsQueryWrapper.eq("type_id",typeid);
        }
        if (name!=null && !"".equals(name.trim())){
            goodsQueryWrapper.like("name", name);
        }
        if (id!=null && !"".equals(id.trim())){
            goodsQueryWrapper.eq("id",id);
        }
        List<Goods> goodsList = goodsService.findGoods(goodsQueryWrapper);
        List<Type> typeList = typeService.listType(null);
        User users = (User) request.getSession().getAttribute("user");
        cartQueryWrapper.eq("user_name",users.getUsername());
        long count = cartService.find(cartQueryWrapper);

        modelAndView.addObject("count", count);
        modelAndView.addObject("goodsList", goodsList);
        modelAndView.addObject("typeList", typeList);

        if (id!=null && !"".equals(id.trim()) && goodsList != null && !goodsList.isEmpty()){
            Goods goods = goodsList.get(0);
            String goodsType = "未知分类";
            QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
            typeQueryWrapper.eq("id", goods.getType_id());
            List<Type> matchedTypes = typeService.listType(typeQueryWrapper);
            if (matchedTypes != null && !matchedTypes.isEmpty()) {
                goodsType = matchedTypes.get(0).getName();
            }
            modelAndView.addObject("goodsType", goodsType);
           modelAndView.setViewName("admin/goodsDetail");
        }else{
            modelAndView.setViewName("index");
        }

        return modelAndView;
    }

    @RequestMapping("/hotSales")
    public ModelAndView hotSales(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        
        List<SalesDTO> hotSalesList = goodsService.findHotSales();
        List<Type> typeList = typeService.listType(null);
        
        User user = (User) request.getSession().getAttribute("user");
        long count = 0;
        if (user != null) {
            QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
            cartQueryWrapper.eq("user_name", user.getUsername());
            count = cartService.find(cartQueryWrapper);
        }
        
        modelAndView.addObject("count", count);
        modelAndView.addObject("hotSalesList", hotSalesList);
        modelAndView.addObject("typeList", typeList);
        modelAndView.setViewName("hotSales");
        
        return modelAndView;
    }

    @RequestMapping("/newProducts")
    public ModelAndView newProducts(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        
        List<SalesDTO> newProductsList = goodsService.findNewProducts();
        List<Type> typeList = typeService.listType(null);
        
        User user = (User) request.getSession().getAttribute("user");
        long count = 0;
        if (user != null) {
            QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
            cartQueryWrapper.eq("user_name", user.getUsername());
            count = cartService.find(cartQueryWrapper);
        }
        
        modelAndView.addObject("count", count);
        modelAndView.addObject("newProductsList", newProductsList);
        modelAndView.addObject("typeList", typeList);
        modelAndView.setViewName("newProducts");
        
        return modelAndView;
    }

    @RequestMapping("/adminGoodsList")
    public String adminGoodsList() {
        return "redirect:/admin/index";
    }

}
