package com.example.shopapp.services.order;

import com.example.shopapp.models.dtos.OrderDTO;
import com.example.shopapp.models.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO);
    OrderResponse findById(Long orderId);
    OrderResponse updateOrder(Long orderId, OrderDTO orderDTO);
    Boolean deleteOrder(Long orderId);
    List<OrderResponse> findByUserId(Long userId);
}
