package com.restaurant.repository;

import com.restaurant.entity.Dish;
import com.restaurant.enums.FoodCategory;
import com.restaurant.enums.IsAsc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {
    @Query(nativeQuery = true, value="select * from food_items where is_available = true")
    List<Dish> getAllAvailableDishes();

    @Query(nativeQuery = true, value="select * from food_items where name like %:foodName% and is_available = true")
    List<Dish> getAllDishesByName(@Param("foodName") String foodName);

    @Query(nativeQuery = true, value = "select * from food_items where is_veg = :isVeg and name like %:foodName% and is_available = true")
    List<Dish> getAllDishes(@Param("foodName") String foodName, @Param("isVeg") boolean isVeg);

    @Query(nativeQuery = true, value="select * from food_items where food_category = :foodCategory and name like %:foodName% and is_available = true")
    List<Dish> getAllDishes(@Param("foodCategory") String foodCategory, @Param("foodName") String foodName);

    @Query(nativeQuery = true, value = "select * from food_items where is_veg = :isVeg and food_category = :foodCategory and name like %:foodName% and is_available = true")
    List<Dish> getAllDishes(@Param("foodCategory") String foodCategory, @Param("foodName") String foodName, @Param("isVeg") boolean isVeg);

    @Query(nativeQuery = true, value="select * from food_items where name like %:foodName% order by price")
    List<Dish> getAllDishesByAsc(@Param("foodName") String foodName);

    @Query(nativeQuery = true, value="select * from food_items where food_category = :foodCategory and name like %:foodName% and is_available = true order by price asc")
    List<Dish> getAllDishesByAsc(@Param("foodCategory") String foodCategory, @Param("foodName") String foodName);

    @Query(nativeQuery = true, value = "select * from food_items where is_veg = :isVeg and name like %:foodName% and is_available = true order by price asc")
    List<Dish> getAllDishesByAsc(@Param("foodName") String foodName, @Param("isVeg") boolean isVeg);

    @Query(nativeQuery = true, value = "select * from food_items where is_veg = :isVeg and food_category = :foodCategory and name like %:foodName% and is_available = true order by price asc")
    List<Dish> getAllDishesByAsc(@Param("foodCategory") String foodCategory, @Param("foodName") String foodName, @Param("isVeg") boolean isVeg);

    @Query(nativeQuery = true, value = "SELECT * FROM food_items WHERE name like %:foodName% and is_available = true ORDER BY price DESC")
    List<Dish> getAllDishesByNameAndIsVegDesc(@Param("foodName") String foodName);

    @Query(nativeQuery = true, value = "SELECT * FROM food_items WHERE name like %:foodName% and is_available = true ORDER BY price ASC")
    List<Dish> getAllDishesByNameAndIsVegAsc(@Param("foodName") String foodName);

    @Query(nativeQuery = true, value = "SELECT * FROM food_items WHERE food_category = :foodCategory AND is_veg = :isVeg and is_available = true ORDER BY price DESC")
    List<Dish> findByFoodCategoryAndIsVegDesc(@Param("foodCategory") String foodCategory, @Param("isVeg") boolean isVeg);

    @Query(nativeQuery = true, value = "SELECT * FROM food_items WHERE food_category = :foodCategory AND is_veg = :isVeg and is_available = true ORDER BY price ASC")
    List<Dish> findByFoodCategoryAndIsVegAsc(@Param("foodCategory") String foodCategory, @Param("isVeg") boolean isVeg);

    @Query(nativeQuery = true, value = "SELECT * FROM food_items WHERE food_category = :foodCategory")
    List<Dish> findByFoodCategory(@Param("foodCategory") String foodCategory);

}