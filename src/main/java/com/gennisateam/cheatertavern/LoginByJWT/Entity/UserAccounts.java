package com.gennisateam.cheatertavern.LoginByJWT.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@Entity
@Table(name = "user_accounts")
@Builder
public class UserAccounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    protected UserAccounts() {

    }

    public void setPassword(String plaintextPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(plaintextPassword);
    }
    // 其他属性、构造函数等
}
