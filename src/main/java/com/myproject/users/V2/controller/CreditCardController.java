package com.myproject.users.V2.controller;

import com.myproject.users.V2.entity.CreditCardEntity;
import com.myproject.users.V2.repository.CreditCardRepository;
import com.myproject.users.V2.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CreditCardController {

    private final CreditCardRepository repo;
    private final TokenService tokenService;

    public CreditCardController(CreditCardRepository repo, TokenService tokenService) {
        this.repo = repo;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<?> addCard(@RequestHeader("Authorization") String token,
                                     @RequestBody CreditCardEntity card) {
        String username = tokenService.getUsernameFromToken(token);
        card.setUsername(username);
        repo.save(card);
        return ResponseEntity.ok("Card saved successfully");
    }

    @GetMapping
    public ResponseEntity<List<CreditCardEntity>> getCards(@RequestHeader("Authorization") String token) {
        String username = tokenService.getUsernameFromToken(token);
        List<CreditCardEntity> cards = repo.findByUsername(username);
        return ResponseEntity.ok(cards);
    }
}