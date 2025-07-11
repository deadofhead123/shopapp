package com.example.shopapp.components.converter;

import com.example.shopapp.entities.Order;
import com.example.shopapp.models.dtos.OrderDTO;
import com.example.shopapp.models.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConverter {
    private final ModelMapper modelMapper;

    public Order convertToEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    public OrderResponse convertToResponse(Order order){
        if(order == null) return null;
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        orderResponse.setUserId(order.getUser().getId());
        return orderResponse;
    }
}
