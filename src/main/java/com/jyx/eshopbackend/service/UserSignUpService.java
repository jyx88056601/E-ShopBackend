package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.UserResponseDTO;
import com.jyx.eshopbackend.dto.UserSignupDTO;
import com.jyx.eshopbackend.exception.DupliateUserException;
import com.jyx.eshopbackend.exception.UserNotStoredException;
import com.jyx.eshopbackend.model.Cart;
import com.jyx.eshopbackend.model.User;
import com.jyx.eshopbackend.persistence.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSignUpService {

   private final UserRepository userRepository;

   private final PasswordEncoder passwordEncoder;

   private final CartService cartService;

    public UserSignUpService(UserRepository userRepository, PasswordEncoder passwordEncoder, CartService cartService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
    }

    public Optional<UserResponseDTO> registerUser(UserSignupDTO userSignupDTO) throws Exception {
        if (userRepository.existsByUsername(userSignupDTO.getUsername())) {
           throw new DupliateUserException("This username is already registered.");
        }
        if(userRepository.existsByEmail(userSignupDTO.getEmail())) {
            throw new DupliateUserException("This email address has already been registered by another user.");
        }
        User user = new User();
        user.setUsername(userSignupDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userSignupDTO.getPassword()));
        user.setEmail(userSignupDTO.getEmail());
        user.setPhoneNumber(userSignupDTO.getPhoneNumber());
        user.setRole(userSignupDTO.getRole());
        user.setActive(true);
        userRepository.save(user);
        User storedUser = userRepository.findByUsername(userSignupDTO.getUsername()).orElseThrow(() -> new UserNotStoredException("Unexpected error, user does not exist in the database"));
        Cart cart = new Cart();
        cart.setUser(storedUser);
        cartService.initializeCart(cart);
        return Optional.of(new UserResponseDTO(storedUser));
    }
 }
