package com.example.shopapp.services.orderDetail;

import com.example.shopapp.entities.OrderDetail;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.dtos.OrderDetailDTO;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData)
            throws DataNotFoundException;
    void deleteById(Long id);
    List<OrderDetail> findByOrderId(Long orderId);


}
