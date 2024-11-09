package com.gennisateam.cheatertavern.LoginByJWT.Config;

import com.gennisateam.cheatertavern.LoginByJWT.Entity.UserAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccounts, Long> {
    Optional<UserAccounts> findByUsername(String username);
}
