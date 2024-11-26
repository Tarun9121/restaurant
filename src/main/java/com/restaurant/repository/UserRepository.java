package com.restaurant.repository;

import com.restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByMobileNo(String mobileNo);
}
