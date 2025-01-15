package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.dto.UserLoginDTO;
import com.jyx.eshopbackend.dto.UserResponseDTO;
import com.jyx.eshopbackend.dto.UserSignupDTO;
import com.jyx.eshopbackend.security.authenticationprovider.UserAuthenticationProvider;
import com.jyx.eshopbackend.security.jwtservice.JwtUtil;
import com.jyx.eshopbackend.service.UserAccountRemovalService;
import com.jyx.eshopbackend.service.UserPrincipalService;
import com.jyx.eshopbackend.service.UserSignInService;
import com.jyx.eshopbackend.service.UserSignUpService;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;



@RestController
//@CrossOrigin(origins = "http://localhost:5174")
public class Controller {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Controller.class);

    private final JwtUtil jwt;

    private final UserSignUpService userSignUpService;

    private final UserSignInService userSignInService;

    private final UserPrincipalService userPrincipalService;

    private final UserAccountRemovalService userAccountRemovalService;

    public Controller(UserSignUpService userSignUpService, UserSignInService userSignInService, UserDetailsService userDetailsService, UserAuthenticationProvider userAuthenticationProvider, JwtUtil jwt, UserPrincipalService userPrincipalService, UserAccountRemovalService userAccountRemovalService ) {
        this.userSignUpService = userSignUpService;
        this.userSignInService = userSignInService;
        this.jwt = jwt;
        this.userPrincipalService = userPrincipalService;
        this.userAccountRemovalService = userAccountRemovalService;
    }

    @GetMapping("/")
    public String indexPage() {
        return "This is the main page";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignupDTO userSignupDTO) {
        logger.info("/signup API");
            Optional<UserResponseDTO> registeredUser = userSignUpService.registerUser(userSignupDTO);
            if (registeredUser.isEmpty()) {
                logger.info("registration failed");
                return ResponseEntity.badRequest().body("User already exists, please log in");
            }
        logger.info("registration succeeded and the return user info is :" + registeredUser);
            return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        // username and password are correct then jwt token should be return to user
        return ResponseEntity.ok(userSignInService.generateToken());
    }

    @GetMapping("/auth")
    public String authorized (){
        return "some secret";
    }

    @PostMapping("/remove-account")
    public ResponseEntity<String> removeAccount(@RequestBody UserLoginDTO userLoginDTO) {
        Optional<String> result = userAccountRemovalService.removeAccount(userLoginDTO);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body("No account in database that has username : " + userLoginDTO.getUsername()));
    }
}
