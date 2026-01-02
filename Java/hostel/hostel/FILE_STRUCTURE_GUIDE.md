# 📁 File Structure - Authentication Module

## Complete Module Structure

```
src/main/java/com/hostelscout/hostel/
├── modules/
│   ├── auth/                          ← NEW AUTH MODULE
│   │   ├── controller/
│   │   │   └── AuthController.java    ✨ NEW - Login endpoint
│   │   ├── service/
│   │   │   └── AuthService.java       ✨ NEW - Auth logic
│   │   └── dto/
│   │       ├── LoginRequestDto.java   ✨ NEW - Request DTO
│   │       └── LoginResponseDto.java  ✨ NEW - Response DTO
│   │
│   ├── admin/
│   │   ├── controller/
│   │   │   └── AdminController.java
│   │   ├── service/
│   │   │   └── AdminService.java      (already has authenticate method)
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── mapper/
│   │   └── repository/
│   │
│   ├── hostelowner/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── mapper/
│   │   └── repository/
│   │       └── HostelOwnerRepository.java  📝 MODIFIED - Added findByBaseUser()
│   │
│   ├── user/
│   │   ├── entity/
│   │   │   └── User.java
│   │   └── repository/
│   │       └── UserRepository.java    ✨ NEW - User queries
│   │
│   ├── common/
│   │   ├── config/
│   │   │   └── SecurityConfig.java    📝 MODIFIED - Added /api/auth/login whitelist
│   │   ├── entity/
│   │   │   └── BaseUser.java          (core entity for all roles)
│   │   ├── enums/
│   │   │   └── Role.java              (ADMIN, HOSTEL_OWNER, HOSTEL_RESIDENT)
│   │   └── repository/
│   │       └── BaseUserRepository.java
│   │
│   └── ...other modules...
│
└── resources/
    └── application.properties          (JWT config here)
```

---

## New Files Added (5 Total)

### 1. AuthController.java
```
Location: src/main/java/com/hostelscout/hostel/modules/auth/controller/
Lines:    47
Purpose:  Handles POST /api/auth/login for all user roles
```

**Content Summary:**
- RestController with @RequestMapping("/api/auth")
- Single method: login(LoginRequestDto)
- Returns: LoginResponseDto with token + role + user details
- Logging for audit trail

---

### 2. AuthService.java
```
Location: src/main/java/com/hostelscout/hostel/modules/auth/service/
Lines:    100
Purpose:  Authentication logic with role-aware user fetching
```

**Content Summary:**
- Transactional(readOnly = true) for safety
- authenticate(username, password) method
- Finds BaseUser → Validates password → Gets role → Generates JWT → Fetches role details
- Handles all 3 roles: ADMIN, HOSTEL_OWNER, HOSTEL_RESIDENT
- Uses repositories for data access
- Uses JwtService for token generation
- Uses AdminMapper and HostelOwnerMapper for DTOs

---

### 3. LoginRequestDto.java
```
Location: src/main/java/com/hostelscout/hostel/modules/auth/dto/
Lines:    22
Purpose:  Login request validation
```

**Content Summary:**
- Fields: username (required), password (required)
- Annotated with @NotBlank for validation
- Lombok annotations for getters/setters

---

### 4. LoginResponseDto.java
```
Location: src/main/java/com/hostelscout/hostel/modules/auth/dto/
Lines:    21
Purpose:  Login response structure
```

**Content Summary:**
- Fields:
  - token (String) - JWT token
  - role (Role enum) - User's role
  - username (String) - Authenticated username
  - userDetails (Object) - Role-specific details (polymorphic)

---

### 5. UserRepository.java
```
Location: src/main/java/com/hostelscout/hostel/modules/user/repository/
Lines:    10
Purpose:  User entity database queries
```

**Content Summary:**
- Extends JpaRepository<User, Long>
- Method: findByBaseUser(BaseUser) → Optional<User>
- Used during login to fetch resident details

---

## Modified Files (2 Total)

### 1. SecurityConfig.java
```
Location: src/main/java/com/hostelscout/hostel/modules/common/config/
Change:   Updated security filter chain rules
```

**Changes Made:**
```diff
  .authorizeHttpRequests(auth -> auth
+     .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
      .requestMatchers(HttpMethod.POST, "/api/hostel-owners").permitAll()
+     .requestMatchers(HttpMethod.POST, "/api/admins/login").permitAll()
+     .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
-     .requestMatchers("/**").permitAll()  // REMOVED overly broad matcher
      .anyRequest().authenticated()
  );
```

**Why Changed:**
- Added specific rules for public endpoints
- Removed dangerous `requestMatchers("/**").permitAll()` rule
- Added `/api/auth/login` to whitelist
- Fixed `/api/admins/login` to use HttpMethod.POST

---

### 2. HostelOwnerRepository.java
```
Location: src/main/java/com/hostelscout/hostel/modules/hostelowner/repository/
Change:   Added findByBaseUser() method
```

