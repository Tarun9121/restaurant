package com.restaurant.repository;

import com.restaurant.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser_IdAndOrderStatus(UUID userId, String status);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.foodItem.id = :dishId AND o.orderStatus = 'IN_CART'")
    Optional<Order> getCurrentOrder(@Param("userId") UUID userId, @Param("dishId") UUID dishId);

}