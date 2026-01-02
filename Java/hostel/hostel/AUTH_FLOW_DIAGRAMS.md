# Authentication Flow Diagram & Examples

## Complete Login Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENT APPLICATION                           │
└──────────────────────────────┬──────────────────────────────────────┘
                               │
                               │ 1. POST /api/auth/login
                               │    { "username": "x", "password": "y" }
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      SPRING SECURITY FILTER                          │
│                                                                       │
│  ✅ Permit /api/auth/login without JWT                              │
│  ✅ Allow request through (no JWT required)                         │
└──────────────────────────────┬──────────────────────────────────────┘
                               │
                               │ 2. Route to AuthController
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      AuthController.login()                         │
│                                                                       │
│  Receives: LoginRequestDto { username, password }                   │
└──────────────────────────────┬──────────────────────────────────────┘
                               │
                               │ 3. Call AuthService.authenticate()
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       AuthService                                     │
│                                                                       │
│  Step 1: Find BaseUser by username ────────────────┐                │
│          (queries BaseUserRepository)              │                │
│                                                    │                │
│  Step 2: Validate password ◄──────────────────────┘                │
│          (BCryptPasswordEncoder.matches)                             │
│                                                    ┐                │
│  Step 3: Generate JWT ◄────────────────────────────┤                │
│          (JwtService.generateToken)                │                │
│          Token includes: username, role            │                │
│                                                    │                │
│  Step 4: Fetch role-specific details ◄────────────┤                │
│                                                    │                │
│          If ADMIN:                                 │                │
│          └─ adminRepository.findByBaseUser()       │                │
│             └─ adminMapper.toAdminResponseDto()    │                │
│                                                    │                │
│          If HOSTEL_OWNER:                         │                │
│          └─ hostelOwnerRepository.findByBaseUser() │                │
│             └─ hostelOwnerMapper.toResponseDto()   │                │
│                                                    │                │
│          If HOSTEL_RESIDENT:                       │                │
│          └─ userRepository.findByBaseUser()        │                │
│             └─ Return User entity                  │                │
│                                                    │                │
│  Step 5: Build LoginResponseDto ◄─────────────────┘                │
│          ├─ token: JWT                                              │
│          ├─ role: Role enum                                         │
│          ├─ username: String                                        │
│          └─ userDetails: Object (polymorphic)                       │
└──────────────────────────────┬──────────────────────────────────────┘
                               │
                               │ 4. Return 200 OK
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENT APPLICATION                           │
│                                                                       │
│  Receives LoginResponseDto:                                         │
│  {                                                                   │
│    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",              │
│    "role": "ADMIN",                                                  │
│    "username": "john_doe",                                           │
│    "userDetails": { /* admin-specific data */ }                     │
│  }                                                                   │
│                                                                       │
│  5. Store token in localStorage/sessionStorage                       │
│  6. Use token for future requests:                                   │
│     Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...   │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Sequential Diagram: Three Different Roles

```
┌─────────────────────────────────────────────────────────────────────┐
│                    SAME ENDPOINT - DIFFERENT RESPONSES              │
└─────────────────────────────────────────────────────────────────────┘

SCENARIO 1: Admin User
─────────────────────────────────────────────────────────────────────

REQUEST:
POST /api/auth/login
{
  "username": "admin_john",
  "password": "admin_password"
}

↓ [Authenticate] ↓

RESPONSE:
200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbl9qb2huIiwicm9sZSI6IkFETUluIiwiaWF0IjoxNjc0MjMwNDAwfQ.xyz...",
  "role": "ADMIN",
  "username": "admin_john",
  "userDetails": {
    "adminId": "550e8400-e29b-41d4-a716-446655440000",
    "email": "admin@example.com",
    "adminStatus": "ACTIVE",
    "createdAt": "2026-01-02T12:00:00",
    "updatedAt": "2026-01-02T14:30:00",
    "role": "ADMIN"
  }
}

───────────────────────────────────────────────────────────────────────

SCENARIO 2: HostelOwner User
─────────────────────────────────────────────────────────────────────

REQUEST:
POST /api/auth/login
{
  "username": "owner_rajesh",
  "password": "owner_password"
}

↓ [Authenticate] ↓

RESPONSE:
200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvd25lcl9yYWplc2giLCJyb2xlIjoiSE9TVEVMX09XTkVSIiwiaWF0IjoxNjc0MjMwNDAwfQ.xyz...",
  "role": "HOSTEL_OWNER",
  "username": "owner_rajesh",
  "userDetails": {
    "username": "owner_rajesh",
    "email": "owner@hostelcompany.com",
    "companyName": "Rajesh's Hostel Network",
    "businessRegistrationNumber": "BR-2024-001234",
    "contactNumber": "+91-9876543210",
    "role": "HOSTEL_OWNER"
  }
}

───────────────────────────────────────────────────────────────────────

SCENARIO 3: User/Resident
─────────────────────────────────────────────────────────────────────

REQUEST:
POST /api/auth/login
{
  "username": "resident_priya",
  "password": "resident_password"
}

↓ [Authenticate] ↓

RESPONSE:
200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJyZXNpZGVudF9wcml5YSIsInJvbGUiOiJIT1NUFWVMX1JFU0lERU5UIiwiaWF0IjoxNjc0MjMwNDAwfQ.xyz...",
  "role": "HOSTEL_RESIDENT",
  "username": "resident_priya",
  "userDetails": {
    "userId": 42,
    "fullName": "Priya Sharma",
    "phoneNumber": "9876543210",
    "gender": "Female",
    "age": 21,
    "state": "Maharashtra",
    "status": "ACTIVE",
    "baseUser": {
      "baseUserId": 100,
      "username": "resident_priya",
      "email": "priya@example.com",
      "role": "HOSTEL_RESIDENT"
    }
  }
}
```

