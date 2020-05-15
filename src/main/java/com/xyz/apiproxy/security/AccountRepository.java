package com.xyz.apiproxy.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xyz.apiproxy.security.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
   Optional<Account> findByUsername(String username);

}
