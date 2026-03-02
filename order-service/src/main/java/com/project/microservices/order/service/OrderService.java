package com.project.microservices.order.service;

import com.project.microservices.order.client.InventoryClient;
import com.project.microservices.order.dto.OrderRequest;
import com.project.microservices.order.event.OrderPlaceEvent;
import com.project.microservices.order.model.Order;
import com.project.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlaceEvent> kafkaTemplate;


    public void placeOrder(OrderRequest orderRequest) {
        boolean val= inventoryClient.isInStock(orderRequest.skuCode(),orderRequest.quantity());
        if (val) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price().multiply(BigDecimal.valueOf(orderRequest.quantity())));
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);

            OrderPlaceEvent orderPlaceEvent = new OrderPlaceEvent();
            orderPlaceEvent.setOrderNumber(order.getOrderNumber());
            orderPlaceEvent.setEmail(orderRequest.userDetails().email());
            orderPlaceEvent.setFirstName(orderRequest.userDetails().firstName());
            orderPlaceEvent.setLastName(orderRequest.userDetails().lastName());
            log.info("Order Event started to Kafka..!{}",orderPlaceEvent);
            kafkaTemplate.send("Order-Placed",orderPlaceEvent);
            log.info("Order Event is done..:) {}",orderPlaceEvent);
        }
        else {
            throw new RuntimeException("Product with SKU code " + orderRequest.skuCode() + " is out of Stock... :(");
        }

    }
}
