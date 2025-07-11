package com.example.shopapp.controllers;


import com.example.shopapp.models.dtos.OrderDTO;
import com.example.shopapp.models.dtos.ResponseDTO;
import com.example.shopapp.services.order.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            if (result.hasErrors()) {
                responseDTO.setErrors(result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList());
                return ResponseEntity.badRequest().body(responseDTO);
            }
            responseDTO.setData(orderService.createOrder(orderDTO));
            responseDTO.setMessage("createOrder successfully");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}") // Thêm biến đường dẫn "user_id"
    //GET http://localhost:8088/api/v1/orders/4
    public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setMessage("Orders with user's id = " + userId);
            responseDTO.setData(orderService.findByUserId(userId));
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}") // Thêm biến đường dẫn "user_id"
    //GET http://localhost:8088/api/v1/orders/4
    public ResponseEntity<?> getOrders(@Valid @PathVariable("id") Long orderId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setMessage("Order with id = " + orderId);
            responseDTO.setData(orderService.findById(orderId));
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    //PUT http://localhost:8088/api/v1/orders/2
    //công việc của admin
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable long id,
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            if (result.hasErrors()) {
                responseDTO.setErrors(result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList());
                return ResponseEntity.badRequest().body(responseDTO);
            }
            responseDTO.setData(orderService.updateOrder(id, orderDTO));
            responseDTO.setMessage("Order updated successfully");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //xóa mềm => cập nhật trường active = false
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            responseDTO.setData(orderService.deleteOrder(id));
            responseDTO.setMessage("Order deleted successfully.");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
