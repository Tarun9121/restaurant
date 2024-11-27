package com.restaurant.service;

import com.restaurant.dto.CartDto;
import com.restaurant.entity.Cart;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {
    ResponseEntity<List<CartDto>> viewCart(UUID userId);

    ResponseEntity<List<CartDto>> viewOrders(UUID userId);

    ResponseEntity<CartDto> addToCart(UUID userId, UUID dishId, Integer quantity);

    ResponseEntity<List<Cart>> orderFood(UUID userId);

    ResponseEntity<List<CartDto>> getAllFoodItems(String sortBy);

    ResponseEntity<List<CartDto>> searchFood(String foodName, Boolean foodType, String sortBy);

    ResponseEntity<List<CartDto>> getFoodByCategory(String category, Boolean foodType, String sortBy);

    ResponseEntity<CartDto> updateOrder(UUID userId, UUID dishId, Integer quantity);

    ResponseEntity<String> removeFromCart(UUID userId, UUID orderId);
}