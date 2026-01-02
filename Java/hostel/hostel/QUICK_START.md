# 🚀 Quick Start Guide - Testing Your New Auth Endpoint

## 30 Second Overview

You now have a **unified login endpoint** that works for all 3 user types:

```bash
POST /api/auth/login
```

Instead of separate endpoints for each role, use this single endpoint!

---

## Test Immediately (Copy & Paste)

### Test 1: Admin Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin_user","password":"admin_password"}'
```

**Expected Response:**
```json
{
  "token": "eyJhbGc...",
  "role": "ADMIN",
  "username": "admin_user",
  "userDetails": {
    "adminId": "...",
    "email": "admin@example.com"
  }
}
```

---

### Test 2: HostelOwner Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"owner_user","password":"owner_password"}'
```

**Expected Response:**
```json
{
  "token": "eyJhbGc...",
  "role": "HOSTEL_OWNER",
  "username": "owner_user",
  "userDetails": {
    "companyName": "My Hostel",
    "businessRegistrationNumber": "BR-123"
  }
}
```

---

### Test 3: User/Resident Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"resident_user","password":"resident_password"}'
```

**Expected Response:**
```json
{
  "token": "eyJhbGc...",
  "role": "HOSTEL_RESIDENT",
  "username": "resident_user",
  "userDetails": {
    "fullName": "John Doe",
    "age": 22,
    "state": "Maharashtra"
  }
}
```

---

### Test 4: Invalid Credentials (Should Fail)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"invalid","password":"wrong"}'
```

**Expected Response:**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password"
}
```

---

## Key Points

✅ **Same Endpoint** - All roles use `/api/auth/login`
✅ **Different Responses** - But returns role-specific details
✅ **No JWT for Registration** - Public signup still works
✅ **JWT for Protected Endpoints** - Use token for other APIs

---

## What Changed

| Before | After |
|--------|-------|
| `/api/admins/login` | `/api/auth/login` ✅ |
| Owner: No endpoint | `/api/auth/login` ✅ |
| User: No endpoint | `/api/auth/login` ✅ |

---

## Using the Token

After login, store the token and use it:

```bash
# Store the token
TOKEN="eyJhbGc..."

# Use in next request
curl -X GET http://localhost:8080/api/admins \
  -H "Authorization: Bearer $TOKEN"
```

---

## Files You Need to Know

1. **AuthController.java** - Handles login requests
2. **AuthService.java** - Logic (validates, generates JWT, fetches details)
3. **LoginRequestDto** - Request structure
4. **LoginResponseDto** - Response structure

---

## Common Issues & Solutions

### Issue: 401 Unauthorized
**Cause:** Invalid credentials
**Solution:** Check username/password

### Issue: 404 Not Found
**Cause:** Endpoint path wrong
**Solution:** Use exactly `/api/auth/login`

### Issue: 400 Bad Request
**Cause:** Missing username or password
**Solution:** Include both fields in JSON

### Issue: Token not working in next request
**Cause:** Token format wrong
**Solution:** Use `Authorization: Bearer <token>`

---

## Next Steps

1. **Test all 3 logins** - Verify each role works
2. **Store the token** - Keep it for other API calls
3. **Test protected endpoint** - Use token to access `/api/admins`
4. **Check frontend** - Update UI to use new endpoint

---

## Need Help?

Read these in order:
1. AUTH_QUICK_REFERENCE.md - Quick answers
2. AUTH_FLOW_DIAGRAMS.md - Visual flows
3. AUTH_IMPLEMENTATION_SUMMARY.md - Complete guide

---

**That's it! You're ready to test.** 🚀

