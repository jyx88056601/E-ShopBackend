package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.dto.UserResponseDTO;
import com.jyx.eshopbackend.dto.UserSignupDTO;
import com.jyx.eshopbackend.exception.UserNotStoredException;
import com.jyx.eshopbackend.service.UserSignInService;
import com.jyx.eshopbackend.service.UserSignUpService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;



@RestController
public class DefaultController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DefaultController.class);

    private final UserSignUpService userSignUpService;

    private final UserSignInService userSignInService;


    public DefaultController(UserSignUpService userSignUpService, UserSignInService userSignInService) {
        this.userSignUpService = userSignUpService;
        this.userSignInService = userSignInService;
    }

    @GetMapping("/")
    public String indexPage() {
        return "This is the main page";
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody UserSignupDTO userSignupDTO) {
        logger.info("Default controller/signup");
        Optional<UserResponseDTO> registeredUser;
        try {
            registeredUser = userSignUpService.registerUser(userSignupDTO);
        } catch (Exception e) {
           if (e instanceof UserNotStoredException) {
               logger.info(e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
           } else {
               logger.info(e.getMessage());
               return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
           }
        }
        logger.info("user has been registered successfully");
        return ResponseEntity.status(HttpStatus.OK).body(registeredUser.get());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login() {
        // username and password are correct then jwt token should be return to user
        String [] response = userSignInService.generateToken().split(" ");
        return ResponseEntity.ok(Map.of("status",HttpStatus.OK, "role", response[0],"token" , response[1], "id",response[2],"username",response[3]));
    }

//    @PostMapping("/logout")
//    public  ResponseEntity<Object> logout() {
//        return ResponseEntity.ok("logged out ");
//    }


    @GetMapping("/auth")
    public String authorized (){
        return "some secret";
    }

}
