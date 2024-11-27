package com.restaurant.repository;

import com.restaurant.entity.Cart;
import com.restaurant.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Cart, UUID> {
    List<Cart> findByUser_IdAndOrderStatus(UUID userId, OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.foodItem.id = :dishId AND o.orderStatus = 'IN_CART'")
    Optional<Cart> getCurrentOrder(@Param("userId") UUID userId, @Param("dishId") UUID dishId);

}