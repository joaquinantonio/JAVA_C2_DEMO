# Asset Tracker UI - Day 14

Day 14 adds a frontend data layer on top of the working Day 13 application.

## Focus

- API abstraction layer
- shared data context
- reducer-based state management
- page cache
- backend pagination
- client-side filters for the current page
- optimistic status updates
- lazy-loaded route modules

## Style freeze

The general Day 12/13 visual style is intentionally preserved. Day 14 focuses on data behaviour, not redesign.

## Run

Start Spring Boot first:

```bash
mvn spring-boot:run
```

Then run the frontend:

```bash
cd frontend
npm install
npm run dev
```

Login with:

```text
admin@example.com
Admin@12345
```
