# Day 16 Post-Implementation Fixes and Preservation Notes

## Why this file exists

After applying Day 16 changes, we found two useful real-world issues:

```text
1. Login could return 401 because the API client abstraction did not convert request bodies into JSON.
2. The optimistic update buttons visually reverted to the older style.
```

These issues make Day 16 stronger as a lesson. They show that AI-assisted refactoring can introduce regressions even when the code looks cleaner.

## Fixed files

```text
frontend/src/services/httpClient.js
frontend/src/services/api.js
frontend/src/context/AuthContext.jsx
frontend/src/context/AssetDataContext.jsx
frontend/src/pages/ReportsPage.jsx
frontend/src/components/OptimisticStatusControls.jsx
frontend/src/components/AppShell.jsx
frontend/src/styles.css
frontend/vite.config.js
frontend/e2e/day15-smoke.spec.js
```

## Correct API client behaviour

The API helper must:

```text
- convert body objects to JSON
- add Content-Type: application/json when body exists
- add Authorization: Bearer <token> when token exists
- preserve error status codes on thrown errors
```

## Correct optimistic update behaviour

The optimistic update controls should show:

```text
Current status = active blue button
Other statuses = neutral buttons
Saving = buttons disabled temporarily
Failure = rollback to previous status
```

## Instructor explanation

Say this to students:

```text
A refactor is not just about cleaner code. A refactor is successful only when the behaviour remains correct.
```

## Commands to verify

```bash
cd frontend
npm run test
npm run test:e2e
```

Also manually verify:

```text
Login → Dashboard → Assets → Optimistic status update → Reports → Logout
```
