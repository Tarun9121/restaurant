package com.restaurant.service;

import com.restaurant.convert.DishConvert;
import com.restaurant.dto.DishDto;
import com.restaurant.enums.FoodCategory;
import com.restaurant.repository.DishRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface DishService {
    ResponseEntity<DishDto> addDishToMenu(DishDto dishDto);
    ResponseEntity<DishDto> udpateDish(DishDto dishDto);
    ResponseEntity<String> removeDish(UUID dishId);
    ResponseEntity<List<DishDto>> getAllDishes(FoodCategory foodCategory, String foodName, Boolean isVeg, boolean sortByAsc);
    ResponseEntity<List<DishDto>> getAllAvailableDishes();
}
