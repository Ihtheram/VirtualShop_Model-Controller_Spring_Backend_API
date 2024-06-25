package com.revature.virtualshop.repositories;

import com.revature.virtualshop.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountDAO extends JpaRepository<Account, Long> {
    Optional<Account> getReferenceByUsername(String username);
}
