package com.myproject.users.V2.repository;

import com.myproject.users.V2.entity.CreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCardEntity, Long> {
    List<CreditCardEntity> findByUsername(String username);
}