package com.fr.controller;

import com.fr.javaBean.Goods;
import com.fr.javaBean.GoodsDTO;
import com.fr.javaBean.Log;
import com.fr.javaBean.Order;
import com.fr.javaBean.OrderDetailDTO;
import com.fr.javaBean.Type;
import com.fr.javaBean.User;
import com.fr.service.GoodsService;
import com.fr.service.LogService;
import com.fr.service.OrderService;
import com.fr.service.TypeService;
import com.fr.service.UserService;
import com.fr.util.LogTranslationUtil;
import com.fr.util.TranslationUtil;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final int PAGE_SIZE = 10;

    @Autowired
    UserService userService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    TypeService typeService;

    @Autowired
    LogService logService;

    @Autowired
    TranslationUtil translationUtil;

    private void addLog(HttpServletRequest request, String operation, String module, String details) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            String isAdmin = ("1".equals(user.getIsadmin()) || "1".equals(String.valueOf(user.getIsadmin()))) ? "1" : "0";
            Log log = new Log(isAdmin, user.getUsername(), operation, module, details);
            try {
                logService.saveLog(log);
                System.out.println("Log saved successfully: " + operation + " - " + details);
            } catch (Exception e) {
                System.out.println("Failed to save log: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("User is null, cannot save log");
        }
    }

    @RequestMapping("/index")
    public ModelAndView adminIndex(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        
        addLog(request, "登录", "系统管理", "管理员登录: " + user.getUsername());

        List<User> allUsers = userService.getUsers(null);
        List<Goods> allGoods = goodsService.findGoods(null);
        List<Order> allOrders = orderService.findAll(null);

        float todayRevenue = 0;
        for (Order order : allOrders) {
            if (order.getStatus() >= 2) {
                todayRevenue += order.getTotal();
            }
        }

        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("userCount", allUsers != null ? allUsers.size() : 0);
        modelAndView.addObject("goodsCount", allGoods != null ? allGoods.size() : 0);
        modelAndView.addObject("orderCount", allOrders != null ? allOrders.size() : 0);
        modelAndView.addObject("todayRevenue", String.format("%.2f", todayRevenue));
        modelAndView.addObject("currentPage", "index");
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }

    @RequestMapping("/userList")
    public ModelAndView userList(HttpServletRequest request,
                                  @RequestParam(defaultValue = "") String keyword) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.and(w -> w.like("username", keyword.trim()).or().like("phone", keyword.trim()));
        }

        List<User> userList = userService.getUsers(queryWrapper);
        
        for (User u : userList) {
            String role = ("1".equals(u.getIsadmin()) || "1".equals(String.valueOf(u.getIsadmin()))) ? "管理员" : "普通用户";
            String status = ("1".equals(u.getIsvalidate()) || "1".equals(String.valueOf(u.getIsvalidate()))) ? "已审核" : "待审核";
            u.setRoleDisplay(translationUtil.translateRole(role));
            u.setStatusDisplay(translationUtil.translateUserStatus(status));
            u.setNameDisplay(translationUtil.translateUserName(u.getUsername()));
            u.setAddressDisplay(translationUtil.translateAddress(u.getAddress()));
        }
        
        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("userList", userList);
        modelAndView.addObject("keyword", keyword);
        modelAndView.addObject("currentPage", "userList");
        modelAndView.setViewName("admin/userList");
        return modelAndView;
    }

    @RequestMapping("/userEdit")
    public ModelAndView userEdit(HttpServletRequest request,
                                  @RequestParam int id) {
        ModelAndView modelAndView = new ModelAndView();
        User adminUser = (User) request.getSession().getAttribute("user");

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        List<User> users = userService.getUsers(queryWrapper);

        modelAndView.addObject("adminName", adminUser.getUsername());
        modelAndView.addObject("currentPage", "userList");
        if (users != null && !users.isEmpty()) {
            modelAndView.addObject("editUser", users.get(0));
        }
        modelAndView.setViewName("admin/userEdit");
        return modelAndView;
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public String updateUser(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String isvalidate = request.getParameter("isvalidate");

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        List<User> users = userService.getUsers(queryWrapper);
        if (users != null && !users.isEmpty()) {
            User user = users.get(0);
            if (name != null) user.setName(name);
            if (email != null) user.setEmail(email);
            if (phone != null) user.setPhone(phone);
            if (address != null) user.setAddress(address);
            if (isvalidate != null) user.setIsvalidate(isvalidate);
            userService.updateUser(user);
            addLog(request, "修改用户", "用户管理", "修改用户: " + user.getUsername());
            redirectAttributes.addFlashAttribute("msg", "用户信息更新成功！");
        }
        return "redirect:/admin/userList";
    }

    @RequestMapping("/validateUser")
    public String validateUser(HttpServletRequest request, @RequestParam int id, RedirectAttributes redirectAttributes) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        List<User> users = userService.getUsers(queryWrapper);
        if (users != null && !users.isEmpty()) {
            User user = users.get(0);
            user.setIsvalidate("1");
            userService.updateUser(user);
            addLog(request, "审核用户", "用户管理", "审核用户: " + user.getUsername());
            redirectAttributes.addFlashAttribute("msg", "用户审核通过！");
        }
        return "redirect:/admin/userList";
    }

    @RequestMapping("/toggleUserRole")
    public String toggleUserRole(HttpServletRequest request, @RequestParam int id, RedirectAttributes redirectAttributes) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        List<User> users = userService.getUsers(queryWrapper);
        if (users != null && !users.isEmpty()) {
            User user = users.get(0);
            String currentRole = user.getIsadmin();
            if ("1".equals(currentRole) || "1".equals(String.valueOf(currentRole))) {
                redirectAttributes.addFlashAttribute("errorMsg", "你暂无权限修改该用户的权限等级");
            } else {
                user.setIsadmin("1");
                userService.updateUser(user);
                addLog(request, "设置管理员", "用户管理", "设置管理员: " + user.getUsername());
                redirectAttributes.addFlashAttribute("msg", "已设为管理员！");
            }
        }
        return "redirect:/admin/userList";
    }

    @RequestMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request, @RequestParam int id, RedirectAttributes redirectAttributes) {
        addLog(request, "删除用户", "用户管理", "删除用户ID: " + id);
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("msg", "用户删除成功！");
        return "redirect:/admin/userList";
    }

    @RequestMapping("/goodsList")
    public ModelAndView goodsList(HttpServletRequest request,
                                   @RequestParam(defaultValue = "") String keyword) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        List<GoodsDTO> goodsList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            goodsList = goodsService.findGoodsWithTypeByName(keyword.trim());
        } else {
            goodsList = goodsService.findGoodsWithType();
        }
        
        for (GoodsDTO goods : goodsList) {
            if (goods.getTypeName() != null) {
                goods.setTypeNameEn(translationUtil.translateCategory(goods.getTypeName()));
            }
            if (goods.getName() != null) {
                goods.setNameEn(translationUtil.translateGoods(goods.getName()));
            }
        }

        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("goodsList", goodsList);
        modelAndView.addObject("keyword", keyword);
        modelAndView.addObject("currentPage", "goodsList");
        modelAndView.setViewName("admin/goodsList");
        return modelAndView;
    }

    @RequestMapping("/addGoods")
    public ModelAndView addGoods(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        List<Type> typeList = typeService.listType(null);
        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("typeList", typeList);
        modelAndView.addObject("currentPage", "addGoods");
        modelAndView.setViewName("admin/addGoods");
        return modelAndView;
    }

    @RequestMapping(value = "/saveGoods", method = RequestMethod.POST)
    public String saveGoods(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        int stock = Integer.parseInt(request.getParameter("stock") != null && !request.getParameter("stock").isEmpty()
                ? request.getParameter("stock") : "100");
        int typeId = Integer.parseInt(request.getParameter("type_id") != null && !request.getParameter("type_id").isEmpty()
                ? request.getParameter("type_id") : "1");
        String intro = request.getParameter("intro");
        String cover = request.getParameter("cover");
        String image1 = request.getParameter("image1");
        String image2 = request.getParameter("image2");

        Goods goods = new Goods();
        goods.setName(name);
        goods.setPrice(price);
        goods.setStock(stock);
        goods.setType_id(typeId);
        goods.setIntro(intro != null ? intro : "");
        goods.setCover(cover != null ? cover : "");
        goods.setImage1(image1 != null ? image1 : "");
        goods.setImage2(image2 != null ? image2 : "");

        goodsService.saveGoods(goods);
        addLog(request, "添加商品", "商品管理", "添加商品: " + name);
        redirectAttributes.addFlashAttribute("msg", "商品添加成功！");
        return "redirect:/admin/goodsList";
    }

    @RequestMapping("/goodsDetail")
    public ModelAndView goodsDetail(HttpServletRequest request, @RequestParam int id) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        GoodsDTO goods = goodsService.getByIdWithType(id);
        
        if (goods != null) {
            if (goods.getName() != null) {
                goods.setNameEn(translationUtil.translateGoods(goods.getName()));
            }
            if (goods.getTypeName() != null) {
                goods.setTypeNameEn(translationUtil.translateCategory(goods.getTypeName()));
            }
        }

        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("goods", goods);
        modelAndView.addObject("currentPage", "goodsDetail");
        modelAndView.setViewName("admin/goodsDetail");
        return modelAndView;
    }

    @RequestMapping("/goodsEdit")
    public ModelAndView goodsEdit(HttpServletRequest request, @RequestParam int id) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        GoodsDTO goods = goodsService.getByIdWithType(id);
        List<Type> typeList = typeService.listType(null);

        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("goods", goods);
        modelAndView.addObject("typeList", typeList);
        modelAndView.addObject("currentTypeId", String.valueOf(goods.getType_id()));
        modelAndView.addObject("currentPage", "goodsList");
        modelAndView.setViewName("admin/goodsEdit");
        return modelAndView;
    }

    @RequestMapping(value = "/updateGoods", method = RequestMethod.POST)
    public String updateGoods(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        int stock = Integer.parseInt(request.getParameter("stock") != null && !request.getParameter("stock").isEmpty()
                ? request.getParameter("stock") : "0");
        int typeId = Integer.parseInt(request.getParameter("type_id") != null && !request.getParameter("type_id").isEmpty()
                ? request.getParameter("type_id") : "1");
        String intro = request.getParameter("intro");
        String cover = request.getParameter("cover");
        String image1 = request.getParameter("image1");
        String image2 = request.getParameter("image2");

        Goods goods = goodsService.getById(id);
        if (goods != null) {
            goods.setName(name);
            goods.setPrice(price);
            goods.setStock(stock);
            goods.setType_id(typeId);
            goods.setIntro(intro != null ? intro : "");
            goods.setCover(cover != null ? cover : "");
            goods.setImage1(image1 != null ? image1 : "");
            goods.setImage2(image2 != null ? image2 : "");
            goodsService.updateGoodsFull(goods);
            addLog(request, "修改商品", "商品管理", "修改商品: " + name);
            redirectAttributes.addFlashAttribute("msg", "商品更新成功！");
        }

        return "redirect:/admin/goodsList";
    }

    @RequestMapping("/deleteGoods")
    public String deleteGoods(HttpServletRequest request, @RequestParam int id, RedirectAttributes redirectAttributes) {
        addLog(request, "删除商品", "商品管理", "删除商品ID: " + id);
        goodsService.deleteGoods(id);
        redirectAttributes.addFlashAttribute("msg", "商品删除成功！");
        return "redirect:/admin/goodsList";
    }

    @RequestMapping("/orderList")
    public ModelAndView orderList(HttpServletRequest request,
                                   @RequestParam(defaultValue = "") String keyword,
                                   @RequestParam(defaultValue = "0") int status,
                                   @RequestParam(defaultValue = "1") int page) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like("id", keyword.trim());
        }
        if (status > 0) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("datetime");

        List<Order> allOrderList = orderService.findAll(queryWrapper);
        int totalOrders = allOrderList != null ? allOrderList.size() : 0;
        int totalPages = (int) Math.ceil((double) totalOrders / PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalOrders);
        List<Order> pagedOrderList = (totalOrders > 0 && fromIndex < totalOrders)
                ? allOrderList.subList(fromIndex, toIndex) : new ArrayList<>();

        for (Order order : pagedOrderList) {
            String statusStr = "";
            if ("2".equals(order.getStatus()) || 2 == order.getStatus()) {
                statusStr = "未发货";
            } else if ("3".equals(order.getStatus()) || 3 == order.getStatus()) {
                statusStr = "已发货";
            } else if ("4".equals(order.getStatus()) || 4 == order.getStatus()) {
                statusStr = "已完成";
            }
            order.setStatusDisplay(translationUtil.translateOrderStatus(statusStr));
            order.setNameDisplay(translationUtil.translateUserName(order.getName()));
            order.setAddressDisplay(translationUtil.translateAddress(order.getAddress()));
        }

        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("orderList", pagedOrderList);
        modelAndView.addObject("keyword", keyword);
        modelAndView.addObject("status", status);
        modelAndView.addObject("currentPage", "orderList");
        modelAndView.addObject("page", page);
        modelAndView.addObject("totalPages", totalPages);
        modelAndView.addObject("totalOrders", totalOrders);
        modelAndView.setViewName("admin/orderList");
        return modelAndView;
    }

    @RequestMapping("/orderDetail")
    public ModelAndView orderDetail(HttpServletRequest request, @RequestParam String id) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        List<Order> orders = orderService.findAll(queryWrapper);

        List<OrderDetailDTO> orderItems = orderService.findOrderDetails(id);

        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("currentPage", "orderList");
        if (orders != null && !orders.isEmpty()) {
            modelAndView.addObject("order", orders.get(0));
        }
        modelAndView.addObject("orderItems", orderItems);
        modelAndView.setViewName("admin/orderDetail");
        return modelAndView;
    }

    @RequestMapping("/shipOrder")
    public String shipOrder(HttpServletRequest request, @RequestParam String orderId,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "") String keyword,
                             @RequestParam(defaultValue = "0") int status,
                             RedirectAttributes redirectAttributes) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);
        List<Order> orders = orderService.findAll(queryWrapper);
        if (orders != null && !orders.isEmpty()) {
            Order order = orders.get(0);
            order.setStatus(3);
            orderService.updateOrder(order);
            addLog(request, "订单发货", "订单管理", "订单发货: " + orderId);
            redirectAttributes.addFlashAttribute("msg", "订单已发货！");
        }
        return "redirect:/admin/orderList?page=" + page + "&keyword=" + keyword + "&status=" + status;
    }

    @RequestMapping("/completeOrder")
    public String completeOrder(HttpServletRequest request, @RequestParam String orderId,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "") String keyword,
                                 @RequestParam(defaultValue = "0") int status,
                                 RedirectAttributes redirectAttributes) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);
        List<Order> orders = orderService.findAll(queryWrapper);
        if (orders != null && !orders.isEmpty()) {
            Order order = orders.get(0);
            order.setStatus(4);
            orderService.updateOrder(order);
            addLog(request, "完成订单", "订单管理", "完成订单: " + orderId);
            redirectAttributes.addFlashAttribute("msg", "订单已完成！");
        }
        return "redirect:/admin/orderList?page=" + page + "&keyword=" + keyword + "&status=" + status;
    }

    @RequestMapping("/typeList")
    public ModelAndView typeList(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        List<Type> typeList = typeService.listType(null);
        Map<Integer, Integer> typeGoodsCount = new HashMap<>();
        if (typeList != null) {
            for (Type t : typeList) {
                typeGoodsCount.put(t.getId(), goodsService.countByTypeId(t.getId()));
                t.setNameEn(translationUtil.translateCategory(t.getName()));
            }
        }

        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("typeList", typeList);
        modelAndView.addObject("typeGoodsCount", typeGoodsCount);
        modelAndView.addObject("currentPage", "typeList");
        modelAndView.setViewName("admin/typeList");
        return modelAndView;
    }

    @RequestMapping(value = "/saveType", method = RequestMethod.POST)
    public String saveType(HttpServletRequest request, @RequestParam String name, RedirectAttributes redirectAttributes) {
        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        List<Type> existing = typeService.listType(queryWrapper);
        if (existing != null && !existing.isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "该分类名称已存在！");
        } else {
            Type type = new Type();
            type.setName(name);
            typeService.saveType(type);
            addLog(request, "添加分类", "分类管理", "添加分类: " + name);
            redirectAttributes.addFlashAttribute("msg", "分类添加成功！");
        }
        return "redirect:/admin/typeList";
    }

    @RequestMapping("/deleteType")
    public String deleteType(HttpServletRequest request, @RequestParam int id, RedirectAttributes redirectAttributes) {
        int goodsCount = goodsService.countByTypeId(id);
        if (goodsCount > 0) {
            redirectAttributes.addFlashAttribute("msg", "该分类下有" + goodsCount + "件商品，无法删除！");
        } else {
            addLog(request, "删除分类", "分类管理", "删除分类ID: " + id);
            typeService.deleteType(id);
            redirectAttributes.addFlashAttribute("msg", "分类删除成功！");
        }
        return "redirect:/admin/typeList";
    }

    @RequestMapping("/typeGoods")
    public ModelAndView typeGoods(HttpServletRequest request, @RequestParam int typeId) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        Type type = typeService.getById(typeId);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId);
        List<Goods> goodsList = goodsService.findGoods(queryWrapper);

        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("type", type);
        modelAndView.addObject("goodsList", goodsList);
        modelAndView.addObject("currentPage", "typeList");
        modelAndView.setViewName("admin/typeGoods");
        return modelAndView;
    }

    // ===== 系统管理 =====

    @RequestMapping("/adminSetting")
    public ModelAndView adminSetting(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("currentPage", "adminSetting");
        modelAndView.setViewName("admin/adminSetting");
        return modelAndView;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (oldPassword == null || oldPassword.trim().isEmpty()
                || newPassword == null || newPassword.trim().isEmpty()
                || confirmPassword == null || confirmPassword.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "密码不能为空");
            return "redirect:/admin/adminSetting";
        }

        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMsg", "密码长度至少6位");
            return "redirect:/admin/adminSetting";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMsg", "两次新密码不一致");
            return "redirect:/admin/adminSetting";
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", sessionUser.getId());
        List<User> users = userService.getUsers(queryWrapper);
        if (users != null && !users.isEmpty()) {
            User user = users.get(0);
            if (!user.getPassword().equals(oldPassword.trim())) {
                redirectAttributes.addFlashAttribute("errorMsg", "原密码错误");
                return "redirect:/admin/adminSetting";
            }
            user.setPassword(newPassword.trim());
            userService.updateUser(user);
            addLog(request, "change_password", "system_mgmt", "Changed password");
            redirectAttributes.addFlashAttribute("msg", "密码修改成功");
        }

        return "redirect:/admin/adminSetting";
    }

    @RequestMapping("/logList")
    public ModelAndView logList(HttpServletRequest request,
                                 @RequestParam(defaultValue = "1") int page) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        List<Log> allLogs = logService.findAll(queryWrapper);
        int totalLogs = allLogs != null ? allLogs.size() : 0;
        int totalPages = (int) Math.ceil((double) totalLogs / PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalLogs);
        List<Log> pagedLogs = (totalLogs > 0 && fromIndex < totalLogs)
                ? allLogs.subList(fromIndex, toIndex) : new ArrayList<>();

        Locale locale = LocaleContextHolder.getLocale();
        for (Log log : pagedLogs) {
            String role;
            if ("管理员".equals(log.getUserIsadmin())) {
                role = "管理员";
            } else if (log.getModule() != null && log.getModule().equals("骑士端")) {
                role = "骑手";
            } else {
                role = "普通用户";
            }
            log.setUserIsadmin(LogTranslationUtil.translateRole(role, locale));
            log.setOperation(LogTranslationUtil.translateOperation(log.getOperation(), locale));
            log.setModule(LogTranslationUtil.translateModule(log.getModule(), locale));
        }
        
        modelAndView.addObject("adminName", user.getUsername());
        modelAndView.addObject("logList", pagedLogs);
        modelAndView.addObject("currentPage", "logList");
        modelAndView.addObject("page", page);
        modelAndView.addObject("totalPages", totalPages);
        modelAndView.addObject("totalLogs", totalLogs);
        modelAndView.setViewName("admin/logList");
        return modelAndView;
    }

    @RequestMapping("/cleanLog")
    public String cleanLog(HttpServletRequest request,
                            @RequestParam(defaultValue = "1") int page,
                            RedirectAttributes redirectAttributes) {
        addLog(request, "clean_logs", "system_mgmt", "Cleared all logs");
        logService.deleteAll();
        redirectAttributes.addFlashAttribute("msg", "日志已清空！");
        return "redirect:/admin/logList?page=" + page;
    }

    // ===== 文件上传 =====

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public void uploadImage(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            
            if (file == null || file.isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"请选择要上传的文件\"}");
                return;
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : ".jpg";
            
            String allowedExtensions = ".jpg,.jpeg,.png,.gif";
            if (!allowedExtensions.contains(extension.toLowerCase())) {
                response.getWriter().write("{\"success\":false,\"message\":\"只允许上传JPG、PNG、GIF格式的图片\"}");
                return;
            }

            long maxSize = 5 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                response.getWriter().write("{\"success\":false,\"message\":\"图片大小不能超过5MB\"}");
                return;
            }

            String uploadDir = "c:\\Users\\Administrator\\Downloads\\demo\\src\\main\\resources\\static\\picture";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String newFilename = UUID.randomUUID().toString() + extension;
            Path filePath = Paths.get(uploadDir, newFilename);
            
            Files.write(filePath, file.getBytes());
            
            String relativePath = "/picture/" + newFilename;
            response.getWriter().write("{\"success\":true,\"path\":\"" + relativePath + "\"}");
            
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.getWriter().write("{\"success\":false,\"message\":\"文件上传失败\"}");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}