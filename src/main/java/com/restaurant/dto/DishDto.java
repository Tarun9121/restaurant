package com.restaurant.dto;

import com.restaurant.enums.FoodCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class DishDto extends BaseResponse {
    private UUID id;
    private String name;
    private String description;
    private Boolean isAvailable = Boolean.FALSE;
    private FoodCategory foodCategory;
    private Boolean isVeg;
    private Double price;
}
