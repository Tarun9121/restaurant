package com.restaurant.service;

import com.restaurant.dto.OrderDto;
import com.restaurant.entity.Order;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    ResponseEntity<List<OrderDto>> viewCart(UUID userId);

    ResponseEntity<List<OrderDto>> viewOrders(UUID userId);

    ResponseEntity<OrderDto> addToCart(UUID userId, UUID dishId, Integer quantity);

    ResponseEntity<List<Order>> orderFood(UUID userId);

    ResponseEntity<List<OrderDto>> getAllFoodItems(String sortBy);

    ResponseEntity<List<OrderDto>> searchFood(String foodName, Boolean foodType, String sortBy);

    ResponseEntity<List<OrderDto>> getFoodByCategory(String category, Boolean foodType, String sortBy);

    ResponseEntity<OrderDto> updateOrder(UUID userId, UUID dishId, Integer quantity);

    ResponseEntity<String> removeFromCart(UUID userId, UUID orderId);
}