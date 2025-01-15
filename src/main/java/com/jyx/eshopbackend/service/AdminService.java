package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.UserUpdateDTO;
import com.jyx.eshopbackend.exception.PasswordNotMatchException;
import com.jyx.eshopbackend.model.Order;
import com.jyx.eshopbackend.model.User;
import com.jyx.eshopbackend.persistence.OrderRepository;
import com.jyx.eshopbackend.persistence.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminService(UserService userService, UserRepository userRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
        logger.info("Admin service starts");
    }

    public Optional<List<Order>> findOrdersByUsername(String username) {
        logger.info("Admin service: find orders that belong to user : " + username);
        Optional<Long> id = userService.findUserIdByUsername(username);
        if(id.isEmpty()) {
            return Optional.empty();
        }
        return orderRepository.findOrdersByUser_Id(id.get());
    }


    public void removeAllUsers(){
        logger.info("Admin service: removeAllUsers()");
        userRepository.deleteAll();
    }

    public Optional<String> removeUserByUsername(String username) {
        logger.info("Admin service: remove user by username : " + username);
        User user;
        try {
            user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (Exception e) {
            logger.warn("user not found");
            return Optional.empty();
        }
        userRepository.deleteById(user.getId());
       return Optional.ofNullable(user.getUsername());
    }

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(UserUpdateDTO userUpdateDTO) throws Exception {
        User user = userRepository.findByUsername(userUpdateDTO.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No user found"));
        if(!passwordEncoder.matches(userUpdateDTO.getOldPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("Old password does not match.");
        }
        user.setUsername(userUpdateDTO.getNewUsername());
        user.setEmail(userUpdateDTO.getNewEmail());
        user.setPassword(passwordEncoder.encode(userUpdateDTO.getNewPassword()));
        user.setPhoneNumber(userUpdateDTO.getNewPhoneNumber());
        userRepository.save(user);
        return userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException("update fail"));
    }

    public String toggleUserActivity(String username) {
        if(!userRepository.existsByUsername(username)) {
            return "user does not exist";
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setActive(!user.isActive());
        userRepository.save(user);
        if(user.isActive()) {
            return "user is activated";
        }
        return "user is deactivated";
    }
}
