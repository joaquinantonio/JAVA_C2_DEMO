# Asset Tracker UI - Day 15

Day 15 adds frontend tests and a light end-to-end smoke test to the Day 14 React application.

## Install dependencies

```bash
npm install
```

## Run component/unit tests

```bash
npm run test
```

## Run tests in watch mode

```bash
npm run test:watch
```

## Run E2E smoke test

Start the Spring Boot backend first:

```bash
mvn spring-boot:run
```

Then run:

```bash
npm run test:e2e
```

The Playwright test starts the Vite frontend automatically and expects the backend at `http://localhost:8080`.
