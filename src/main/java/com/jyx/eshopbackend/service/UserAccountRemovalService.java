package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.LoginDTO;
import com.jyx.eshopbackend.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountRemovalService {
    private final UserRepository userRepository;

    public UserAccountRemovalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<String> removeAccount(LoginDTO loginDTO) {
        var user = userRepository.findByUsername(loginDTO.getUsername());
        if (user.isEmpty()) {
            return Optional.empty();
        }
        userRepository.deleteById(user.get().getId());
        return Optional.of(loginDTO.getUsername() + " has been remove from database");
    }
}
