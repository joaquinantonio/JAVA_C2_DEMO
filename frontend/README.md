# Day 11 Asset Tracker UI

This is the trainer demo for Day 11: React Fundamentals.

## What this demonstrates

- Vite + React project structure
- JSX
- Components and props
- State with `useState`
- Derived UI with filtering
- Effects with `useEffect`
- Loading and error states
- Layout, list screen, detail screen
- Basic backend connectivity using public Day 10 endpoints

## How to run

Start the Day 10 Spring Boot backend first:

```bash
mvn spring-boot:run
```

Then run the React app:

```bash
cd asset-tracker-ui-day11
npm install
npm run dev
```

Open:

```text
http://localhost:5173
```

The app uses Vite's dev server proxy so frontend requests to `/api/...` are forwarded to the Spring Boot backend on `http://localhost:8080`.

## Important teaching note

The asset list uses local sample data on Day 11. This keeps the focus on React fundamentals.

The app only fetches public API information from the Day 10 backend. Protected asset API fetching will be handled later after students learn login, routing, protected pages, and API data patterns.
