package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.UserResponseDTO;
import com.jyx.eshopbackend.dto.UserUpdateDTO;
import com.jyx.eshopbackend.exception.PasswordNotMatchException;
import com.jyx.eshopbackend.exception.UserDeletionFailedException;
import com.jyx.eshopbackend.model.Order;
import com.jyx.eshopbackend.model.User;
import com.jyx.eshopbackend.persistence.OrderRepository;
import com.jyx.eshopbackend.persistence.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminService(UserService userService, UserRepository userRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<List<Order>> findOrdersByUsername(String username) {
        Optional<Long> id = userService.findUserIdByUsername(username);
        if(id.isEmpty()) {
            return Optional.empty();
        }
        return orderRepository.findOrdersByUser_Id(id.get());
    }


    public String removeAllUsers() {
        userRepository.deleteAll();
        return "All users have been removed";
    }

    public Optional<String> removeUserByUsername(String username) throws Exception {
        Optional<User> user  = userRepository.findByUsername(username);
        if(user.isEmpty()) throw new UsernameNotFoundException("User does not exist");
        userRepository.deleteById(user.get().getId());
        if (userRepository.existsById(user.get().getId())) {
            throw new UserDeletionFailedException("Deleting " +  username +" failed");
        }
        return Optional.of("User " + username + " has been deleted");
    }

    public Optional<List<User>> fetchAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) return Optional.empty();
        return Optional.of(users);
    }

    public Optional<UserResponseDTO> updateUser(UserUpdateDTO userUpdateDTO) throws Exception {
        Optional<User> preUpdateUser = userRepository.findByUsername(userUpdateDTO.getUsername());
        if(preUpdateUser.isEmpty())  {
            throw new UsernameNotFoundException("User does not exist");
        }
        if(!passwordEncoder.matches(userUpdateDTO.getOldPassword(), preUpdateUser.get().getPassword())) {
           throw new PasswordNotMatchException("Password does not match");
        }
        var user = preUpdateUser.get();
        user.setUsername(userUpdateDTO.getNewUsername());
        user.setEmail(userUpdateDTO.getNewEmail());
        user.setPassword(passwordEncoder.encode(userUpdateDTO.getNewPassword()));
        user.setPhoneNumber(userUpdateDTO.getNewPhoneNumber());
        userRepository.save(user);
        User updatedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Updated user does not find"));
        return Optional.of(new UserResponseDTO(updatedUser));
    }

    public Optional<String> toggleUserActivity(String username) {
        Optional<User> origin = userRepository.findByUsername(username);
        if(origin.isEmpty()) {
            return Optional.empty();
        }
        var user = origin.get();
        boolean isActive = user.isActive();
        user.setActive(!isActive);
        userRepository.save(user);
        if(user.isActive()) {
            return Optional.of(username + " is now activated");
        }
        return Optional.of(username + " is deactivated");
    }
}
