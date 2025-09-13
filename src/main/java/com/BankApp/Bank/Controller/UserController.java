package com.BankApp.Bank.Controller;

import com.BankApp.Bank.Entity.User;
import com.BankApp.Bank.Repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ADMIN')") // Requires token with ROLE_ADMIN
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Will automatically be serialized as JSON
    }
}
