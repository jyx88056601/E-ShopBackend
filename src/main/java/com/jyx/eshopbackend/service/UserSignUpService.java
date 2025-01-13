package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.UserDTO;
import com.jyx.eshopbackend.model.User;
import com.jyx.eshopbackend.persistence.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSignUpService {

   private final UserRepository userRepository;

   private final PasswordEncoder passwordEncoder;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserSignUpService.class);
    public UserSignUpService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        logger.info("Create UserSignUpService object");
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserDTO> registerUser(UserDTO userDTO) {
        logger.info("UserSignUpService.registerUser");
        if (userRepository.existsByUsername(userDTO.getUsername()) || userRepository.existsByEmail(userDTO.getEmail())) {
            logger.info("found same user in database");
            return Optional.empty();
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        userDTO.setPassword(user.getPassword());
        user.setRole(userDTO.getRole());
        userRepository.save(user);
        return Optional.of(userDTO);
    }
 }
