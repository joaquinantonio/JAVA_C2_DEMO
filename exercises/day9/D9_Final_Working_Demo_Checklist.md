# Day 9 Final Working Demo Checklist

## Before running

Confirm MongoDB is running.

Confirm application properties include:

```properties
app.jwt.secret=...
app.jwt.expiration-minutes=60
```

## Start application

```bash
mvn spring-boot:run
```

## Expected startup behaviour

You should see logs for:

```text
Asset seeding completed.
Seeded user email=admin@example.com role=ADMIN
```

If the admin already exists, you should see:

```text
Seed user already exists: admin@example.com
```

## Test 1 - health endpoint

```http
GET /api/health
```

Expected:

```text
200 OK
```

## Test 2 - assets without token

```http
GET /api/assets
```

Expected:

```text
401 Unauthorized
```

## Test 3 - register normal user

```http
POST /api/auth/register
```

Expected:

```text
201 Created
Response contains token
Role is USER
```

## Test 4 - login normal user

```http
POST /api/auth/login
```

Expected:

```text
200 OK
Response contains token
```

## Test 5 - use USER token to view assets

```http
GET /api/assets
Authorization: Bearer <USER_TOKEN>
```

Expected:

```text
200 OK
```

## Test 6 - use USER token to create asset

```http
POST /api/assets
Authorization: Bearer <USER_TOKEN>
```

Expected:

```text
403 Forbidden
```

## Test 7 - login seeded admin

```http
POST /api/auth/login
```

Body:

```json
{
  "email": "admin@example.com",
  "password": "Admin@12345"
}
```

Expected:

```text
200 OK
Role is ADMIN
```

## Test 8 - use ADMIN token to create asset

```http
POST /api/assets
Authorization: Bearer <ADMIN_TOKEN>
```

Expected:

```text
201 Created
```

## Student proof of completion

Students submit screenshots or copied response output showing:

```text
1. Register success
2. Login success with token
3. 401 without token
4. 200 with token
5. 403 when role is not allowed
```
