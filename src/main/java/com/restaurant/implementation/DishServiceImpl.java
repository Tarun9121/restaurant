package com.restaurant.implementation;

import com.restaurant.constants.Restaurant;
import com.restaurant.convert.DishConvert;
import com.restaurant.dto.DishDto;
import com.restaurant.entity.Dish;
import com.restaurant.enums.FoodCategory;
import com.restaurant.enums.IsAsc;
import com.restaurant.exception.ApiException;
import com.restaurant.repository.DishRepository;
import com.restaurant.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    private final DishConvert dishConvert;
    private final DishRepository dishRepository;

    public DishServiceImpl(DishConvert dishConvert, DishRepository dishRepository) {
        this.dishConvert = dishConvert;
        this.dishRepository = dishRepository;
    }

    public ResponseEntity<DishDto> addDishToMenu(DishDto dishDto) {
        try {
            if(ObjectUtils.isEmpty(dishDto)) {
                throw new ApiException(Restaurant.DISH_EMPTY);
            }

            Dish dish = dishConvert.convert(dishDto);
            Dish savedDish = dishRepository.save(dish);
            DishDto savedDishDto = dishConvert.convert(savedDish);

            return ResponseEntity.status(HttpStatus.OK).body(savedDishDto);
        } catch (Exception e) {
            DishDto error = new DishDto();

            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @Override
    public ResponseEntity<DishDto> udpateDish(DishDto dishDto) {
        return addDishToMenu(dishDto);
    }

    public ResponseEntity<String> removeDish(UUID dishId) {
        try {
            Dish existingDish = dishRepository.findById(dishId)
                    .orElseThrow(() -> new ApiException(Restaurant.DISH_NOT_FOUND));

            dishRepository.delete(existingDish);
            return ResponseEntity.status(HttpStatus.OK).body(Restaurant.DISH_REMOVED_SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Restaurant.DISH_REMOVED_FAILED);
        }
    }

    @Override
    public ResponseEntity<List<DishDto>> getAllAvailableDishes() {
        try {
            List<Dish> allDishes = dishRepository.getAllAvailableDishes();
            List<DishDto> allDishesDto = dishConvert.convert(allDishes);
            return ResponseEntity.status(HttpStatus.OK).body(allDishesDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<List<FoodCategory>> getCategories() {
        try {
            List<FoodCategory> allCategories = Arrays.asList(FoodCategory.class.getEnumConstants());
            return ResponseEntity.status(HttpStatus.OK).body(allCategories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<List<DishDto>> getAllDishes(FoodCategory foodCategory, String foodName, Boolean isVeg, boolean isAsc) {
        List<Dish> allDishes;
        if(!ObjectUtils.isEmpty(foodCategory)) {
            if(!ObjectUtils.isEmpty(isVeg)) {
                if(isAsc) {
                    allDishes = dishRepository.getAllDishesByAsc(foodCategory.toString(), foodName, isVeg);
                }
                else {
                    allDishes = dishRepository.getAllDishes(foodCategory.toString(), foodName, isVeg);
                }
            }else {
                if(isAsc) {
                    allDishes = dishRepository.getAllDishesByAsc(foodCategory.toString(), foodName);
                }
                else {
                    allDishes = dishRepository.getAllDishes(foodCategory.toString(), foodName);
                }
            }

        }
        else {
            if(!ObjectUtils.isEmpty(isVeg)) {
                if(isAsc) {
                    allDishes = dishRepository.getAllDishesByAsc(foodName, isVeg);
                }
                else {
                    allDishes = dishRepository.getAllDishes(foodName, isVeg);
                }
            }
            else {
                if(isAsc) {
                    allDishes = dishRepository.getAllDishesByAsc(foodName);
                }
                else {
                    allDishes = dishRepository.getAllDishesByName(foodName);
                }
            }
        }

        List<DishDto> allDishesDto = dishConvert.convert(allDishes);
        return ResponseEntity.status(HttpStatus.OK).body(allDishesDto);
    }
}
