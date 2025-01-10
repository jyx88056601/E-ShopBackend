package com.jyx.eshopbackend.presentation;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/")
    @CrossOrigin(origins = "http://localhost:5174")
    public String indexPage() {
        return "This is the main page";
    }

}
