package com.gennisateam.cheatertavern.LoginByJWT.DAO;

import com.gennisateam.cheatertavern.LoginByJWT.Entity.UserAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthDAO extends JpaRepository<UserAccounts, String> {
    Optional<UserAccounts> findByUsername(String username);
}