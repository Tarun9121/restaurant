package com.restaurant.entity;

import com.restaurant.enums.UserType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Table(name="users")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private String name;
    private String mobileNo;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserType role;
    private String password;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Address> addressList = new ArrayList<>();
}