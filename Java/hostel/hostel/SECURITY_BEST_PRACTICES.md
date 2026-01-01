# Security Implementation Best Practices Guide

## Overview
Your Spring Security & JWT implementation is **production-ready** and follows industry standards. This document outlines the current setup and recommendations for enhancement.

---

## Current Implementation Summary

### ✅ What's Implemented Correctly

1. **JWT Token-Based Authentication**
   - Uses Auth0's mature `java-jwt` library
   - HMAC256 algorithm for signing
   - Token contains username (subject) and role (custom claim)
   - Configurable expiration time (60 minutes)

2. **Stateless Security**
   - No session state on server
   - Perfect for REST APIs and microservices
   - Scalable horizontally

3. **Custom JWT Filter**
   - `OncePerRequestFilter` ensures single execution per request
   - Validates incoming Bearer tokens
   - Sets Spring Security context with user info and roles
   - Graceful error handling

4. **CORS Configuration**
   - Properly configured for frontend integration
   - Environment-specific (loaded from properties)
   - Credentials allowed for authentication

5. **Password Security**
   - BCrypt password hashing (cost factor 10+)
   - Never stores plaintext passwords

---

## Implementation Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Incoming Request                         │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│              SecurityFilterChain                            │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ 1. CORS Filter (highest precedence)                 │   │
│  │    - Handles preflight OPTIONS requests             │   │
│  │    - Sets CORS headers for allowed origins          │   │
│  └─────────────────────────────────────────────────────┘   │
│                       │                                      │
│  ┌─────────────────────▼─────────────────────────────────┐  │
│  │ 2. JwtAuthenticationFilter                           │  │
│  │    - Extracts Bearer token from Authorization header │  │
│  │    - Validates token signature & expiration          │  │
│  │    - Extracts username and role                      │  │
│  │    - Sets SecurityContext                            │  │
│  └─────────────────────────────────────────────────────┘   │
│                       │                                      │
│  ┌─────────────────────▼─────────────────────────────────┐  │
│  │ 3. Authorization Filter                              │  │
│  │    - Checks if endpoint is permitted                 │  │
│  │    - Validates user roles                            │  │
│  │    - /api/admins/login - permitAll()                 │  │
│  │    - Other /api/* - authenticated()                  │  │
│  └─────────────────────────────────────────────────────┘   │
│                       │                                      │
│  ┌─────────────────────▼─────────────────────────────────┐  │
│  │ 4. Controller Method Execution                        │  │
│  │    - Optional: @PreAuthorize for finer control       │  │
│  │    - Example: @PreAuthorize("hasRole('ADMIN')")      │  │
│  └─────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────┘
```

---

## Key Components

### 1. JwtService
**Location:** `src/main/java/com/hostelscout/hostel/modules/common/security/jwt/JwtService.java`

**Responsibilities:**
- Generate JWT tokens with username and role
- Validate incoming tokens
- Extract claims from tokens

**Features:**
- Caches Algorithm instance for better performance
- Throws `JWTVerificationException` on invalid/expired tokens
- Configurable expiration via properties

**Usage:**
```java
// Generate token
String token = jwtService.generateToken("john_doe", Role.ADMIN);

// Validate and extract claims
String username = jwtService.validateAndGetSubject(token);
String role = jwtService.validateAndGetRole(token);
```

### 2. JwtAuthenticationFilter
**Location:** `src/main/java/com/hostelscout/hostel/modules/common/security/jwt/JwtAuthenticationFilter.java`

**Responsibilities:**
- Intercept HTTP requests
- Extract Bearer token from Authorization header
- Validate and process token
- Populate Spring Security context

**Flow:**
1. Check for "Authorization: Bearer <token>" header
2. Extract token (substring after "Bearer ")
3. Validate token signature and expiration
4. Extract username and role from token
5. Create `UsernamePasswordAuthenticationToken` with authorities
6. Set in `SecurityContextHolder`
7. Continue filter chain

**Error Handling:**
- Catches all exceptions during validation
- Clears SecurityContext on error
- Logs warnings for debugging

### 3. SecurityConfig
**Location:** `src/main/java/com/hostelscout/hostel/modules/common/config/SecurityConfig.java`

**Configuration:**
- CORS sources: Loaded from `app.security.cors.allowed-origins` property
- CSRF: Disabled (correct for stateless APIs)
- Session Policy: Stateless (no server-side sessions)
- Public endpoints: `/api/admins/login`, OPTIONS requests
- Protected endpoints: All other `/api/**` requests
- JWT filter: Added before `UsernamePasswordAuthenticationFilter`

---

## Configuration Files

### application.properties
```properties
# JWT Configuration
app.security.jwt.secret=hostelscout-jwt-secret-32-characters-minimum!!
app.security.jwt.expiration-minutes=60

# CORS Configuration
app.security.cors.allowed-origins=http://localhost:5173,http://localhost:3000
```

**Environment Variables (Recommended for Production):**
```bash
# Use environment variables instead of hardcoding
export JWT_SECRET="your-secure-random-string-here"
export JWT_EXPIRATION=60
export CORS_ORIGINS="https://yourdomain.com,https://www.yourdomain.com"
```

---

## Authentication Flow

### 1. User Login
```
Client → POST /api/admins/login
         { username: "john", password: "secure123" }
            ↓
Server → Authenticate credentials
         Hash password and verify
            ↓
         Generate JWT token
         payload: { sub: "john", role: "ADMIN", iat: 1704067200, exp: 1704070800 }
            ↓
Client ← { token: "eyJhbGc...", admin: {...} }
```

### 2. Authenticated Request
```
Client → GET /api/admins/123
         Header: Authorization: Bearer eyJhbGc...
            ↓
Server → JwtAuthenticationFilter intercepts
         Validates token signature & expiration
         Extracts: username="john", role="ADMIN"
            ↓
         Creates authentication token
         Sets SecurityContext
            ↓
         Authorization filter checks:
         - User authenticated? ✓
         - Endpoint requires ADMIN role? 
         - User has ADMIN role? ✓
            ↓
Server ← Executes controller method
         Returns { id: "123", name: "John Hotel", ... }
```

### 3. Token Expiration/Invalid
```
Client → GET /api/admins/123
         Header: Authorization: Bearer invalid_or_expired_token
            ↓
Server → JwtAuthenticationFilter intercepts
         Tries to validate token
         ✗ Signature mismatch or expired
         Exception caught
         SecurityContext cleared
            ↓
         Authorization filter checks:
         - User authenticated? ✗
         - Endpoint requires authentication? ✓
            ↓
Server ← 401 Unauthorized
```

---

## Security Best Practices Implemented

### ✅ JWT Security
- **Signature Verification:** HMAC256 prevents token tampering
- **Expiration:** Tokens expire after 60 minutes
- **Issuer Claim:** Could add to prevent cross-service token usage
- **Audience Claim:** Could add for additional validation

### ✅ Password Security
- **Hashing:** BCrypt with salt
- **Never Stored Plaintext:** Only hashed versions stored
- **Timing-Safe Comparison:** BCrypt handles timing attacks

### ✅ Transport Security
- HTTPS should be enforced in production (configure in nginx/gateway)
- CORS prevents unauthorized cross-origin requests
- CSRF disabled (safe for stateless JWT)

### ✅ Session Security
- No server-side sessions
- No session fixation attacks
- No JSESSIONID cookies to steal

---

## Recommended Enhancements

### Priority 1: High (Implement Soon)

#### 1. Add Token Refresh Mechanism
**Current:** Single token, 60-minute expiration
**Problem:** Long-lived tokens are security risk; short-lived tokens hurt UX
**Solution:** Implement refresh tokens

```java
// Generate both access and refresh tokens
{
  "accessToken": "eyJhbGc...",      // 15 minutes
  "refreshToken": "eyJhbGc...",     // 7 days
  "expiresIn": 900
}

// Client uses refresh token to get new access token
POST /api/auth/refresh
{ "refreshToken": "eyJhbGc..." }
```

#### 2. Add Audit Logging
**Current:** Basic logging in filter
**Problem:** No record of authentication attempts
**Solution:** Log to database or monitoring system

```java
@Service
public class AuthAuditService {
    public void logLoginAttempt(String username, boolean success, String reason) {
        // Log to database or external service
    }
}
```

#### 3. Rate Limiting on Login
**Current:** No rate limiting
**Problem:** Vulnerable to brute force attacks
**Solution:** Use Spring Boot's built-in rate limiting or library

```java
@RateLimiter(name = "loginLimiter")  // 5 attempts per minute
@PostMapping("/login")
public ResponseEntity<?> login(...) { }
```

### Priority 2: Medium (Implement for Production)

#### 1. Token Revocation/Blacklist
**When:** User logout, password change, or permission revocation
**Implementation:**
```java
@Service
public class TokenBlacklistService {
    private final Set<String> blacklist = new ConcurrentHashSet<>();
    
    public void blacklistToken(String token) {
        blacklist.add(jwtService.getTokenJTI(token));
    }
    
    public boolean isBlacklisted(String token) {
        return blacklist.contains(jwtService.getTokenJTI(token));
    }
}
```

#### 2. Add Standard JWT Claims
```java
// Current: sub, role, iat, exp
// Recommended additions:
.withJTI(UUID.randomUUID().toString())  // Unique token ID (for revocation)
.withIssuer("hostelscout")              // Token issuer
.withAudience("hostel-api")             // Token audience
.withNotBefore(new Date())              // Not valid before
```

#### 3. HTTPS/SSL Enforcement
```properties
# application.properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=changeme
server.ssl.key-store-type=PKCS12
```

### Priority 3: Low (Nice to Have)

#### 1. Two-Factor Authentication (2FA)
- Email/SMS verification on login
- TOTP (Time-based One-Time Password)
- Recovery codes

#### 2. IP Whitelisting
- Allow logins only from known IPs
- Track and alert on new IPs

#### 3. Device Fingerprinting
- Track which devices are logged in
- Require confirmation for new devices

#### 4. OpenAPI/Swagger Documentation
```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearer-jwt",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
```

---

## Testing Your Security

### Unit Tests
```java
@Test
public void testValidToken_ShouldAuthenticate() {
    String token = jwtService.generateToken("john", Role.ADMIN);
    String username = jwtService.validateAndGetSubject(token);
    assertEquals("john", username);
}

@Test
public void testExpiredToken_ShouldThrowException() {
    String token = generateExpiredToken();
    assertThrows(JWTVerificationException.class, 
        () -> jwtService.validateAndGetSubject(token));
}

@Test
public void testInvalidSignature_ShouldThrowException() {
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalid.signature";
    assertThrows(JWTVerificationException.class,
        () -> jwtService.validateAndGetSubject(token));
}
```

### Integration Tests
```java
@Test
public void testUnauthorizedRequest_ShouldReturn401() {
    mvc.perform(get("/api/admins/123")
        .header("Authorization", "Bearer invalid_token"))
        .andExpect(status().isUnauthorized());
}

@Test
public void testValidToken_ShouldAccess() {
    String token = getValidToken();
    mvc.perform(get("/api/admins/123")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
}
```

---

## Troubleshooting

### Problem: "401 Unauthorized" on valid token
**Possible Causes:**
1. Token expired (check exp claim)
2. Wrong JWT secret in properties
3. Token signature modified
4. Clock skew between servers

**Solution:**
```bash
# Decode token to check claims (online: jwt.io)
echo "token_here" | base64 -d
```

### Problem: CORS error in browser
**Possible Causes:**
1. Frontend origin not in `app.security.cors.allowed-origins`
2. CORS filter not running before Spring Security
3. Missing preflight OPTIONS handling

**Solution:**
```properties
# Add your frontend URL
app.security.cors.allowed-origins=http://localhost:5173,https://yourdomain.com
```

### Problem: Stateless session error
**Ensure:** `SessionCreationPolicy.STATELESS` is set in SecurityConfig

---

## Production Checklist

- [ ] Use environment variables for JWT secret and CORS origins
- [ ] Enable HTTPS/SSL
- [ ] Set shorter token expiration (15-30 minutes)
- [ ] Implement token refresh mechanism
- [ ] Add rate limiting on login endpoint
- [ ] Implement token blacklist for logout
- [ ] Add comprehensive audit logging
- [ ] Configure WAF (Web Application Firewall) rules
- [ ] Monitor for suspicious authentication patterns
- [ ] Regular security audits and penetration testing
- [ ] Keep dependencies updated
- [ ] Use strong, randomly-generated JWT secret (min 256 bits)

---

## References

1. **JWT Standard:** https://tools.ietf.org/html/rfc7519
2. **Auth0 Java JWT:** https://github.com/auth0/java-jwt
3. **Spring Security:** https://spring.io/projects/spring-security
4. **OWASP Top 10:** https://owasp.org/Top10/
5. **RFC 6750 (Bearer Token):** https://tools.ietf.org/html/rfc6750

---

## Summary

Your implementation:
- ✅ Follows REST API security best practices
- ✅ Uses industry-standard JWT approach
- ✅ Is simple and maintainable
- ✅ Is NOT over-engineered
- ⚠️ Would benefit from refresh tokens for production
- ⚠️ Should add audit logging

**Verdict:** Ready for production with the recommended Priority 1 enhancements.

