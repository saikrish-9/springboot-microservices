package com.project.microservices.order.dto;

import java.math.BigDecimal;

public record OrderRequest(Long id, String orderNumber, String skuCode,
                           BigDecimal price, Integer quantity, UserDetails userDetails) {

    public record UserDetails(Long id,String email, String firstName, String lastName) {}
}
