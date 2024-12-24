package cn.edu.ncut.cs.springboot.employeemanagesystem1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouterController {
    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    @RequestMapping("/")
    public String index() {
        return "login";
    }
}
