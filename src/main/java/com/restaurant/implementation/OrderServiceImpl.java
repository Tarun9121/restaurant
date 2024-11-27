package com.restaurant.implementation;

import com.restaurant.constants.Restaurant;
import com.restaurant.convert.OrderConvert;
import com.restaurant.dto.CartDto;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Cart;
import com.restaurant.entity.User;
import com.restaurant.enums.FoodCategory;
import com.restaurant.enums.OrderStatus;
import com.restaurant.exception.ApiException;
import com.restaurant.repository.DishRepository;
import com.restaurant.repository.OrderRepository;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final OrderConvert orderConvert;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, DishRepository dishRepository, OrderConvert orderConvert, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
        this.orderConvert = orderConvert;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<List<CartDto>> viewCart(UUID userId) {
        try {
            List<Cart> cartItems = orderRepository.findByUser_IdAndOrderStatus(userId, OrderStatus.IN_CART);
            List<CartDto> cartDto = cartItems.stream().map(orderConvert::convert).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(cartDto);
        } catch (Exception ex) {
            log.error("Error while fetching cart items for user ID {}: {}", userId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Cart>> orderFood(UUID userId) {
        try {
            List<Cart> cartItems = orderRepository.findByUser_IdAndOrderStatus(userId, OrderStatus.IN_CART);

            cartItems.forEach(item -> item.setOrderStatus(OrderStatus.ORDER_PLACED));

            List<Cart> orderdItems = orderRepository.saveAll(cartItems);

            return ResponseEntity.status(HttpStatus.OK).body(orderdItems);

        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<List<CartDto>> viewOrders(UUID userId) {
        try {
            List<Cart> carts = orderRepository.findByUser_IdAndOrderStatus(userId, OrderStatus.ORDER_PLACED);
            List<CartDto> ordersDto = carts.stream().map(orderConvert::convert).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ordersDto);
        } catch (Exception ex) {
            log.error("Error while fetching orders for user ID {}: {}", userId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> removeFromCart(UUID userId, UUID orderId) {
        try {
            
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException("User not found with ID: " + userId));

            Cart cart = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ApiException("Order not found with ID: " + orderId));

            if (!cart.getUser().getId().equals(userId)) {
                throw new ApiException("Specified food item is not in your cart");
            }
            if(!OrderStatus.IN_CART.equals(cart.getOrderStatus())) {
                throw new ApiException("Order is placed and cannot be removed");
            }

            orderRepository.delete(cart);
            return ResponseEntity.status(HttpStatus.OK).body("Item removed from cart successfully");
        } catch (ApiException ex) {
            log.error("Error while removing item from cart: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error while removing item from cart: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to remove item from cart. Please try again later.");
        }
    }

    @Override
    public ResponseEntity<CartDto> addToCart(UUID userId, UUID dishId, Integer quantity) {
        try {
            Dish dish = dishRepository.findById(dishId)
                    .orElseThrow(() -> new ApiException("Dish not found with ID: " + dishId));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException("User not found with ID: " + userId));

            if (quantity <= 0) {
                throw new ApiException("Quantity must be greater than zero");
            }

            Cart cart = new Cart();
            cart.setUser(user);
            cart.setFoodItem(dish);
            cart.setQuantity(quantity);
            cart.setOrderedDate(LocalDate.now());
            cart.setPrice(dish.getPrice() * quantity);
            cart.setOrderStatus(OrderStatus.IN_CART);

            Cart savedCart = orderRepository.save(cart);
            CartDto savedCartDto = orderConvert.convert(savedCart);
            savedCartDto.setMessage(Restaurant.DISH_ADDED_SUCCESSFULLY);

            return ResponseEntity.status(HttpStatus.OK).body(savedCartDto);
        } catch (ApiException ex) {
            log.error("Error while adding item to cart: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            log.error("Unexpected error while adding item to cart for user ID {}: {}", userId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CartDto>> getAllFoodItems(String sortBy) {
        try {
            List<Dish> foodItems = sortBy.equalsIgnoreCase("high to low")
                    ? dishRepository.findAll(Sort.by(Sort.Direction.DESC, "price"))
                    : dishRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));

            List<CartDto> foodItemDtos = foodItems.stream()
                    .map(dish -> orderConvert.convert(new Cart(dish, LocalDate.now(), OrderStatus.IN_CART)))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(foodItemDtos);
        } catch (Exception ex) {
            log.error("Error while fetching all food items: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CartDto>> searchFood(String foodName, Boolean foodType, String sortBy) {
        try {
            List<Dish> foodItems = foodType != null
                    ? sortBy.equalsIgnoreCase("high to low")
                    ? dishRepository.getAllDishesByNameAndIsVegDesc(foodName)
                    : dishRepository.getAllDishesByNameAndIsVegAsc(foodName)
                    : dishRepository.getAllDishesByName(foodName);

            List<CartDto> foodItemDtos = foodItems.stream()
                    .map(dish -> orderConvert.convert(new Cart(dish, LocalDate.now(), OrderStatus.IN_CART)))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(foodItemDtos);
        } catch (Exception ex) {
            log.error("Error while searching food items: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CartDto>> getFoodByCategory(String category, Boolean foodType, String sortBy) {
        try {
            List<Dish> foodItems;
            FoodCategory foodCategory = FoodCategory.valueOf(category.toUpperCase());

            if (foodType != null) {
                foodItems = sortBy.equalsIgnoreCase("high to low")
                        ? dishRepository.findByFoodCategoryAndIsVegDesc(category, foodType)
                        : dishRepository.findByFoodCategoryAndIsVegAsc(category, foodType);
            } else {
                foodItems = dishRepository.findByFoodCategory(category);
            }

            List<CartDto> foodItemDtos = foodItems.stream()
                    .map(dish -> orderConvert.convert(new Cart(dish, LocalDate.now(), OrderStatus.IN_CART)))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(foodItemDtos);
        } catch (Exception ex) {
            log.error("Error while fetching food items by category: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<CartDto> updateOrder(UUID userId, UUID dishId, Integer quantity) {
        try {
            Optional<Cart> existingOrder = orderRepository.getCurrentOrder(userId, dishId);
            Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new ApiException(Restaurant.DISH_NOT_FOUND));

            if(existingOrder.isPresent()) {
                Cart currentCart = orderRepository.findById(existingOrder.get().getId()).orElseThrow();
                currentCart.setQuantity(quantity);
                currentCart.setPrice(quantity * dish.getPrice());
                Cart updatedCart = orderRepository.save(currentCart);
                CartDto updatedCartDto = orderConvert.convert(updatedCart);

                updatedCartDto.setMessage(Restaurant.DISH_UPDTED_SUCCESSFULLY);
                return ResponseEntity.status(HttpStatus.OK).body(updatedCartDto);
            }
            else {
                return addToCart(userId, dishId, quantity);
            }
        } catch (Exception e) {
            CartDto error = new CartDto();

            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}