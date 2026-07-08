# Day 11 Branch Notes

## Recommended branch

```bash
git checkout test/10
git checkout -b test/11
```

## Add frontend folder

Copy or create:

```text
asset-tracker-ui-day11/
```

Commit message suggestion:

```text
Day 11 React fundamentals UI
```

## Why this is a separate frontend folder

Days 6–10 focused on the Spring Boot backend.

Day 11 starts the React frontend. Keeping it in a separate folder makes the project easier to understand:

```text
repo-root/
├── src/                       Spring Boot backend
├── requests/                  HTTP test files
└── asset-tracker-ui-day11/    React frontend
```

## Backend dependency

Day 11 only calls public backend endpoints:

```text
GET /api/v1/info
GET /api/docs
```

Protected asset fetching is intentionally not included yet. That comes after routing, login, protected views and API data patterns.
