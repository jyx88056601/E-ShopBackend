package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.model.User;
import com.jyx.eshopbackend.persistence.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AdminService.class);
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("Admin service starts");
    }

    public void removeAllUsers(){
        logger.info("Admin service: removeAllUsers()");
        userRepository.deleteAll();
    }

    public Optional<String> removeUserByUsername(String username) {
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

    public Optional<String> fetchAllUsers() {
        List<User> users = userRepository.findAll();
        StringBuilder sb = new StringBuilder();
        for (var user : users) {
            sb.append(user.getId()).append(" ").append(user.getUsername()).append(" ").append(user.getRole()).append(" ").append(user.getRegistrationTime()).append(" ").append(user.getEmail()).append("\n");
        }
        return Optional.of(sb.toString());
    }
}
