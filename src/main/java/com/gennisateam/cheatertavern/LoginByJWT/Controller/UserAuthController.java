package com.gennisateam.cheatertavern.LoginByJWT.Controller;

import com.gennisateam.cheatertavern.LoginByJWT.DAO.UserAuthDAO;
import com.gennisateam.cheatertavern.LoginByJWT.Entity.UserAccounts;
import com.gennisateam.cheatertavern.LoginByJWT.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/login")
public class UserAuthController {

    @Autowired
    private UserAuthDAO userAuthDAO;
    @Autowired
    private JWTUtils jwtUtil;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/auth")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserAccounts user) {
        Map<String, Object> response = new HashMap<>();

        // 账号为空的情况，返回400 Bad Request
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            response.put("code", "ACCOUNT_EMPTY");
            response.put("errMsg", "Account cannot be empty");
            return ResponseEntity.badRequest().body(response);
        }

        UserAccounts foundUser = userAuthDAO.findByUsername(user.getUsername()).orElse(null);

        // 账号不存在的情况，返回404 Not Found
        if (foundUser == null) {
            response.put("code", "ACCOUNT_NOT_FOUND");
            response.put("errMsg", "Account does not exist");
            return ResponseEntity.status(404).body(response);
        }

        // 密码错误的情况，返回401 Unauthorized
        if (!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            response.put("code", "PASSWORD_ERROR");
            response.put("errMsg", "Invalid password");
            return ResponseEntity.status(401).body(response);
        }

        String token = jwtUtil.generateToken(foundUser.getUsername());
        response.put("code", "SUCCESS");
        response.put("data", Map.of("token", token));

        // 创建Cookie对象
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        // 将Cookie添加到响应中并返回200 OK
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response);
    }
}
