package com.jyx.eshopbackend.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/")
    public String indexPage() {
        return "This is the main page";
    }

}
