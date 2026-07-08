# Day 9 Exact Trainer Plan - Authentication & Authorisation with JWT

## Day 9 title

Authentication & Authorisation with JWT

## Learning outcomes

By the end of Day 9, students should be able to:

1. Explain the difference between authentication and authorisation.
2. Explain why passwords must be hashed, not stored as plain text.
3. Register a user using a backend endpoint.
4. Login and receive a JWT.
5. Send a JWT using the `Authorization: Bearer` header.
6. Protect API endpoints using Spring Security.
7. Apply simple role-based access control.
8. Test public, authenticated, and role-protected endpoints using an HTTP file.

## 9:00 - 9:30 | Recap Day 8 and introduce security

Review:

```text
Day 7: MongoDB persistence
Day 8: queries, pagination, indexes, logging, duplicate handling
Day 9: secure the API
```

Ask students:

```text
Should anyone on the internet be able to create assets in our system?
```

Expected answer:

```text
No. We need login and permission checks.
```

## 9:30 - 10:15 | Theory: authentication, authorisation, JWT

Explain:

```text
Authentication = Who are you?
Authorisation = What are you allowed to do?
```

Introduce the flow:

```text
Register -> password is hashed -> user saved
Login -> password checked -> JWT returned
Client sends token -> backend validates token -> endpoint allowed or rejected
```

## 10:15 - 11:00 | Add user model and repository

Show:

```text
AppUser.java
AppUserRepository.java
```

Explain why user is separate from asset.

## 11:00 - 12:00 | Build register and login

Show:

```text
RegisterRequest
LoginRequest
AuthResponse
AuthService
AuthController
```

Test:

```http
POST /api/auth/register
POST /api/auth/login
```

## 12:00 - 1:00 | Generate JWT

Show:

```text
JwtService
app.jwt.secret
app.jwt.expiration-minutes
```

Explain token claims:

```text
subject = email
userId = MongoDB user id
role = USER or ADMIN
expiresAt = token expiry time
```

## 2:00 - 3:00 | Add Spring Security configuration

Show:

```text
SecurityConfig
AppUserDetailsService
PasswordEncoder
SecurityFilterChain
JwtEncoder
JwtDecoder
```

Explain endpoint rules:

```text
/api/health -> public
/api/auth/** -> public
GET /api/assets/** -> USER or ADMIN
POST /api/assets -> ADMIN only
```

## 3:00 - 3:45 | Test protected endpoints

Use `requests/day09-auth.http`.

Test sequence:

```text
1. GET /api/assets without token -> 401
2. Register user -> token returned
3. GET /api/assets with USER token -> 200
4. POST /api/assets with USER token -> 403
5. Login admin -> token returned
6. POST /api/assets with ADMIN token -> 201
```

## 4:00 - 4:40 | Student exercise

Students apply the same concept to their Support Desk Ticket API.

## 4:40 - 5:00 | Review and auth flow diagram

Students submit:

```text
1. Auth flow diagram
2. HTTP test evidence
3. Short explanation of 401 vs 403
```
