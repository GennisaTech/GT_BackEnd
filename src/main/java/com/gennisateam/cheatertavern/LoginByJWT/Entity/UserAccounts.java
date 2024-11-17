package com.gennisateam.cheatertavern.LoginByJWT.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_accounts")
public class UserAccounts {
    @Id
    @Column(name = "player_id",unique = true,nullable = false)
    private String id;  // 这个字段即为数据库中的player_id 类似与UUID

    @Setter
    @Column(name = "account")
    private String username;

    @Setter
    @Column(name = "password")
    private String password;

    @Column(name = "registration_time")
    private LocalDateTime registrationTime; // 使用 LocalDateTime 类型

    protected UserAccounts() {

    }

    public String getPassword() {
        return password;
    }

    // Getter 和 Setter 方法
    public String getId() {
        return id; // id即为player_id
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime() {
        this.registrationTime = LocalDateTime.now();
    }
    // 其他属性、构造函数等
}
