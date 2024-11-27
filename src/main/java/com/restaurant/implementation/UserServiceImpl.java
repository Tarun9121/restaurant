package com.restaurant.implementation;

import com.restaurant.conversion.UserConvert;
import com.restaurant.dto.UserDto;
import com.restaurant.dto.AddressDto;
import com.restaurant.entity.User;
import com.restaurant.entity.Address;
import com.restaurant.enums.UserType;
import com.restaurant.exception.ApiException;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConvert userConvert;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public UserServiceImpl(UserRepository userRepository, UserConvert userConversion) {
        this.userRepository = userRepository;
        this.userConvert = userConversion;

    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        try {
            User user = userConvert.convert(userDto); // Convert UserDto to User
            user.setPassword(encoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            return userConvert.convert(savedUser); // Convert User to UserDto
        } catch (Exception ex) {
            log.error("Error during user registration: {}", ex.getMessage());
            throw new ApiException("Unable to register user. Please try again later.");
        }
    }

    @Override
    public UserDto addAddress(UUID userId, AddressDto addressDto) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException("User not found with ID: " + userId));

            Address address = userConvert.convert(addressDto); // Convert AddressDto to Address
            user.getAddressList().add(address);

            User updatedUser = userRepository.save(user);
            return userConvert.convert(updatedUser); // Convert User to UserDto
        } catch (Exception ex) {
            log.error("Error while adding address: {}", ex.getMessage());
            throw new ApiException("Unable to add address. Please try again later.");
        }
    }


    @Override
    public UserDto editProfile(UUID userId, UserDto userDto) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException("User not found with ID: " + userId));

            user.setName(userDto.getName());
            user.setMobileNo(userDto.getMobileNo());
            user.setEmail(userDto.getEmail());
            user.setRole(UserType.valueOf(userDto.getRole().toUpperCase()));
            user.setPassword(userDto.getPassword());

            if (userDto.getAddressList() != null) {
                List<Address> addresses = userDto.getAddressList()
                        .stream()
                        .map(addressDto -> userConvert.convert(addressDto)) // Explicit call
                        .collect(Collectors.toList());
                user.setAddressList(addresses);
            }

            User updatedUser = userRepository.save(user);
            return userConvert.convert(updatedUser); // Automatically uses User to UserDto conversion
        } catch (ApiException ex) {
            log.error("Error while editing profile: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while editing profile for user ID {}: {}", userId, ex.getMessage());
            throw new ApiException("Unable to edit profile. Please try again later.");
        }
    }

    @Override
    public UserDto getProfile(UUID userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException("User not found with ID: " + userId));
            return userConvert.convert(user); // Automatically uses User to UserDto conversion
        } catch (ApiException ex) {
            log.error("Error while fetching profile: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while fetching profile for user ID {}: {}", userId, ex.getMessage());
            throw new ApiException("Unable to fetch profile. Please try again later.");
        }
    }

    @Override
    public ResponseEntity<UserDto> updateUser(UUID userId, UserDto userDto) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException("User not found with ID: " + userId));

            if (userDto.getName() != null) {
                user.setName(userDto.getName());
            }
            if (userDto.getMobileNo() != null) {
                user.setMobileNo(userDto.getMobileNo());
            }
            if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            }
            if (userDto.getRole() != null) {
                user.setRole(UserType.valueOf(userDto.getRole().toUpperCase()));
            }
            if (userDto.getPassword() != null) {
                user.setPassword(userDto.getPassword());
            }

            User updatedUser = userRepository.save(user);
            UserDto updatedUserDto = userConvert.convert(updatedUser);

            return ResponseEntity.status(HttpStatus.OK).body(updatedUserDto);

        } catch (Exception e) {
            log.error("Error while updating user: {}", e.getMessage());
            UserDto error = new UserDto();
            error.setStatus(HttpStatus.BAD_REQUEST);
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

}