package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Controller
public class hello{
    @RequestMapping("/")
    public String helloWorld() {
        return "Hello Spring Boot, I am maxu";
    }
}




