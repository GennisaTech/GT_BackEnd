package com.gennisateam.cheatertavern.LoginByJWT.Config;

import com.gennisateam.cheatertavern.LoginByJWT.Service.UserAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "com.gennisateam.cheatertavern.LoginByJWT")
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthService userAuthService;

    public SecurityConfig(UserAuthService userDetailsService) {
        this.userAuthService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 定义安全过滤规则的核心接口(例如：/api/login/auth)【登录、注册】
        http.csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF 保护
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/login").permitAll() // 允许所有人访问登录接口
                        .requestMatchers("/api/user/register").permitAll() // 允许所有人访问注册接口
                        .anyRequest().authenticated() // 其他请求需要认证
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 设置为无状态会话

        return http.build();
    }

    @Bean
    // 用于用户认证
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userAuthService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    // 对密码进行加密和验证
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 这个实例可以在整个应用中共享
    }

    @Bean
    // 用于协调认证过程
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://bar.gennisa.tech", "https://gennisa.tech", "https://www.gennisa.tech") // 允许具体的子域名
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);

            }
        };
    }
}