**Changes Made:**
```diff
+ import com.hostelscout.hostel.modules.common.entity.BaseUser;
+ import java.util.Optional;

  public interface HostelOwnerRepository extends JpaRepository<HostelOwner, UUID> {
+     Optional<HostelOwner> findByBaseUser(BaseUser baseUser);
  }
```

**Why Changed:**
- Needed to lookup HostelOwner by BaseUser during login
- Called by AuthService.authenticate()
- Follows same pattern as AdminRepository

---

## Class Diagram

```
┌──────────────────────────────────────┐
│          BaseUser (Entity)            │
├──────────────────────────────────────┤
│ - base_user_id (PK)                  │
│ - username (UNIQUE)                  │
│ - email (UNIQUE)                     │
│ - password (hashed)                  │
│ - role (ENUM)                        │
└──────────────────────────────────────┘
            ▲      ▲      ▲
            │      │      │
            │1:1   │1:1   │1:1
            │      │      │
            │      │      └─ User
            │      │
            │      └─ HostelOwner
            │
            └─ Admin

┌──────────────────────────────────────┐
│     AuthController                   │
├──────────────────────────────────────┤
│ + login(LoginRequestDto)             │
└──────────────────────────────────────┘
            │ calls
            ▼
┌──────────────────────────────────────┐
│     AuthService                      │
├──────────────────────────────────────┤
│ + authenticate(username, password)   │
│   - findByUsername()                 │
│   - validatePassword()               │
│   - generateToken()                  │
│   - fetchUserDetails(role)           │
└──────────────────────────────────────┘
            │ uses
            ├─ BaseUserRepository
            ├─ AdminRepository
            ├─ HostelOwnerRepository
            ├─ UserRepository
            ├─ JwtService
            ├─ AdminMapper
            └─ HostelOwnerMapper
```

---

## Dependencies (No New Dependencies Added)

Your existing Spring Boot dependencies handle:
- ✅ JPA/Hibernate (repositories)
- ✅ Spring Security (authentication)
- ✅ JWT (com.auth0:java-jwt)
- ✅ Lombok (annotations)
- ✅ MapStruct (DTOs)
- ✅ Validation (jakarta.validation)

---

## Configuration Required (in application.properties)

```properties
# JWT Configuration (should already be set)
app.security.jwt.secret=your-secret-key-here
app.security.jwt.expiration-minutes=60

# CORS Configuration (should already be set)
app.security.cors.allowed-origins=http://localhost:3000,http://localhost:8080
```

---

## Database Schema Impact

### New Repositories Using Existing Schema
- UserRepository queries existing `users` table
- HostelOwnerRepository.findByBaseUser() queries existing `hostel_owners` table
- No schema changes needed
- All relationships already exist (1:1 with BaseUser)

---

## Build Artifacts

### Compiled Classes Generated
```
target/classes/com/hostelscout/hostel/modules/auth/
├── controller/AuthController.class
├── service/AuthService.class
└── dto/
    ├── LoginRequestDto.class
    └── LoginResponseDto.class

target/classes/com/hostelscout/hostel/modules/user/
└── repository/UserRepository.class
```

---

## Total Code Statistics

| Metric | Value |
|--------|-------|
| New Files | 5 |
| Modified Files | 2 |
| Total Lines Added | ~200 |
| Total Lines Modified | ~15 |
| Total Classes | 5 |
| New Endpoints | 1 |
| Compilation Status | ✅ Success |
| Errors | 0 |
| Warnings | 0 |

---

## File Checklist

### Created Files (Verify These Exist)
- [x] modules/auth/controller/AuthController.java
- [x] modules/auth/service/AuthService.java
- [x] modules/auth/dto/LoginRequestDto.java
- [x] modules/auth/dto/LoginResponseDto.java
- [x] modules/user/repository/UserRepository.java

### Modified Files (Verify These Have Updates)
- [x] modules/common/config/SecurityConfig.java
- [x] modules/hostelowner/repository/HostelOwnerRepository.java

### Supporting Files (Already Exist)
- [x] modules/common/entity/BaseUser.java
- [x] modules/admin/entity/Admin.java
- [x] modules/admin/repository/AdminRepository.java
- [x] modules/admin/mapper/AdminMapper.java
- [x] modules/hostelowner/entity/HostelOwner.java
- [x] modules/hostelowner/mapper/HostelOwnerMapper.java
- [x] modules/user/entity/User.java
- [x] modules/common/security/jwt/JwtService.java

---

## Next Steps: Optional Enhancements

### Easy to Add (1-2 hours each)
1. **UserResponseDto** - Create mapper for cleaner User login response
2. **LoginAudit** - Track login attempts
3. **Rate Limiting** - Limit failed login attempts

### Medium Complexity (2-4 hours each)
1. **Token Refresh** - Implement refresh token mechanism
2. **Email Verification** - Send verification email on registration
3. **Password Reset** - Forgot password flow

### Advanced (4+ hours each)
1. **Token Revocation** - Logout and blacklist tokens
2. **OAuth2 Integration** - Login with Google/GitHub
3. **Two-Factor Auth** - SMS/Email OTP verification

---

**All files are in place and project compiles successfully!** ✅

