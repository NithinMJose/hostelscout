# Implementation Guide for Security Enhancements

This document provides step-by-step code examples for implementing the recommended security enhancements.

---

## 1. Token Refresh Mechanism (Priority 1)

### Step 1: Add Refresh Token Configuration

**File:** `src/main/resources/application.properties`

```properties
# Access token (short-lived)
app.security.jwt.access-token-expiration-minutes=15

# Refresh token (long-lived)
app.security.jwt.refresh-token-expiration-days=7
```

### Step 2: Update JwtService

**File:** `src/main/java/com/hostelscout/hostel/modules/common/security/jwt/JwtService.java`

Add these new methods:

```java
public String generateAccessToken(String subject, Role role) {
    Instant now = Instant.now();
    return JWT.create()
            .withSubject(subject)
            .withClaim("role", role.name())
            .withClaim("type", "access")
            .withJTI(UUID.randomUUID().toString())
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(now.plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES)))
            .sign(getAlgorithm());
}

public String generateRefreshToken(String subject) {
    Instant now = Instant.now();
    return JWT.create()
            .withSubject(subject)
            .withClaim("type", "refresh")
            .withJTI(UUID.randomUUID().toString())
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(now.plus(refreshTokenExpirationDays, ChronoUnit.DAYS)))
            .sign(getAlgorithm());
}

public String validateRefreshToken(String token) throws JWTVerificationException {
    var decodedJWT = JWT.require(getAlgorithm())
            .build()
            .verify(token);
    
    String tokenType = decodedJWT.getClaim("type").asString();
    if (!"refresh".equals(tokenType)) {
        throw new JWTVerificationException("Invalid token type");
    }
    
    return decodedJWT.getSubject();
}
```

### Step 3: Create Token Response DTO

**File:** `src/main/java/com/hostelscout/hostel/modules/admin/dto/TokenResponseDto.java`

```java
package com.hostelscout.hostel.modules.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;  // in seconds (900 for 15 minutes)
    private String tokenType = "Bearer";
}
```

### Step 4: Update Admin Controller

```java
@PostMapping("/login")
public ResponseEntity<?> login(@Valid @RequestBody AdminLoginRequestDto loginRequest) {
    TokenResponseDto response = adminService.authenticateAndGetTokens(
        loginRequest.getUsername(), 
        loginRequest.getPassword()
    );
    return ResponseEntity.ok(response);
}

@PostMapping("/refresh")
public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDto request) {
    TokenResponseDto response = adminService.refreshAccessToken(request.getRefreshToken());
    return ResponseEntity.ok(response);
}
```

### Step 5: Create Refresh Token Request DTO

```java
package com.hostelscout.hostel.modules.admin.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequestDto {
    @NotBlank
    private String refreshToken;
}
```

### Step 6: Update AdminService

```java
@Transactional(readOnly = true)
public TokenResponseDto authenticateAndGetTokens(String username, String rawPassword) {
    Optional<BaseUser> baseUserOpt = baseUserRepository.findByUsername(username);
    BaseUser baseUser = baseUserOpt.orElseThrow(
        () -> new ResourceNotFoundException("Invalid username or password"));

    if (!passwordEncoder.matches(rawPassword, baseUser.getPassword())) {
        throw new ResourceNotFoundException("Invalid username or password");
    }

    if (baseUser.getRole() != Role.ADMIN) {
        throw new ResourceNotFoundException("User is not an admin");
    }

    String accessToken = jwtService.generateAccessToken(baseUser.getUsername(), Role.ADMIN);
    String refreshToken = jwtService.generateRefreshToken(baseUser.getUsername());
    
    logger.info("Tokens generated for user: {}", baseUser.getUsername());

    return TokenResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(15 * 60L)  // 15 minutes in seconds
            .build();
}

public TokenResponseDto refreshAccessToken(String refreshToken) {
    try {
        String username = jwtService.validateRefreshToken(refreshToken);
        BaseUser baseUser = baseUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(username, baseUser.getRole());
        
        logger.info("Access token refreshed for user: {}", username);

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)  // Reuse existing refresh token
                .expiresIn(15 * 60L)
                .build();
    } catch (Exception e) {
        throw new ResourceNotFoundException("Invalid refresh token");
    }
}
```

