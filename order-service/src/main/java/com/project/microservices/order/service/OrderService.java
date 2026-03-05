package com.project.microservices.order.service;

import com.project.microservices.order.client.InventoryClient;
import com.project.microservices.order.dto.OrderRequest;
import com.project.microservices.order.dto.OrderResponse;
import com.project.microservices.order.event.OrderPlaceEvent;
import com.project.microservices.order.model.Order;
import com.project.microservices.order.model.User;
import com.project.microservices.order.repository.OrderRepository;
import com.project.microservices.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlaceEvent> kafkaTemplate;
    private final UserRepository userRepository;

    public void placeOrder(OrderRequest orderRequest) {

        boolean inStock = inventoryClient.isInStock(
                orderRequest.skuCode(),
                orderRequest.quantity()
        );

        if (!inStock) {
            throw new RuntimeException(
                    "Product with SKU code " + orderRequest.skuCode() + " is out of Stock... :("
            );
        }

        User user = userRepository.findByEmail(orderRequest.userDetails().email())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(orderRequest.userDetails().email());
                    newUser.setFirstName(orderRequest.userDetails().firstName());
                    newUser.setLastName(orderRequest.userDetails().lastName());
                    return userRepository.save(newUser);
                });

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price()
                .multiply(BigDecimal.valueOf(orderRequest.quantity())));
        order.setSkuCode(orderRequest.skuCode());
        order.setQuantity(orderRequest.quantity());
        order.setUser(user);

        orderRepository.save(order);

        inventoryClient.updateInventory(
                orderRequest.skuCode(),
                orderRequest.quantity()
        );

        OrderPlaceEvent event = new OrderPlaceEvent();
        event.setOrderNumber(order.getOrderNumber());
        event.setEmail(user.getEmail());
        event.setFirstName(user.getFirstName());
        event.setLastName(user.getLastName());

        log.info("Order Event started to Kafka..! {}", event);
        kafkaTemplate.send("Order-Placed", event);
        log.info("Order Event is done.. :) {}", event);
    }

    public String getAllOrders() {
        StringBuilder stringBuilder = new StringBuilder();
        orderRepository.findAll().forEach(order -> {
            stringBuilder.append("Order Number: ").append(order.getOrderNumber())
                    .append(", SKU Code: ").append(order.getSkuCode())
                    .append(", Price: ").append(order.getPrice())
                    .append(", Quantity: ").append(order.getQuantity())
                    .append("\n");
        });
        return stringBuilder.toString();
    }


    public List<OrderResponse> getOrdersByUserEmail(String email) {

        return orderRepository.findAllByUser_Email(email)
                .stream()
                .map(order -> new OrderResponse(
                        order.getOrderNumber(),
                        order.getSkuCode(),
                        order.getPrice(),
                        order.getQuantity()
                ))
                .toList();
    }
}
