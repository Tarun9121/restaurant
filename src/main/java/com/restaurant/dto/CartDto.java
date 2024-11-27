package com.restaurant.dto;

import com.restaurant.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class CartDto extends BaseResponse{
    private UUID id;
    private DishDto foodItem;
    private Integer quantity;
    private LocalDate orderedDate;
    private String orderStatus;
    private Double price;
    private User user;
}