---

## 2. Audit Logging (Priority 1)

### Step 1: Create Audit Log Entity

**File:** `src/main/java/com/hostelscout/hostel/modules/common/entity/AuthAuditLog.java`

```java
package com.hostelscout.hostel.modules.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auth_audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthEventType eventType;  // LOGIN, LOGIN_FAILED, TOKEN_REFRESH, LOGOUT

    @Column(nullable = false)
    private String ipAddress;

    @Column
    private String userAgent;

    @Column
    private String failureReason;  // Reason for failure

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum AuthEventType {
        LOGIN, LOGIN_FAILED, TOKEN_REFRESH, LOGOUT, TOKEN_INVALID, PASSWORD_CHANGED
    }
}
```

### Step 2: Create Audit Log Repository

```java
package com.hostelscout.hostel.modules.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hostelscout.hostel.modules.common.entity.AuthAuditLog;

import java.util.UUID;

public interface AuthAuditLogRepository extends JpaRepository<AuthAuditLog, UUID> {
}
```

### Step 3: Create Audit Service

**File:** `src/main/java/com/hostelscout/hostel/modules/common/security/audit/AuthAuditService.java`

```java
package com.hostelscout.hostel.modules.common.security.audit;

import com.hostelscout.hostel.modules.common.entity.AuthAuditLog;
import com.hostelscout.hostel.modules.common.repository.AuthAuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class AuthAuditService {
    private static final Logger logger = LogManager.getLogger(AuthAuditService.class);
    private final AuthAuditLogRepository auditLogRepository;

    private String getClientIp() {
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "UNKNOWN";
        }
        HttpServletRequest request = attributes.getRequest();
        
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    private String getUserAgent() {
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "UNKNOWN";
        }
        return attributes.getRequest().getHeader("User-Agent");
    }

    public void logLoginAttempt(String username, boolean success, String failureReason) {
        AuthAuditLog.AuthEventType eventType = success ? 
            AuthAuditLog.AuthEventType.LOGIN : 
            AuthAuditLog.AuthEventType.LOGIN_FAILED;

        AuthAuditLog log = AuthAuditLog.builder()
                .username(username)
                .eventType(eventType)
                .ipAddress(getClientIp())
                .userAgent(getUserAgent())
                .failureReason(failureReason)
                .build();

        auditLogRepository.save(log);
        
        logger.info("Auth audit: {} - {} - {}", username, eventType, getClientIp());
    }

    public void logTokenRefresh(String username) {
        AuthAuditLog log = AuthAuditLog.builder()
                .username(username)
                .eventType(AuthAuditLog.AuthEventType.TOKEN_REFRESH)
                .ipAddress(getClientIp())
                .userAgent(getUserAgent())
                .build();

        auditLogRepository.save(log);
    }

    public void logLogout(String username) {
        AuthAuditLog log = AuthAuditLog.builder()
                .username(username)
                .eventType(AuthAuditLog.AuthEventType.LOGOUT)
                .ipAddress(getClientIp())
                .userAgent(getUserAgent())
                .build();

        auditLogRepository.save(log);
    }
}
```

### Step 4: Update AdminService to Use Audit Logging

```java
@Service
@RequiredArgsConstructor
public class AdminService {
    // ... existing fields ...
    private final AuthAuditService auditService;

    public TokenResponseDto authenticateAndGetTokens(String username, String rawPassword) {
        try {
            Optional<BaseUser> baseUserOpt = baseUserRepository.findByUsername(username);
            BaseUser baseUser = baseUserOpt.orElseThrow(
                () -> new ResourceNotFoundException("Invalid username or password"));

            if (!passwordEncoder.matches(rawPassword, baseUser.getPassword())) {
                auditService.logLoginAttempt(username, false, "Invalid password");
                throw new ResourceNotFoundException("Invalid username or password");
            }

            if (baseUser.getRole() != Role.ADMIN) {
                auditService.logLoginAttempt(username, false, "User is not an admin");
                throw new ResourceNotFoundException("User is not an admin");
            }

            String accessToken = jwtService.generateAccessToken(baseUser.getUsername(), Role.ADMIN);
            String refreshToken = jwtService.generateRefreshToken(baseUser.getUsername());
            
            auditService.logLoginAttempt(username, true, null);

            return TokenResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(15 * 60L)
                    .build();
        } catch (Exception e) {
            auditService.logLoginAttempt(username, false, e.getMessage());
            throw e;
        }
    }
}
```

