# Day 9 Auth Concepts Guide

## Authentication vs authorisation

Authentication answers:

```text
Who are you?
```

Authorisation answers:

```text
What are you allowed to do?
```

Example:

```text
A user logs in successfully. That means they are authenticated.
But they may still not be allowed to create assets. That is authorisation.
```

## Password hashing

Never store passwords as plain text.

Instead:

```text
User password -> BCrypt hashing -> passwordHash stored in database
```

When the user logs in, Spring Security compares the entered password against the stored hash.

## JWT idea

JWT means JSON Web Token.

In this course, students only need the practical idea:

```text
The server gives the client a signed token after login.
The client sends the token on later requests.
The server checks the token before allowing access.
```

## Bearer token header

The client sends the token like this:

```http
Authorization: Bearer <token>
```

## 401 vs 403

```text
401 Unauthorized = You are not logged in, or your token is missing/invalid.
403 Forbidden = You are logged in, but your role is not allowed to do this action.
```

## Roles used in Day 9

```text
USER  = can view assets
ADMIN = can view and create assets
```

## Why we seed an admin user

Normal registration creates USER accounts only.

The seeded admin lets the trainer demonstrate role-based access without needing an admin creation screen.

```text
admin@example.com / Admin@12345
```
