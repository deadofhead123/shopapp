package com.example.shopapp.services.order;

import com.example.shopapp.components.converter.OrderConverter;
import com.example.shopapp.constant.OrderStatus;
import com.example.shopapp.entities.Order;
import com.example.shopapp.entities.User;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.dtos.OrderDTO;
import com.example.shopapp.models.responses.OrderResponse;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) {
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("Cannot find user with id = " + orderDTO.getUserId()));

        Order newOrder = orderConverter.convertToEntity(orderDTO);
        newOrder.setId(null);
        newOrder.setUser(existingUser);
        newOrder.setStatus(OrderStatus.PENDING.getName());
        newOrder.setActive(true);
        newOrder.setOrderDate(new Date());

        return orderConverter.convertToResponse(orderRepository.save(newOrder));
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        return orderConverter.convertToResponse(
                orderRepository.findById(orderId)
                                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id = " + orderId))
        );
    }

    @Override
    public OrderResponse updateOrder(Long orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                                            .orElseThrow(() -> new DataNotFoundException("Cannot find order with id = " + orderId));
        User userInOrder = existingOrder.getUser();

        // Control the mapping
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId)); // Skip set 'id' of order

        modelMapper.map(orderDTO, existingOrder);
        existingOrder.setUser(userInOrder);

        return orderConverter.convertToResponse(orderRepository.save(existingOrder));
    }

    @Override
    public Boolean deleteOrder(Long orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id = " + orderId));
        existingOrder.setActive(false);
        orderRepository.save(existingOrder);
        return true;
    }

    @Override
    public List<OrderResponse> getOrderByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(orderConverter::convertToResponse).collect(Collectors.toList());
    }
}