---

## Database Query Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│ User submits: { "username": "john_doe", "password": "secret" }     │
└────────────────────────────┬────────────────────────────────────────┘
                             │
                             ▼
        ┌────────────────────────────────────────┐
        │ SELECT * FROM base_users               │
        │ WHERE username = 'john_doe'            │
        └────────┬─────────────────────────────────┘
                 │
                 ▼
        ┌────────────────────────────────────────┐
        │ BaseUser found:                        │
        │ {                                      │
        │   base_user_id: 1,                     │
        │   username: 'john_doe',                │
        │   email: 'john@example.com',           │
        │   password: 'hashed_bcrypt_value',     │
        │   role: 'HOSTEL_OWNER'                 │
        │ }                                      │
        └────────┬─────────────────────────────────┘
                 │
                 ├─ Verify password with BCrypt
                 │  (matches raw password with hashed)
                 │
                 ▼
        ┌────────────────────────────────────────┐
        │ Password is valid ✅                   │
        │ Role: HOSTEL_OWNER                     │
        └────────┬─────────────────────────────────┘
                 │
                 ├─ Switch on role
                 │
                 ▼
        ┌────────────────────────────────────────┐
        │ SELECT * FROM hostel_owners            │
        │ WHERE base_user_id = 1                 │
        └────────┬─────────────────────────────────┘
                 │
                 ▼
        ┌────────────────────────────────────────┐
        │ HostelOwner found:                     │
        │ {                                      │
        │   owner_id: 'uuid-...',                │
        │   companyName: 'My Hostel Co.',        │
        │   businessRegistrationNumber: 'BR-...', │
        │   contactNumber: '+91-...',            │
        │   baseUser: { /* BaseUser */ }         │
        │ }                                      │
        └────────┬─────────────────────────────────┘
                 │
                 ├─ Mapper converts to DTO
                 │
                 ▼
        ┌────────────────────────────────────────┐
        │ HostelOwnerResponseDto created         │
        │ {                                      │
        │   username: 'john_doe',                │
        │   email: 'john@example.com',           │
        │   companyName: 'My Hostel Co.',        │
        │   businessRegistrationNumber: 'BR-...', │
        │   contactNumber: '+91-...',            │
        │   role: 'HOSTEL_OWNER'                 │
        │ }                                      │
        └────────┬─────────────────────────────────┘
                 │
                 ├─ Generate JWT token
                 │
                 ▼
        ┌────────────────────────────────────────┐
        │ JWT Token created with:                │
        │ {                                      │
        │   sub: 'john_doe',                     │
        │   role: 'HOSTEL_OWNER',                │
        │   iat: 1674230400,                     │
        │   exp: 1674234000                      │
        │ }                                      │
        │ Signed with: HMAC256(jwt_secret)       │
        └────────┬─────────────────────────────────┘
                 │
                 ▼
        ┌────────────────────────────────────────┐
        │ LoginResponseDto returned:             │
        │ {                                      │
        │   token: 'eyJ...',                     │
        │   role: 'HOSTEL_OWNER',                │
        │   username: 'john_doe',                │
        │   userDetails: { /* DTO */ }           │
        │ }                                      │
        └────────────────────────────────────────┘
