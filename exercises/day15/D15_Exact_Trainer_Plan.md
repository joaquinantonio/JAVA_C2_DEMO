# Day 15 Exact Trainer Plan - Frontend Testing & E2E Smoke

## Day 15 Theme

Day 15 is about proving that the React frontend works before moving into AI-assisted refactoring on Day 16.

By this point, students have already built:

- React components
- protected routing
- login and logout
- form wizard
- backend form submission
- API client functions
- data context and reducer
- pagination and filtering
- optimistic status update

Day 15 should not introduce a major new feature. The goal is to add confidence.

Use this teaching phrase:

```text
Before we add more features, we need confidence that the current app still works.
```

Another useful phrase:

```text
Testing is not only for finding bugs. Testing also protects us when we refactor later.
```

---

## Learning Outcomes

By the end of Day 15, students should be able to:

1. Explain the difference between unit tests, component tests, page tests, and E2E smoke tests.
2. Set up Vitest in a Vite React project.
3. Use React Testing Library to test visible behaviour.
4. Use Playwright to run a browser-based smoke test.
5. Test protected route behaviour.
6. Test form validation behaviour.
7. Explain why Vitest and Playwright should be run separately.
8. Explain why stale JWTs can cause 401 errors.
9. Write more precise test locators.
10. Submit Milestone 2 evidence.

---

## Tools Used

### Vitest

Vitest is used for fast unit and component tests.

Use it for:

- utility functions
- small components
- protected route behaviour
- form validation
- page rendering with mocked data

Command:

```bash
npm run test
```

### React Testing Library

React Testing Library is used with Vitest to test components from the user's point of view.

Teaching principle:

```text
Test what the user can see or do, not private internal implementation first.
```

Examples:

```js
screen.getByRole('button', { name: 'Login' })
screen.getByText('Asset created successfully.')
screen.getByRole('textbox', { name: 'Location' })
```

### Playwright

Playwright is used for the E2E smoke test.

Use it to test:

- login
- protected dashboard
- assets page
- asset form
- successful create flow

Command:

```bash
npm run test:e2e
```

---

## Setup Commands

Run this inside the `frontend` folder:

```bash
cd frontend
npm install
npx playwright install chromium
npm run test
npm run test:e2e
```

For E2E testing, use three terminals.

### Terminal 1 - Backend

```bash
mvn spring-boot:run
```

### Terminal 2 - Frontend

```bash
cd frontend
npm run dev
```

### Terminal 3 - E2E Test

```bash
cd frontend
npm run test:e2e
```

The E2E test expects:

```text
Backend:  http://localhost:8080
Frontend: http://localhost:5173
```

---

# Full-Day Plan

## 9:00 - 9:20 | Recap Day 14

Review:

- API abstraction layer
- asset data context
- reducer-based state
- pagination
- cache
- filtering
- optimistic update
- rollback on failure

Ask:

```text
What could break if we refactor the frontend tomorrow?
```

Expected answers:

- login could break
- protected routes could break
- asset list might not load
- form might not submit
- optimistic update might not roll back
- stale token handling might fail

Then explain:

```text
Today we write tests to catch those problems before users do.
```

---

## 9:20 - 9:45 | Explain Testing Levels

Use this table:

| Test Type | What It Tests | Example |
|---|---|---|
| Unit Test | One function | `filterAssets()` |
| Component Test | One component | `SummaryCards` |
| Page Test | Page behaviour with mocked state/data | `AssetsPage` |
| E2E Smoke Test | Real browser flow | Login → Create Asset |

Explain:

```text
A unit test is fast and focused.
A component test checks what users see.
An E2E test checks whether the app works through a browser.
```

Important:

```text
We do not test everything with E2E. E2E tests are powerful but slower and more fragile.
```

---

## 9:45 - 10:15 | Add Testing Dependencies and Scripts

Edit:

```text
frontend/package.json
```

Add scripts:

```json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "test": "vitest run",
    "test:watch": "vitest",
    "test:e2e": "playwright test"
  }
}
```

Add dependencies:

```json
{
  "dependencies": {
    "@testing-library/jest-dom": "latest",
    "@testing-library/react": "latest",
    "@testing-library/user-event": "latest",
    "@playwright/test": "latest",
    "jsdom": "latest",
    "vitest": "latest"
  }
}
```

Explain:

```text
Vitest and Playwright are separate tools. Do not accidentally run Playwright tests inside Vitest.
```

---

## 10:15 - 10:45 | Configure Vitest

Edit:

```text
frontend/vite.config.js
```

Show:

```js
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  test: {
    environment: 'jsdom',
    setupFiles: './src/test/setup.js',
    include: ['src/**/*.test.{js,jsx}'],
    exclude: ['node_modules', 'dist', 'e2e/**', 'playwright.config.js']
  }
});
```

Explain:

```text
environment: jsdom gives React a browser-like testing environment.
setupFiles loads the test setup file.
include makes Vitest run only src test files.
exclude prevents Vitest from running Playwright E2E files.
```

Common error:

```text
Playwright Test did not expect test() to be called here.
```

Meaning:

```text
Vitest is accidentally running a Playwright file.
```

---

## 10:45 - 11:00 | Break

---

## 11:00 - 11:30 | Create Test Setup Files

Add:

```text
frontend/src/test/setup.js
```

Code:

```js
import '@testing-library/jest-dom/vitest';
```

Explain:

```text
This enables matchers like toBeInTheDocument() and toBeVisible().
```

Optional helper file:

