package com.myproject.users.V2.controller;

import com.myproject.users.V2.entity.UserEntity;
import com.myproject.users.V2.repository.UserRepository;
import com.myproject.users.V2.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserRepository repo;
    private final TokenService tokenService;

    public UserController(UserRepository repo, TokenService tokenService) {
        this.repo = repo;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity u) {
        if (repo.findByUsername(u.getUsername()).isPresent())
            return ResponseEntity.badRequest().body("username exists");
        repo.save(u);
        return ResponseEntity.ok("registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity u) {
        return repo.findByUsername(u.getUsername())
                .filter(user -> user.getPassword().equals(u.getPassword()))
                .map(user -> ResponseEntity.ok(tokenService.generateToken(user.getUsername())))
                .orElse(ResponseEntity.status(401).body("invalid"));
    }
}