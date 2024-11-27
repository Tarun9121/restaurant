package com.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserDto extends BaseResponse {
    private UUID id;
    private String name;
    private String mobileNo;
    private String email;
    private String role;
    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    private String password;
    private List<CartDto> orders;
    private List<AddressDto> addressList;
}

//{
//        "status": null,
//        "message": null,
//        "id": "7c7d95f1-c1fd-4533-b13f-6c4f9dc39c4d",
//        "name": "john",
//        "mobileNo": "9876543210",
//        "email": "john@gmail.com",
//        "role": "CUSTOMER",
//        "password": "1234",
//        "orders": null,
//        "addressList": []
//        }