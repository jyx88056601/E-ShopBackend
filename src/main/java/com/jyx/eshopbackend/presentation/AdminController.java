package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.service.AdminService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return "Admin-panel";
    }

    @GetMapping("/display-all-users")
    public ResponseEntity<String> displayAllData(){
        Optional<String> users = adminService.fetchAllUsers();
        if(users.isEmpty() || users.get().isEmpty()) {
            return  ResponseEntity.ok("No users in the database");
        }
        return ResponseEntity.ok(users.get());
    }

    @DeleteMapping("/delete-all-users")
    public ResponseEntity<String> removeAllUsers(){
        adminService.removeAllUsers();
        return ResponseEntity.ok("All users have been removed from database");
    }

    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        // ex: localhost:8080/admin/delete-user/username
        Optional<String> deletedUser = adminService.removeUserByUsername(username);
        return deletedUser.map(s -> ResponseEntity.ok("User with username " + s + " deleted successfully.")).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User with username " + username + " not found."));
    }

}
