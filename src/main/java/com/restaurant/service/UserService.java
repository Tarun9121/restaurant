package com.restaurant.service;

import com.restaurant.dto.UserDto;
import com.restaurant.dto.AddressDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserService {
    UserDto registerUser(UserDto userDto);
    UserDto addAddress(UUID userId, AddressDto addressDto);
    UserDto editProfile(UUID userId, UserDto userDto);
    UserDto getProfile(UUID userId);
    ResponseEntity<UserDto> updateUser(UUID userId, UserDto userDto);
}