package com.gennisateam.cheatertavern.LoginByJWT.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_accounts")
public class UserAccounts {
    @Id
    @Getter
    @Column(name = "player_id",unique = true,nullable = false)
    private String id;  // 这个字段即为数据库中的player_id 类似与UUID

    @Setter
    @Getter
    @Column(name = "account")
    private String username;

    @Setter
    @Getter
    @Column(name = "password")
    private String password;

    @Getter
    @Column(name = "registration_time")
    private LocalDateTime registrationTime; // 使用 LocalDateTime 类型

    protected UserAccounts() {

    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public void setRegistrationTime() {
        this.registrationTime = LocalDateTime.now();
    }
    // 其他属性、构造函数等
}
