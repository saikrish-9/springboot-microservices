package com.project.microservices.order.controller;

import com.project.microservices.order.dto.OrderRequest;
import com.project.microservices.order.dto.OrderResponse;
import com.project.microservices.order.model.Order;
import com.project.microservices.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getAllOrders() {
        String orders = orderService.getAllOrders();
        return orders;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, String>> placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Order Placed Successfully"));
    }

    @GetMapping("/{email}")
    public List<OrderResponse> getOrdersByUserEmail(@PathVariable String email) {
        return orderService.getOrdersByUserEmail(email);
    }
}

