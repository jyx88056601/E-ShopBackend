package com.jyx.eshopbackend.presentation;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public String errorPage() {
        return "This is the error page";
    }
}
