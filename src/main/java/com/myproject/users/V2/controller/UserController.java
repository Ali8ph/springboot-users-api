package com.myproject.users.V2.controller;

import com.myproject.users.V2.entity.UserEntity;
import com.myproject.users.V2.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserRepository repo;
    private final String secret = "my-visible-secret-key-my-visible-secret-key";

    public UserController(UserRepository repo) { this.repo = repo; }

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
                .map(user -> {
                    Key key = Keys.hmacShaKeyFor(secret.getBytes());
                    String token = Jwts.builder()
                            .setSubject(user.getUsername())
                            .setIssuedAt(new Date())
                            .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                            .signWith(key, SignatureAlgorithm.HS256)
                            .compact();
                    return ResponseEntity.ok(token);
                })
                .orElse(ResponseEntity.status(401).body("invalid"));
    }
}