```

---

## JWT Token Structure (After Base64 Decoding)

```
Header (eyJhbGc...):
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload (eyJzdWI...):
{
  "sub": "john_doe",              ← Username
  "role": "HOSTEL_OWNER",         ← Role
  "iat": 1674230400,              ← Issued at
  "exp": 1674234000               ← Expires at (60 minutes later)
}

Signature (xyz...):
HMACSHA256(
  header + "." + payload,
  "your-jwt-secret-from-properties"
)
```

---

## Error Scenarios

```
SCENARIO 1: Invalid Username
─────────────────────────────

REQUEST:
POST /api/auth/login
{
  "username": "nonexistent_user",
  "password": "somepassword"
}

↓ [BaseUserRepository.findByUsername returns empty] ↓

RESPONSE:
401 UNAUTHORIZED
{
  "timestamp": "2026-01-02T17:15:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password",
  "path": "/api/auth/login"
}

───────────────────────────────────────────────────

SCENARIO 2: Invalid Password
─────────────────────────────

REQUEST:
POST /api/auth/login
{
  "username": "john_doe",
  "password": "wrongpassword"
}

↓ [BCryptPasswordEncoder.matches returns false] ↓

RESPONSE:
401 UNAUTHORIZED
{
  "timestamp": "2026-01-02T17:15:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password",
  "path": "/api/auth/login"
}

───────────────────────────────────────────────────

SCENARIO 3: Missing Required Field
───────────────────────────────────

REQUEST:
POST /api/auth/login
{
  "username": "john_doe"
  // missing password
}

↓ [Validation fails @NotBlank] ↓

RESPONSE:
400 BAD REQUEST
{
  "timestamp": "2026-01-02T17:15:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Password is required",
  "path": "/api/auth/login"
}
```

---

## Using JWT Token in Subsequent Requests

```
AUTHENTICATED REQUEST FLOW
──────────────────────────

Step 1: Client stores token from login response
   localStorage.setItem('jwt_token', 'eyJhbGc...')

Step 2: Client makes request to protected endpoint
   GET /api/admins
   Headers: {
     "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   }

Step 3: Spring Security intercepts request
   ├─ JwtAuthenticationFilter extracts token from header
   ├─ JwtService validates token signature
   ├─ JwtService extracts username and role
   ├─ Creates UsernamePasswordAuthenticationToken
   └─ Sets in SecurityContext

Step 4: Request proceeds to controller
   ✅ User is authenticated
   ✅ Controller can access authenticated user info
   ✅ @PreAuthorize checks role if needed

Step 5: Response sent to client
   200 OK { /* requested data */ }

───────────────────────────────────────────────────

UNAUTHENTICATED REQUEST (No Token)
──────────────────────────────────

GET /api/admins
(no Authorization header)

↓ [JwtAuthenticationFilter finds no token] ↓

401 UNAUTHORIZED
{
  "timestamp": "2026-01-02T17:15:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token is missing or invalid",
  "path": "/api/admins"
}

───────────────────────────────────────────────────

EXPIRED TOKEN REQUEST
──────────────────────────────────

GET /api/admins
Headers: {
  "Authorization": "Bearer eyJhbGc...expired"
}

↓ [JwtService.validateToken fails due to exp < now] ↓

401 UNAUTHORIZED
{
  "timestamp": "2026-01-02T17:15:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token has expired",
  "path": "/api/admins"
}
```

---

## Frontend Integration Example (JavaScript)

```javascript
// 1. LOGIN
async function login(username, password) {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  
  if (!response.ok) throw new Error('Login failed');
  
  const data = await response.json();
  
  // Save token
  localStorage.setItem('jwt_token', data.token);
  localStorage.setItem('role', data.role);
  localStorage.setItem('username', data.username);
  
  // Store user details
  localStorage.setItem('userDetails', JSON.stringify(data.userDetails));
  
  return data;
}

// 2. PROTECTED API CALL
async function getAdmins() {
  const token = localStorage.getItem('jwt_token');
  
  const response = await fetch('/api/admins', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  
  if (response.status === 401) {
    // Token expired or invalid
    logout();
    return;
  }
  
  return response.json();
}

// 3. LOGOUT
function logout() {
  localStorage.removeItem('jwt_token');
  localStorage.removeItem('role');
  localStorage.removeItem('username');
  localStorage.removeItem('userDetails');
  
  // Redirect to login
  window.location.href = '/login';
}
```

---

**This is the complete authentication flow for your system!** ✅

