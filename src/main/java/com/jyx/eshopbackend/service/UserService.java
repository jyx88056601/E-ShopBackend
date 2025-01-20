package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<Long> findUserIdByUsername(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
           return Optional.empty();
        }
        return Optional.of(userRepository.findByUsername(username).get().getId());
    }
}
