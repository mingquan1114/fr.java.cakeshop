package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Order;
import com.fr.javaBean.OrderDetailDTO;
import com.fr.javaBean.OrderItem;
import com.fr.mapper.OrderItemMapper;
import com.fr.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    OrderMapper orderMapper;
    @Resource
    OrderItemMapper orderItemMapper;

    @Override
    public void saveOrder(Order order) {
        orderMapper.insert(order);
    }

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        orderItemMapper.insert(orderItem);
    }

    @Override
    public void saveOrderItems(List<OrderItem> orderItems) {
        System.out.println("OrderServiceImpl.saveOrderItems: saving " + orderItems.size() + " items");
        for (OrderItem item : orderItems) {
            if (item.getOrderId() != null) {
                item.setOrderId(item.getOrderId().trim());
            }
            System.out.println("OrderServiceImpl.saveOrderItems: inserting item - order_id=" + item.getOrderId() + ", goods_id=" + item.getGoodsId());
            int result = orderItemMapper.insert(item);
            System.out.println("OrderServiceImpl.saveOrderItems: insert result=" + result + ", item.id=" + item.getId());
        }
    }

    @Override
    public List<Order> findAll(QueryWrapper<Order> queryWrapper) {
        return orderMapper.selectList(queryWrapper);
    }

    @Override
    public List<OrderItem> findOrderItems(QueryWrapper<OrderItem> queryWrapper) {
        return orderItemMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteOrder(String orderId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);
        orderMapper.delete(queryWrapper);
    }

    @Override
    public void deleteOrderItems(String orderId) {
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        orderItemMapper.delete(queryWrapper);
    }

    @Override
    public void updateOrder(Order order) {
        orderMapper.updateById(order);
    }

    @Override
    public List<OrderDetailDTO> findOrderDetails(String orderId) {
        return orderItemMapper.selectOrderDetails(orderId);
    }
}
