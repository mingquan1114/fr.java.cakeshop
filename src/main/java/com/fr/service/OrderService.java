package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Order;
import com.fr.javaBean.OrderDetailDTO;
import com.fr.javaBean.OrderItem;

import java.util.List;

public interface OrderService {
    void saveOrder(Order order);
    void saveOrderItem(OrderItem orderItem);
    void saveOrderItems(List<OrderItem> orderItems);
    List<Order> findAll(QueryWrapper<Order> queryWrapper);
    List<OrderItem> findOrderItems(QueryWrapper<OrderItem> queryWrapper);
    void deleteOrder(String orderId);
    void deleteOrderItems(String orderId);
    void updateOrder(Order order);
    List<OrderDetailDTO> findOrderDetails(String orderId);
}