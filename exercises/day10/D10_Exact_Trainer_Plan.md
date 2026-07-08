# Day 10 Exact Trainer Plan

## Day Title

API Quality, Versioning, Documentation & Aggregation

## Main Learning Outcomes

By the end of Day 10, students should be able to:

1. Explain why APIs use versioned routes.
2. Add a `/api/v1` endpoint without breaking older endpoints.
3. Create a simple API documentation endpoint.
4. Use MongoDB aggregation to group and count documents.
5. Improve API quality by validating pagination and sorting inputs.
6. Review backend readiness before frontend integration.

## Suggested Schedule

### 9:00 - 9:30 Recap Day 9

Review:

- register
- login
- JWT
- Bearer token
- USER vs ADMIN
- protected endpoints

### 9:30 - 10:15 API Quality Concepts

Explain:

- APIs should be predictable
- errors should be consistent
- versioning helps avoid breaking frontend/client apps
- reports should be calculated by backend/database, not by downloading all data

### 10:15 - 11:00 Versioning Demo

Add:

```text
/api/v1/info
/api/v1/assets
```

Keep old `/api/assets` routes working.

### 11:00 - 12:00 API Documentation Demo

Add:

```text
GET /api/docs
```

Explain that this is a simple classroom-friendly documentation endpoint.

### 12:00 - 1:00 API Quality Validation

Improve pagination/sorting validation:

- page must be 0 or greater
- size must be 1 to 50
- sortBy must be an allowed field
- direction must be asc or desc

### 2:00 - 3:00 Aggregation Theory and Demo

Explain:

```text
group by status → count assets
```

Build:

```text
GET /api/v1/reports/assets-by-status
```

### 3:00 - 3:45 More Report Endpoints

Build:

```text
GET /api/v1/reports/assets-by-category
GET /api/v1/reports/assets-by-location
```

### 4:00 - 4:40 Student Exercise

Students apply the same concept to Support Desk Ticket API:

```text
/api/v1/tickets
/api/v1/reports/tickets-by-status
/api/v1/reports/tickets-by-priority
/api/docs
```

### 4:40 - 5:00 Milestone 1 Backend Review

Review student backend readiness before React frontend.
