package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.dto.OrderDTO;
import com.jyx.eshopbackend.dto.UserResponseDTO;
import com.jyx.eshopbackend.dto.UserUpdateDTO;
import com.jyx.eshopbackend.exception.PasswordNotMatchException;
import com.jyx.eshopbackend.model.Order;
import com.jyx.eshopbackend.model.User;
import com.jyx.eshopbackend.service.AdminService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String administrationPage() {
        logger.info("/admin/");
        logger.info("AdminController.class: administrationPage()");
        return "Admin-panel";
    }

    // user control
    @GetMapping("/display-all-users")
    public ResponseEntity<Object> displayAllData(){
        logger.info("/admin/display-all-users");
        logger.info("AdminController.class: displayAllData()");
        List<User> users = adminService.fetchAllUsers();
        if(users.isEmpty()) {
            return  ResponseEntity.ok("No users in the database");
        }
        List<UserResponseDTO> userDTOS = new ArrayList<>();
        for(var user : users) {
            userDTOS.add(new UserResponseDTO(user));
        }
        return ResponseEntity.ok(userDTOS);
       // return ResponseEntity.ok(Map.of("status","success","Users:", userDTOS));
    }

    @DeleteMapping("/delete-all-users")
    public ResponseEntity<String> removeAllUsers(){
        logger.info("/admin/delete-all-users");
        logger.info("AdminController.class: removeAllUsers()");
        adminService.removeAllUsers();
        return ResponseEntity.ok("All users have been removed from database");
    }

    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        logger.info("/admin//delete-user/" + username);
        logger.info("AdminController.class: deleteUser()");
        // ex: localhost:8080/admin/delete-user/username
        Optional<String> deletedUser = adminService.removeUserByUsername(username);
        return deletedUser.map(target -> ResponseEntity.ok("User with username " + target + " deleted successfully.")).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User with username " + username + " not found."));
    }

    @PutMapping("/update-user")
    public ResponseEntity<Object> updateUser(@RequestBody UserUpdateDTO userUpdateDTO)  {
        logger.info("/update-user/"+userUpdateDTO.getUsername());
        logger.info("AdminController.class: updateUser()");
        User updateUser;
        try {
            updateUser = adminService.updateUser(userUpdateDTO);
        } catch (Exception e) {
            if (e instanceof UsernameNotFoundException) {
                return ResponseEntity.badRequest().body("Invalid user update data.");
            } else if (e instanceof PasswordNotMatchException) {
                return ResponseEntity.badRequest().body("Original password is incorrect.");
            } else {
                logger.error("unexpected error from AdminController.class: updateUser()");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("AdminController.class: updateUser()");
            }
        }
        return ResponseEntity.ok(Map.of("status","success","data",new UserResponseDTO(updateUser)));
    }


    @PutMapping("/set-up-activity/{username}")
    public ResponseEntity<String> toggleActivity(@PathVariable String username) {
        logger.info("//set-up-activity" + username);
        logger.info("AdminController.class: toggleActivity()");
           return  ResponseEntity.ok(adminService.toggleUserActivity(username));
    }


    // order control
    @GetMapping("/user/{username}/orders")
    public ResponseEntity<Object> findOrdersByUsername(@PathVariable String username) {
        logger.info("/admin/user/"+username+"/orders");
        logger.info("AdminController.class: findOrdersByUsername()");
        Optional<List<Order>> orderList = adminService.findOrdersByUsername(username);
        List<OrderDTO> orderDTOS =  new ArrayList<>();
        if (orderList.isEmpty()) return ResponseEntity.ok(Map.of("status","Empty list","data",orderList));
       for(Order order : orderList.get()) {
          orderDTOS.add(new OrderDTO(order));
       }
        return ResponseEntity.ok(Map.of("status","success","data",orderDTOS));
    }
}
