package com.gennisateam.cheatertavern.LoginByJWT.Controller;

import com.gennisateam.cheatertavern.LoginByJWT.DAO.UserAuthDAO;
import com.gennisateam.cheatertavern.LoginByJWT.Entity.UserAccounts;
import com.gennisateam.cheatertavern.LoginByJWT.Utils.JWTUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserAuthController {

    @Resource
    private UserAuthDAO userAuthDAO;
    @Resource
    private JWTUtils jwtUtil;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 登录接口
     * @param user 请求体user
     * @return 返回cookies及请求体
     */
    @PostMapping("/login")
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // 密码错误的情况，返回401 Unauthorized
        if (!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            response.put("code", "PASSWORD_ERROR");
            response.put("errMsg", "Invalid password");
            return ResponseEntity.status(401).body(response);
        }

        // 使用JWT生成token并返回
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

    /**
     * 注册接口
     * @param newUser 新用户请求体
     * @return 注册信息
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserAccounts newUser) {
        Map<String, Object> response = new HashMap<>();

        // 输入帐号为空
        if (newUser.getUsername() == null || newUser.getUsername().isEmpty()) {
            response.put("code", "ACCOUNT_EMPTY");
            response.put("errMsg", "Account cannot be empty");
            return ResponseEntity.badRequest().body(response);
        }

        // 检查账号是否已存在
        if (userAuthDAO.findByUsername(newUser.getUsername()).isPresent()) {
            response.put("code", "ACCOUNT_EXISTS");
            response.put("errMsg", "Account already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // Conflict
        }

        // 加密密码
        String encryptedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encryptedPassword);

        // 生成唯一的 player_id
        newUser.setId();

        // 设置注册时间为当前时间
        newUser.setRegistrationTime();

        // 保存新用户到数据库
        UserAccounts savedUser = userAuthDAO.save(newUser);

        response.put("code", "SUCCESS");
        response.put("data", Map.of("playerId", savedUser.getId())); // 使用id作为player_id

        return ResponseEntity.status(201).body(response); // 201 Created
    }

}
