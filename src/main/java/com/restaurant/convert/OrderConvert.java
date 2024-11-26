package com.restaurant.convert;

import com.restaurant.dto.OrderDto;
import com.restaurant.entity.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class OrderConvert {

    private final DishConvert dishConvert;

    public OrderConvert(DishConvert dishConvert) {
        this.dishConvert = dishConvert;
    }

    public Order convert(OrderDto orderDto) {
        Order order = new Order();
        if (!ObjectUtils.isEmpty(orderDto)) {
            BeanUtils.copyProperties(orderDto, order);
            if (orderDto.getFoodItem() != null) {
                order.setFoodItem(dishConvert.convert(orderDto.getFoodItem())); // Convert DishDto to Dish
            }
        }
        return order;
    }

    public OrderDto convert(Order order) {
        OrderDto orderDto = new OrderDto();
        if (!ObjectUtils.isEmpty(order)) {
            BeanUtils.copyProperties(order, orderDto);
            if (order.getFoodItem() != null) {
                orderDto.setFoodItem(dishConvert.convert(order.getFoodItem())); // Convert Dish to DishDto
            }
        }
        return orderDto;
    }
}
