package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.UserSignupDTO;
import com.jyx.eshopbackend.dto.UserResponseDTO;
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

    public Optional<UserResponseDTO> registerUser(UserSignupDTO userSignupDTO) {
        logger.info("UserSignUpService.registerUser");
        if (userRepository.existsByUsername(userSignupDTO.getUsername()) || userRepository.existsByEmail(userSignupDTO.getEmail())) {
            logger.info("found same user in database");
            return Optional.empty();
        }

        User user = new User();
        user.setUsername(userSignupDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userSignupDTO.getPassword()));
        user.setEmail(userSignupDTO.getEmail());
        user.setPhoneNumber(userSignupDTO.getPhoneNumber());
        user.setRole(userSignupDTO.getRole());
        user.setActive(true);
        userRepository.save(user);
        return Optional.of(new UserResponseDTO(user));
    }
 }
