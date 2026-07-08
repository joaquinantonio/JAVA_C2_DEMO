# Day 8 Concept Guide: Queries, Pagination, Indexes & Logging

## Query parameters

Query parameters are values added after `?` in the URL.

Example:

```text
GET /api/assets?status=AVAILABLE
```

The controller reads it with:

```java
@RequestParam(required = false) String status
```

## Path variable vs query parameter

| Type | Example | Use |
|---|---|---|
| Path variable | `/api/assets/{id}` | Identify one resource |
| Query parameter | `/api/assets?status=AVAILABLE` | Filter/control results |

## Pagination

Pagination means returning data in smaller pages instead of returning everything.

Example:

```text
GET /api/assets/paged?page=0&size=5
```

Useful because:

- faster response
- less memory usage
- better frontend performance
- easier user experience

## Sorting

Sorting controls order.

Example:

```text
GET /api/assets/paged?sortBy=name&direction=asc
```

## Indexes

An index is a database shortcut.

Good fields to index:

- fields used often for search
- fields used often for filtering
- fields that must be unique

Do not index everything. Indexes improve reads but also add storage and write overhead.

## Logging

Logging helps you see what the backend is doing.

Use logs for:

- important service actions
- query/filter values
- created records
- unexpected conditions

Avoid logging sensitive data such as passwords or tokens.
