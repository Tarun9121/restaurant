package com.restaurant.entity;

import com.restaurant.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name="orders")
@Data @NoArgsConstructor @AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Dish foodItem;
    private Integer quantity;
    private LocalDate orderedDate;
    private Double price;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @ManyToOne
    private User user;

    public Order(Dish foodItem, LocalDate orderedDate, OrderStatus orderStatus) {
        this.foodItem = foodItem;
        this.orderedDate = orderedDate;
        this.orderStatus = orderStatus;
    }
}
