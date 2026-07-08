# Day 8 Exact Trainer Plan: Queries, Pagination, Indexes & Logging

## Big idea

Day 7 proved that Spring Boot can store data in MongoDB. Day 8 makes the API more useful by allowing clients to filter, paginate, sort, and observe what the backend is doing.

## Teaching pattern

- Show the concept using Asset Tracker.
- Students apply the concept to Support Desk Tickets.

## By the end of the day

Trainer demo should support:

- `GET /api/assets`
- `GET /api/assets?status=AVAILABLE`
- `GET /api/assets?category=Laptop`
- `GET /api/assets?location=HQ`
- `GET /api/assets/paged?page=0&size=5`
- `GET /api/assets/paged?page=0&size=5&sortBy=name&direction=asc`

Students should support:

- `GET /api/tickets?status=OPEN`
- `GET /api/tickets?priority=HIGH`
- `GET /api/tickets?category=Email`
- `GET /api/tickets/paged?page=0&size=5&sortBy=createdAt&direction=desc`

---

## 9:00–9:30 — Recap Day 7

Ask:

- What is a MongoDB document?
- What does `@Document` do?
- What does `MongoRepository` give us?
- What is the problem with returning all records every time?

Key message:

```text
Day 7: Store data.
Day 8: Retrieve the right data properly.
```

---

## 9:30–10:00 — Teach query parameters

Explain:

```text
Path variable: identifies one resource.
Query parameter: filters or controls the result.
```

Examples:

| URL | Meaning |
|---|---|
| `/api/assets/{id}` | Get one asset |
| `/api/assets?status=AVAILABLE` | Filter assets by status |
| `/api/assets?page=0&size=5` | Control page and size |

---

## 10:00–10:30 — Demo 1: Asset filtering

Show:

- `AssetRepository` query methods
- `@RequestParam`
- Service method choosing which repository method to call

Test:

- `GET /api/assets?status=AVAILABLE`
- `GET /api/assets?category=Laptop`
- `GET /api/assets?location=HQ`

---

## 10:45–11:30 — Exercise 1: Ticket filtering

Students add filtering for:

- status
- priority
- category

End result:

- `GET /api/tickets?status=OPEN`
- `GET /api/tickets?priority=HIGH`
- `GET /api/tickets?category=Email`

---

## 11:30–12:15 — Demo 2: Pagination and sorting

Explain:

```text
APIs should not return thousands of records at once.
Pagination lets the client request a smaller page of data.
Sorting controls the order of results.
```

Show:

- `Page`
- `Pageable`
- `PageRequest`
- `Sort`

Test:

- `GET /api/assets/paged?page=0&size=5`
- `GET /api/assets/paged?page=1&size=5`
- `GET /api/assets/paged?page=0&size=5&sortBy=name&direction=asc`

---

## 12:15–1:00 — Exercise 2: Ticket pagination and sorting

Students add:

- `GET /api/tickets/paged?page=0&size=5`
- `GET /api/tickets/paged?page=0&size=5&sortBy=createdAt&direction=desc`

---

## 2:00–2:45 — Demo 3: Seed data

Show `AssetDataSeeder`.

Explain:

```text
Query and pagination features are hard to test with only two records.
Seed data gives us enough data to test properly.
```

Important note:

- The seeder should skip if data already exists.
- Do not seed duplicate unique fields.

---

## 2:45–3:30 — Demo 4: Logging

Show service logs using SLF4J:

```java
private static final Logger logger = LoggerFactory.getLogger(AssetService.class);
```

Explain:

```text
Logs help us understand what the backend is doing while it runs.
```

Show the terminal logs while calling the API.

---

## 3:45–4:20 — Demo 5: Indexes

Explain:

```text
An index is a shortcut that helps the database find records faster.
```

Show:

```java
@Indexed(unique = true)
private String assetTag;
```

Discuss why these fields are indexed:

- assetTag
- serialNumber
- status
- category
- location

---

## 4:20–4:45 — Exercise 3: Ticket indexes and logging

Students add indexes to:

- status
- priority
- category
- createdBy
- createdAt

Students add at least two useful logs in `TicketService`.

---

## 4:45–5:00 — Exercise 4 and review

Students update their `.http` file and submit short notes:

- Which filters work?
- Which fields did they index?
- Why is pagination important?
- What logs appear in the terminal?
