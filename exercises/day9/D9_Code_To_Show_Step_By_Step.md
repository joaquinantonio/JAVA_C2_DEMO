# Day 9 Code To Show Step By Step

## Before Exercise 1

Show only the Day 8 code structure:

```text
model/Asset.java
repository/AssetRepository.java
service/AssetService.java
controller/AssetController.java
```

Explain that Day 8 secured data quality, but not access.

## Step 1 - Add dependencies

Show the new dependencies in `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

Tell students:

```text
Security dependency changes the behaviour of the application immediately.
If we add it without configuration, Spring protects everything by default.
```

## Step 2 - Add AppUser model

Show:

```text
model/AppUser.java
```

Focus on:

```text
email
passwordHash
role
```

Do not go too deep into JWT yet.

## Step 3 - Add repository

Show:

```text
repository/AppUserRepository.java
```

Important methods:

```java
Optional<AppUser> findByEmailIgnoreCase(String email);
boolean existsByEmailIgnoreCase(String email);
```

## Step 4 - Add DTOs

Show:

```text
RegisterRequest
LoginRequest
AuthResponse
```

Explain DTO separation again.

## Step 5 - Add password hashing and AuthService

Show:

```text
AuthService.register()
AuthService.login()
```

Focus on:

```java
passwordEncoder.encode(request.getPassword())
```

and:

```java
authenticationManager.authenticate(...)
```

## Step 6 - Add JwtService

Show only the main method:

```java
generateToken(AppUser user)
```

Explain claims:

```text
subject = email
userId = user id
role = USER or ADMIN
```

## Step 7 - Add SecurityConfig

Show endpoint rules first:

```java
.requestMatchers("/api/health").permitAll()
.requestMatchers("/api/auth/**").permitAll()
.requestMatchers(HttpMethod.GET, "/api/assets/**").hasAnyRole("USER", "ADMIN")
.requestMatchers(HttpMethod.POST, "/api/assets").hasRole("ADMIN")
.anyRequest().authenticated()
```

Only after that, explain JWT encoder/decoder.

## Step 8 - Add AuthController

Show:

```text
POST /api/auth/register
POST /api/auth/login
```

## Step 9 - Add UserDataSeeder

Show seeded admin:

```text
admin@example.com / Admin@12345
```

Explain this is demo-only.

## Step 10 - Test with HTTP file

Use:

```text
requests/day09-auth.http
```

Test in this order:

```text
GET assets without token
Register user
Login user
GET assets with user token
POST asset with user token
Login admin
POST asset with admin token
```
