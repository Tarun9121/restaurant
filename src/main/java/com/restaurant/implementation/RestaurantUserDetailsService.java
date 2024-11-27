//package com.restaurant.implementation;
//
//import com.restaurant.entity.UserPrincipal;
//import com.restaurant.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import com.restaurant.entity.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ObjectUtils;
//
//@Slf4j
//@Service
//public class RestaurantUserDetailsService implements UserDetailsService {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String mobileNo) throws UsernameNotFoundException {
//        log.info("loading user details");
//        User user = userRepository.findByMobileNo(mobileNo);
//
//        if(ObjectUtils.isEmpty(user)) {
//            log.info("user details not found");
//            throw new UsernameNotFoundException("User not found with the provided mobile no");
//        }
//
//        log.info("user details fetched, now validating");
//
//        return new UserPrincipal(user);
//    }
//}