---

## 3. Rate Limiting (Priority 1)

### Step 1: Add Dependency

**File:** `pom.xml`

```xml
<dependency>
    <groupId>io.github.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>
```

### Step 2: Create Rate Limiter

**File:** `src/main/java/com/hostelscout/hostel/modules/common/security/ratelimit/RateLimiter.java`

```java
package com.hostelscout.hostel.modules.common.security.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Check if request from IP is allowed (5 requests per minute)
     */
    public boolean allowRequest(String ipAddress) {
        return buckets.computeIfAbsent(ipAddress, this::createNewBucket)
                .tryConsume(1);
    }

    private Bucket createNewBucket(String ipAddress) {
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}
```

### Step 3: Create Rate Limit Interceptor

```java
package com.hostelscout.hostel.modules.common.security.ratelimit;

import com.hostelscout.hostel.common.exception.ResourceConflictException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RateLimiter rateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, 
                            jakarta.servlet.http.HttpServletResponse response, 
                            Object handler) throws Exception {
        String clientIp = getClientIp(request);
        
        if (!rateLimiter.allowRequest(clientIp)) {
            throw new ResourceConflictException("Too many login attempts. Please try again later.");
        }
        
        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
```

### Step 4: Register Interceptor

```java
package com.hostelscout.hostel.modules.common.config;

import com.hostelscout.hostel.modules.common.security.ratelimit.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/admins/login")
                .addPathPatterns("/api/*/login");
    }
}
```

---

## 4. Token Revocation/Blacklist (Priority 2)

### Step 1: Create Token Blacklist Service

```java
package com.hostelscout.hostel.modules.common.security.jwt;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.time.Instant;

@Service
public class TokenBlacklistService {
    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void blacklistToken(String tokenJti, long expirationTime) {
        blacklist.put(tokenJti, expirationTime);
    }

    public boolean isBlacklisted(String tokenJti) {
        Long expirationTime = blacklist.get(tokenJti);
        if (expirationTime == null) {
            return false;
        }
        
        // Clean up expired entries
        if (System.currentTimeMillis() > expirationTime) {
            blacklist.remove(tokenJti);
            return false;
        }
        
        return true;
    }

    public void cleanup() {
        long now = System.currentTimeMillis();
        blacklist.entrySet().removeIf(entry -> now > entry.getValue());
    }
}
```

### Step 2: Add JTI Extraction to JwtService

```java
// Add to JwtService class
public String getTokenJti(String token) throws JWTVerificationException {
    return JWT.require(getAlgorithm())
            .build()
            .verify(token)
            .getId();  // JTI claim
}
```

### Step 3: Update JWT Filter to Check Blacklist

```java
// In JwtAuthenticationFilter.doFilterInternal()
if (authHeader != null && authHeader.startsWith("Bearer ")) {
    String token = authHeader.substring(7);
    try {
        // Check if token is blacklisted
        String jti = jwtService.getTokenJti(token);
        if (tokenBlacklistService.isBlacklisted(jti)) {
            SecurityContextHolder.clearContext();
            logger.warn("Blacklisted token attempted to use");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtService.validateAndGetSubject(token);
        // ... rest of the code
    } catch (Exception e) {
        SecurityContextHolder.clearContext();
        logger.warn("JWT validation failed: {}", e.getMessage());
    }
}
```

---

## Summary

These enhancements implement:

1. **Token Refresh:** 15-minute access tokens + 7-day refresh tokens
2. **Audit Logging:** All authentication events logged to database
3. **Rate Limiting:** 5 login attempts per minute per IP
4. **Token Revocation:** Ability to blacklist tokens for logout

All are production-ready patterns following industry standards.

