package com.portfoliosass.controller;

import com.portfoliosass.model.User;
import com.portfoliosass.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping
    public String adminPanel(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<User> users = userRepository.findAll();

        long activeUsers = users.stream().filter(User::isEnabled).count();
        long adminCount = users.stream()
                .filter(u -> u.getRole() == User.Role.ROLE_ADMIN).count();

        model.addAttribute("users", users);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("adminCount", adminCount);

        return "admin/index";
    }
}
