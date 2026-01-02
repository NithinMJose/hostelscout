# Quick Reference: Authentication Endpoints

## Overview of All Auth Endpoints

### ❌ DEPRECATED (but still works)
- `POST /api/admins/login` - Admin-only login (kept for backward compatibility)

### ✅ RECOMMENDED (New Unified Endpoint)
- `POST /api/auth/login` - Universal login for all roles

### Public Endpoints
- `POST /api/hostel-owners` - Register new HostelOwner
- `POST /api/admins` - Create new Admin (admin-only typically)

---

## Why You Don't Need JWT for Registration

Registration endpoints are **public** by design because:

1. **New users don't have credentials yet** - Can't authenticate without existing account
2. **Industry standard** - ALL platforms allow registration without login
3. **Self-service signup** - Users should be able to create their own accounts

**Security is maintained by:**
- ✅ Password validation & hashing
- ✅ Email uniqueness constraints
- ✅ Rate limiting (optional enhancement)
- ✅ CAPTCHA (optional enhancement)

---

## Current Security Configuration

```
Publicly Accessible:
├── OPTIONS /* (CORS preflight)
├── POST /api/hostel-owners (registration)
├── POST /api/admins/login (legacy login)
└── POST /api/auth/login (new unified login) ✅

Protected (Requires JWT):
├── GET/PUT/DELETE /api/admins/** 
├── GET/PUT/DELETE /api/hostel-owners/**
├── GET/PUT/DELETE /api/hostels/**
└── Other endpoints...
```

---

## What to Delete Later

You can optionally deprecate the old endpoint:
- `POST /api/admins/login` is now redundant since `/api/auth/login` handles all roles

But keep it for now for **backward compatibility** if you have frontend code using it.

---

## Flow Comparison

### Before (Multiple Endpoints)
```
Admin     → POST /api/admins/login
Owner     → POST /api/owner/login (didn't exist)
Resident  → POST /api/user/login (didn't exist)
```

### After (Single Endpoint) ✅
```
Admin     ┐
Owner     ├→ POST /api/auth/login (unified)
Resident  ┘
```

---

## Example: Testing All Three Roles

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api/auth/login"

# Admin login
echo "=== Admin Login ==="
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"username":"admin_user","password":"admin_password"}'

# HostelOwner login
echo -e "\n=== HostelOwner Login ==="
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"username":"owner_user","password":"owner_password"}'

# User/Resident login
echo -e "\n=== User/Resident Login ==="
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"username":"resident_user","password":"resident_password"}'
```

All three use the **same endpoint** but get back **different user details** based on their role!

---

## Checking JWT Token Content

After login, you'll get a token. To see what's inside:

```bash
# Visit https://jwt.io
# Paste your token there to see:
# Header: { "alg": "HS256", "typ": "JWT" }
# Payload: { "sub": "username", "role": "ADMIN", "iat": ..., "exp": ... }
```

---

## Security Summary ✅

| Aspect | Status |
|--------|--------|
| Public Registration | ✅ No auth required |
| Public Login | ✅ No auth required |
| JWT Generation | ✅ Includes role |
| Password Hashing | ✅ BCrypt |
| Token Validation | ✅ On protected endpoints |
| CORS Configuration | ✅ Configured |
| CSRF Protection | ✅ Disabled for stateless API |

Your setup follows **industry best practices**! 🚀

