package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.model.User;
import com.jyx.eshopbackend.persistence.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/delete-all-users")
    public  ResponseEntity<String> clearDatabase(){
        userRepository.deleteAll();
        return ResponseEntity.ok("All users have been remove from database");
    }

    @GetMapping("/show-all-users")
    public ResponseEntity<String> displayAllData(){
       List<User> list = userRepository.findAll();
       StringBuilder sb = new StringBuilder();
       for(User user : list) {
           sb.append(user.getId()).append(" ").append(user.getUsername()).append(" ").append(user.getRegistrationTime()).append(" ").append(user.getRole());
       }
       return ResponseEntity.ok(sb.toString());
    }

}
