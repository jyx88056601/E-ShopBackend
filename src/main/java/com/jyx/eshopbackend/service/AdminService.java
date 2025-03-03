package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.dto.UserResponseDTO;
import com.jyx.eshopbackend.dto.UserUpdateDTO;
import com.jyx.eshopbackend.exception.PasswordNotMatchException;
import com.jyx.eshopbackend.exception.UserDeletionFailedException;
import com.jyx.eshopbackend.model.User;
import com.jyx.eshopbackend.persistence.OrderRepository;
import com.jyx.eshopbackend.persistence.PaymentRepository;
import com.jyx.eshopbackend.persistence.ProductRepository;
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

    private final ProductRepository productRepository;

    private final PaymentRepository paymentRepository;

    public AdminService(UserService userService, UserRepository userRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder, ProductRepository productRepository, PaymentRepository paymentRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;

        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
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

    public Optional<User> fetchAUser(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserResponseDTO> updateUser(UserUpdateDTO userUpdateDTO) throws PasswordNotMatchException {
        Optional<User> preUpdateUser = userRepository.findById(userUpdateDTO.getId());
        if(preUpdateUser.isEmpty())  {
            throw new UsernameNotFoundException("User does not exist");
        }
        // administrator can modify password without matching the old password
//        if(!passwordEncoder.matches(userUpdateDTO.getOldPassword(), preUpdateUser.get().getPassword())) {
//           throw new PasswordNotMatchException("Password does not match");
//        }
        if (!userUpdateDTO.getOldPassword().equals(userUpdateDTO.getNewPassword())) {
            throw new PasswordNotMatchException("input two different passwords");
        }
        var user = preUpdateUser.get();
        user.setUsername(userUpdateDTO.getNewUsername());
        user.setEmail(userUpdateDTO.getNewEmail());
        if(!userUpdateDTO.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getNewPassword()));
        }
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


    public String deleteAllProduct() {
         productRepository.deleteAll();
         return "Products from database have been cleared";
    }

    public void deleteAllPayments() {
        paymentRepository.deleteAll();
    }
}
