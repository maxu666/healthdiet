package cn.edu.ncut.cs.springboot.employeemanagesystem1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/main")
    public String main(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("authorities", authentication.getAuthorities());
            model.addAttribute("isAdmin", authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SYSTEM_ADMIN")));
            model.addAttribute("isHR", authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_HR")));
        }
        return "main";
    }
} 