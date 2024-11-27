package com.restaurant.controller;

import com.restaurant.dto.CartDto;
import com.restaurant.entity.Cart;
import com.restaurant.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer/orders")
@Tag(name = "Order Controller", description = "Operations related to orders and cart management")
@CrossOrigin
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/all-food-items")
    @Operation(
            summary = "Get All Food Items",
            description = "Fetch all food items with sorting (high to low or low to high price)."
    )
    public ResponseEntity<List<CartDto>> getAllFoodItems(
            @RequestParam(required = false, defaultValue = "low to high") String sortBy) {
        return cartService.getAllFoodItems(sortBy);
    }

    @GetMapping("/food-search/{foodName}")
    @Operation(
            summary = "Search Food",
            description = "Search food by name, type (veg or non-veg), and sorting (low to high or high to low price)."
    )
    public ResponseEntity<List<CartDto>> searchFood(
            @PathVariable String foodName,
            @RequestParam(required = false) Boolean foodType, // true = veg, false = non-veg
            @RequestParam(required = false, defaultValue = "low to high") String sortBy) {
        return cartService.searchFood(foodName, foodType, sortBy);
    }

//    @GetMapping("/food-category/{category}")
//    @Operation(
//            summary = "Get Food by Category",
//            description = "Fetch food items by category, type (veg or non-veg), and sorting (low to high or high to low price)."
//    )
//    public ResponseEntity<List<OrderDto>> getFoodByCategory(
//            @PathVariable FoodCategory category,
//            @RequestParam(required = false) Boolean foodType,
//            @RequestParam(required = false, defaultValue = "low to high") String sortBy) {
//        return orderService.getFoodByCategory(category, foodType, sortBy);
//    }

    @GetMapping("/view-cart/{userId}")
    @Operation(
            summary = "View Cart",
            description = "View the cart for a specific user by user ID."
    )
    public ResponseEntity<List<CartDto>> viewCart(@PathVariable UUID userId) {
        return cartService.viewCart(userId);
    }

    @PutMapping("/order-food/{userId}")
    public ResponseEntity<List<Cart>> orderFood(@PathVariable("userId") UUID userId) {
        return cartService.orderFood(userId);
    }

    @GetMapping("/view-orders/{userId}")
    @Operation(
            summary = "View Placed Orders",
            description = "View the placed orders for a specific user by user ID."
    )
    public ResponseEntity<List<CartDto>> viewOrders(@PathVariable UUID userId) {
        return cartService.viewOrders(userId);
    }

    @PostMapping("/add-to-cart/{userId}/{dishId}")
    @Operation(
            summary = "Add Items to Cart",
            description = "Add items to the cart for a specific user by user ID and dish ID, with the specified quantity."
    )
    public ResponseEntity<CartDto> addToCart(
            @PathVariable UUID userId,
            @PathVariable UUID dishId,
            @RequestParam Integer quantity) {
        return cartService.addToCart(userId, dishId, quantity);
    }

    @PutMapping("/update-quantity/{userId}/{dishId}")
    public ResponseEntity<CartDto> updateQuantity(
            @PathVariable UUID userId,
            @PathVariable UUID dishId,
            @RequestParam Integer quantity
    ) {
        return cartService.updateOrder(userId, dishId, quantity);
    }

    @DeleteMapping("/remove-from-cart/{userId}/{orderId}")
    @Operation(summary = "Remove Item from Cart", description = "Removes a specific item from the user's cart.")
    public ResponseEntity<String> removeFromCart(@PathVariable UUID userId, @PathVariable UUID orderId) {
        return cartService.removeFromCart(userId, orderId);
    }
}
