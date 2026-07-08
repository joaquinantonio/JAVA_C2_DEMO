# Day 10 Final Working Demo Checklist

Before ending Day 10, confirm these work:

## Public Endpoints

```text
[ ] GET /api/health
[ ] GET /api/v1/info
[ ] GET /api/docs
```

## Authentication

```text
[ ] POST /api/auth/login returns token
[ ] Protected endpoint rejects missing token
[ ] Protected endpoint accepts valid token
```

## Versioned Asset Endpoints

```text
[ ] GET /api/v1/assets
[ ] GET /api/v1/assets/paged?page=0&size=5&sortBy=assetTag&direction=asc
[ ] POST /api/v1/assets works with ADMIN token
```

## API Quality Validation

```text
[ ] Invalid page returns 400
[ ] Invalid size returns 400
[ ] Invalid sort field returns 400
[ ] Invalid direction returns 400
```

## Report Endpoints

```text
[ ] GET /api/v1/reports/assets-by-status
[ ] GET /api/v1/reports/assets-by-category
[ ] GET /api/v1/reports/assets-by-location
```

## Teaching Wrap-Up

Students should be able to explain:

```text
Why versioning matters
Why documentation matters
Why reports should be calculated by the backend/database
Why validation improves API quality
```
