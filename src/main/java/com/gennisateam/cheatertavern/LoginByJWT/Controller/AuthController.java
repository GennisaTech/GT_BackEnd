package com.gennisateam.cheatertavern.LoginByJWT.Controller;

import com.gennisateam.cheatertavern.LoginByJWT.Config.*;
import com.gennisateam.cheatertavern.LoginByJWT.Entity.UserAccounts;
import com.gennisateam.cheatertavern.LoginByJWT.Utils.JWTUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/login")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/auth")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserAccounts user) {
        Map<String, Object> response = new HashMap<>();
        UserAccounts foundUser = userRepository.findByUsername(user.getUsername()).orElse(null);

        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            String token = jwtUtil.generateToken(foundUser.getUsername());
            response.put("code", "SUCCESS");
            response.put("data", Map.of("token", token));
            return ResponseEntity.ok(response);
        } else {
            response.put("code", "FAILURE");
            response.put("errMsg", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }
}
