package com.restaurant.convert;

import com.restaurant.dto.CartDto;
import com.restaurant.entity.Cart;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class OrderConvert {

    private final DishConvert dishConvert;

    public OrderConvert(DishConvert dishConvert) {
        this.dishConvert = dishConvert;
    }

    public Cart convert(CartDto cartDto) {
        Cart cart = new Cart();
        if (!ObjectUtils.isEmpty(cartDto)) {
            BeanUtils.copyProperties(cartDto, cart);
            if (cartDto.getFoodItem() != null) {
                cart.setFoodItem(dishConvert.convert(cartDto.getFoodItem())); // Convert DishDto to Dish
            }
        }
        return cart;
    }

    public CartDto convert(Cart cart) {
        CartDto cartDto = new CartDto();
        if (!ObjectUtils.isEmpty(cart)) {
            BeanUtils.copyProperties(cart, cartDto);
            if (cart.getFoodItem() != null) {
                cartDto.setFoodItem(dishConvert.convert(cart.getFoodItem())); // Convert Dish to DishDto
            }
        }
        return cartDto;
    }
}
