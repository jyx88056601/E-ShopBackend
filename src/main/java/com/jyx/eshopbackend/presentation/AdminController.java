package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.dto.UserResponseDTO;
import com.jyx.eshopbackend.dto.UserUpdateDTO;
import com.jyx.eshopbackend.exception.PasswordNotMatchException;
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
    public ResponseEntity<Object> administrationPage() {
        logger.info("/admin/");
        logger.info("AdminController.class: administrationPage()");
        return ResponseEntity.ok("Admin-panel");
    }

    // user control
    @GetMapping("/display-all-users")
    public ResponseEntity<Object> displayAllData(){
        logger.info("/admin/display-all-users");
        logger.info("AdminController.class: displayAllData()");
        Optional<List<User>> users = adminService.fetchAllUsers();
        if(users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<UserResponseDTO> userDTOS = new ArrayList<>();
        for(var user : users.get()) {
            userDTOS.add(new UserResponseDTO(user));
        }
        return ResponseEntity.ok(Map.of("status",HttpStatus.OK,"userDTOS", userDTOS));
    }

    @GetMapping("/userinfo/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable String id) {
        logger.info("/admin/userinfo/"+id);
        logger.info("AdminController.class: findUserById()");
        Optional<User> userOptional= adminService.fetchAUser(Long.valueOf(id));
        if(userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var userDTO = new UserResponseDTO(userOptional.get());
        return ResponseEntity.ok(Map.of("status",HttpStatus.OK,"userDTO", userDTO));
    }

    @DeleteMapping("/delete-all-users")
    public ResponseEntity<String> removeAllUsers(){
        logger.info("/admin/delete-all-users");
        logger.info("AdminController.class: removeAllUsers()");
        return ResponseEntity.status(HttpStatus.OK).body(adminService.removeAllUsers());
    }

    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        logger.info("/admin/delete-user/" + username);
        logger.info("AdminController.class: deleteUser()");
        // ex: localhost:8080/admin/delete-user/username
        Optional<String> response;
        try {
            response = adminService.removeUserByUsername(username);
        } catch (Exception e) {
            if (e instanceof UsernameNotFoundException) {
                logger.info("User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.get());
    }

    @DeleteMapping("/delete-all-products")
    public ResponseEntity<String> removeAllProducts(){
        logger.info("/admin/delete-all-products");
        logger.info("AdminController.class:  removeAllProducts()");
        return ResponseEntity.status(HttpStatus.OK).body(adminService.deleteAllProduct());
    }

    @PutMapping("/update-user")
    public ResponseEntity<Object> updateUser(@RequestBody UserUpdateDTO userUpdateDTO)  {
        logger.info("/update-user/"+userUpdateDTO.getUsername());
        logger.info("AdminController.class: updateUser()");
        Optional<UserResponseDTO> updateUser;
        try {
            updateUser = adminService.updateUser(userUpdateDTO);
        } catch (Exception e) {
            if (e instanceof UsernameNotFoundException) {
                logger.info(e.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else if (e instanceof PasswordNotMatchException) {
                logger.info(e.getMessage());
                return ResponseEntity.badRequest().body(e.getMessage());
            } else {
                logger.error(e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("AdminController.class: updateUser()");
            }
        }
        logger.info("user info has been updated");
        return ResponseEntity.ok(Map.of("status", HttpStatus.OK,"data",updateUser.get()));
    }


    @PutMapping("/set-up-activity/{username}")
    public ResponseEntity<String> toggleActivity(@PathVariable String username) {
        logger.info("/set-up-activity" + username);
        logger.info("AdminController.class: toggleActivity()");
        Optional<String> response = adminService.toggleUserActivity(username);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    // order control
//    @GetMapping("/user/{username}/orders")
//    public ResponseEntity<Object> findOrdersByUsername(@PathVariable String username) {
//        logger.info("/admin/user/"+username+"/orders");
//        logger.info("AdminController.class: findOrdersByUsername()");
//        Optional<List<Order>> orderList = adminService.findOrdersByUsername(username);
//        List<OrderDTO> orderDTOS =  new ArrayList<>();
//        if (orderList.isEmpty()) return ResponseEntity.noContent().build();
//                ResponseEntity.ok(Map.of("status",HttpStatus.,"data", orderList));
//       for(Order order : orderList.get()) {
//          orderDTOS.add(new OrderDTO(order));
//       }
//        return ResponseEntity.ok(Map.of("status", HttpStatus.OK,"data",orderDTOS));
//        HttpHeaders headers = new HttpHeaders();
//        // 总条目数
//        headers.add("X-Total-Count", "100");
//        // 调试信息
//        headers.add("Debug-Info", "Processed in 25ms");
//       return ResponseEntity.status(HttpStatus.OK).headers(headers).body(orderDTOS);
//    }
}
