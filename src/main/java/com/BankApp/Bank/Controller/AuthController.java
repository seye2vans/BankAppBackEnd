package com.BankApp.Bank.Controller;

import com.BankApp.Bank.DTOs.LoginRequest;
import com.BankApp.Bank.DTOs.SigninRequest;
import com.BankApp.Bank.Entity.User;
import com.BankApp.Bank.Repositories.UserRepository;
import com.BankApp.Bank.Service.JwtUtil;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private UserRepository  userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signuo(@RequestBody SigninRequest req){
        if(userRepo.existsByEmail(req.getEmail())){
            return ResponseEntity.badRequest().body("Email already in use");

        }
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepo.save(user);

        String token = jwtUtil.generateAccessToken(user.getEmail());
        return ResponseEntity.ok(Map.of("accessToken", token));

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        }
        String token = jwtUtil.generateAccessToken(user.getEmail());
        return ResponseEntity.ok(Map.of("access Token",token));
    }

}