```text
frontend/src/test/testUtils.jsx
```

Use it for shared wrappers such as `MemoryRouter` or provider setup.

---

## 11:30 - 12:00 | Unit Test: Asset Filtering

Add:

```text
frontend/src/utils/assets.test.js
```

Test:

- return all assets when status is `ALL`
- filter by status
- search by text

Teaching script:

```text
This is the easiest type of test because it does not render React and does not call the backend.
```

---

## 12:00 - 12:30 | Component Test: SummaryCards

Add:

```text
frontend/src/components/SummaryCards.test.jsx
```

Test visible output:

- Total Assets
- Available
- Assigned
- Maintenance

Explain:

```text
We are testing what appears on screen, not the CSS.
```

---

## 12:30 - 1:00 | Protected Route Test

Add:

```text
frontend/src/components/ProtectedRoute.test.jsx
```

Test:

```text
Unauthenticated user → redirect to login
Authenticated user → protected content visible
```

Explain:

```text
Frontend protected routes improve user experience. Backend JWT security is still required because users can bypass the frontend.
```

---

## 1:00 - 2:00 | Lunch

---

## 2:00 - 2:45 | Form Test: AssetFormWizard

Add:

```text
frontend/src/components/AssetFormWizard.test.jsx
```

Test:

- empty form blocks progression/submission
- valid form calls submit handler

Teaching point:

```text
Use userEvent because the test should behave like a real user.
```

---

## 2:45 - 3:15 | Page Test: AssetsPage

Add:

```text
frontend/src/pages/AssetsPage.test.jsx
```

Important fix:

Do not use this if the same text appears multiple times:

```js
screen.getByText('LAP-2026-001')
```

Use a precise query:

```js
expect(
  await screen.findByRole('button', { name: /LAP-2026-001/i })
).toBeInTheDocument();
```

Teaching point:

```text
A good test locator describes the element we actually care about.
```

---

## 3:15 - 3:30 | Break

---

## 3:30 - 4:15 | Playwright E2E Smoke Test

Add:

```text
frontend/playwright.config.js
frontend/e2e/day15-smoke.spec.js
```

E2E flow:

```text
Open login page
Login as admin
Confirm dashboard loaded
Open Assets page
Open Asset Form
Fill create form
Submit
Confirm success message
```

Use robust locators:

```js
const mainNav = page.getByRole('navigation', { name: 'Main navigation' });

await mainNav.getByRole('link', { name: 'Assets', exact: true }).click();
await mainNav.getByRole('link', { name: 'Asset Form', exact: true }).click();
await page.getByRole('textbox', { name: 'Location', exact: true }).fill('Testing Room');
```

Teaching point:

```text
Playwright strict mode is useful. If a locator matches more than one element, Playwright fails instead of guessing.
```

---

## 4:15 - 4:40 | Common Day 15 Issues

### Issue 1: Playwright browser missing

Error:

```text
Executable doesn't exist...
Please run npx playwright install
```

Fix:

```bash
npx playwright install chromium
```

### Issue 2: Vitest runs E2E files

Error:

```text
Playwright Test did not expect test() to be called here.
```

Fix Vitest config:

```js
include: ['src/**/*.test.{js,jsx}'],
exclude: ['node_modules', 'dist', 'e2e/**', 'playwright.config.js']
```

### Issue 3: Protected pages return 401

Cause:

```text
Old or expired JWT is still in localStorage.
```

Quick fix:

```js
localStorage.removeItem('assetTrackerAuth');
location.href = '/login';
```

Better app fix:

```text
Attach HTTP status to thrown errors and logout automatically on 401.
```

### Issue 4: Assets nav stays highlighted on Asset Form

Fix:

```jsx
<NavLink to="/app/assets" end>Assets</NavLink>
```

### Issue 5: Optimistic status buttons all look active

Fix:

```text
Current status button should be active.
Other status buttons should be neutral.
```

Explain:

```text
Optimistic update means the UI changes immediately, then rolls back if the backend request fails.
```

---

## 4:40 - 5:00 | Milestone 2 Evidence

Students should submit:

1. Screenshot of passed unit/component tests.
2. Screenshot of passed E2E smoke test.
3. Screenshot of login page.
4. Screenshot of protected dashboard.
5. Screenshot of asset/ticket list.
6. Screenshot of create form.
7. Screenshot of success message.
8. Short explanation of one test issue they fixed.

Closing statement:

```text
Testing is not separate from development. Testing helps us discover unclear UI, stale tokens, weak locators, and fragile assumptions.
```

---

# Final Checklist

## Frontend Tests

- [ ] `npm run test` passes
- [ ] Vitest does not run E2E tests
- [ ] utility test passes
- [ ] component test passes
- [ ] protected route test passes
- [ ] form validation test passes
- [ ] page test passes

## E2E Smoke Test

- [ ] Playwright browser installed
- [ ] backend running
- [ ] frontend running
- [ ] `npm run test:e2e` passes
- [ ] login works
- [ ] dashboard route works
- [ ] assets route works
- [ ] asset form works
- [ ] success message appears

## Post-testing Fixes

- [ ] stale localStorage token handled
- [ ] Assets NavLink uses `end`
- [ ] E2E locators are precise
- [ ] Location textbox locator is not ambiguous
- [ ] optimistic update buttons clearly show current status only

---

# Trainer Summary

Day 15 is successful when students understand this:

```text
Tests do not only confirm that the app works.
Tests also reveal unclear UI state, stale authentication, fragile wording, and ambiguous selectors.
```
