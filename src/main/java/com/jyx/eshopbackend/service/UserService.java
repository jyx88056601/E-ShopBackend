package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.model.User;
import com.jyx.eshopbackend.persistence.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<Long> findUserIdByUsername(String username) {
        User user;
        try {
            user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(user.getId());
    }
}
