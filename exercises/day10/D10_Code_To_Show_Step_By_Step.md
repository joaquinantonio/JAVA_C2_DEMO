# Day 10 Code To Show Step By Step

## Step 1: Show the Current Day 9 State

Remind students:

```text
Day 9 gave us authentication and authorisation.
```

Show:

```http
POST /api/auth/login
GET /api/assets
```

## Step 2: Add API Version Info

Create:

```text
ApiInfoController.java
```

Endpoint:

```http
GET /api/v1/info
```

Teaching point:

```text
Versioning gives clients a stable contract.
```

## Step 3: Add Versioned Asset Controller

Create:

```text
AssetV1Controller.java
```

Endpoint examples:

```http
GET /api/v1/assets
GET /api/v1/assets/paged
POST /api/v1/assets
```

Teaching point:

```text
We can introduce versioned routes while keeping old routes alive.
```

## Step 4: Update SecurityConfig

Permit public docs/info:

```java
.requestMatchers("/api/docs/**").permitAll()
.requestMatchers("/api/v1/info").permitAll()
```

Protect versioned routes:

```java
.requestMatchers(HttpMethod.GET, "/api/v1/assets/**").hasAnyRole("USER", "ADMIN")
.requestMatchers(HttpMethod.POST, "/api/v1/assets").hasRole("ADMIN")
.requestMatchers(HttpMethod.GET, "/api/v1/reports/**").hasAnyRole("USER", "ADMIN")
```

## Step 5: Add API Docs Endpoint

Create:

```text
ApiDocsController.java
```

Endpoint:

```http
GET /api/docs
```

Teaching point:

```text
Documentation helps frontend developers and testers know how to use the API.
```

## Step 6: Add Pagination/Sort Validation

Create:

```text
InvalidRequestException.java
```

Add checks in `AssetService`:

```text
page >= 0
1 <= size <= 50
sortBy must be allowed
direction must be asc or desc
```

## Step 7: Add Aggregation Report Service

Create:

```text
ReportCountResponse.java
AssetReportService.java
```

Use:

```java
Aggregation.group("status").count().as("count")
```

## Step 8: Add Report Controller

Create:

```text
ReportController.java
```

Endpoints:

```http
GET /api/v1/reports/assets-by-status
GET /api/v1/reports/assets-by-category
GET /api/v1/reports/assets-by-location
```

## Step 9: Test with HTTP File

Use:

```text
requests/day10-api-quality.http
```

Show:

- public docs endpoint
- protected v1 endpoint
- invalid sort returns 400
- report endpoint returns grouped counts
