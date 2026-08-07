# Day 15 Code To Show Step By Step - Frontend Testing & E2E Smoke

This file gives the exact code sequence to show during Day 15.

Do not paste all test files at once. Show the code gradually, run tests often, and explain why each file exists.

---

## Starting Point

Start from the working Day 14 branch.

```bash
git checkout test/14
git checkout -b test/15
```

Explain:

```text
Day 14 made the app more realistic with data fetching, context, reducer, pagination, cache and optimistic updates.
Day 15 adds tests so we can trust the app before refactoring in Day 16.
```

---

# Step 1 - Update `package.json`

## File to edit

```text
frontend/package.json
```

## Add scripts

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

## Add dependencies

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

## Explain

```text
npm run test runs Vitest.
npm run test:e2e runs Playwright.
They are different tools for different testing levels.
```

---

# Step 2 - Update `vite.config.js`

## File to edit

```text
frontend/vite.config.js
```

## Show this code

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

## Explain

```text
jsdom gives React a browser-like environment during Vitest tests.
setupFiles loads test setup before each test file.
include keeps Vitest focused on src test files.
exclude prevents Vitest from running Playwright E2E tests.
```

## Important mistake to show

If students see:

```text
Playwright Test did not expect test() to be called here.
```

Explain:

```text
Vitest is accidentally running a Playwright test file. Exclude the e2e folder.
```

---

# Step 3 - Add Test Setup

## File to add

```text
frontend/src/test/setup.js
```

## Code

```js
import '@testing-library/jest-dom/vitest';
```

## Explain

This enables:

```js
expect(element).toBeInTheDocument();
expect(element).toBeVisible();
```

---

# Step 4 - Add Optional Test Utility

## File to add

```text
frontend/src/test/testUtils.jsx
```

## Example

```jsx
import { MemoryRouter } from 'react-router';
import { render } from '@testing-library/react';

export function renderWithRouter(ui, { route = '/' } = {}) {
  window.history.pushState({}, 'Test page', route);

  return render(
    <MemoryRouter initialEntries={[route]}>
      {ui}
    </MemoryRouter>
  );
}
```

## Explain

```text
Some components depend on routing. A helper avoids repeating router setup in many test files.
```

---

# Step 5 - Unit Test: Asset Utility

## File to add

```text
frontend/src/utils/assets.test.js
```

## Code pattern

```js
import { describe, expect, it } from 'vitest';
import { filterAssets } from './assets.js';

const assets = [
  {
    id: 'A001',
    assetTag: 'LAP-2026-001',
    name: 'Dell Latitude 5440',
    category: 'Laptop',
    status: 'AVAILABLE',
    location: 'HQ Level 3'
  },
  {
    id: 'A002',
    assetTag: 'MON-2026-001',
    name: 'Samsung Monitor',
    category: 'Monitor',
    status: 'ASSIGNED',
    location: 'HQ Level 2'
  }
];

describe('filterAssets', () => {
  it('returns all assets when status filter is ALL', () => {
    const result = filterAssets(assets, '', 'ALL');

    expect(result).toHaveLength(2);
  });

  it('filters assets by status', () => {
    const result = filterAssets(assets, '', 'AVAILABLE');

    expect(result).toHaveLength(1);
    expect(result[0].assetTag).toBe('LAP-2026-001');
  });

  it('searches assets by text', () => {
    const result = filterAssets(assets, 'monitor', 'ALL');

    expect(result).toHaveLength(1);
    expect(result[0].assetTag).toBe('MON-2026-001');
  });
});
```

## Explain

```text
This is a unit test. It does not render React and does not call the backend.
```

---

# Step 6 - Component Test: SummaryCards

## File to add

```text
frontend/src/components/SummaryCards.test.jsx
```

## Code pattern

```jsx
import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import SummaryCards from './SummaryCards.jsx';

const assets = [
  { id: 'A001', status: 'AVAILABLE' },
  { id: 'A002', status: 'ASSIGNED' },
  { id: 'A003', status: 'MAINTENANCE' }
];

describe('SummaryCards', () => {
  it('shows summary labels', () => {
    render(<SummaryCards assets={assets} />);

    expect(screen.getByText('Total Assets')).toBeInTheDocument();
    expect(screen.getByText('Available')).toBeInTheDocument();
    expect(screen.getByText('Assigned')).toBeInTheDocument();
    expect(screen.getByText('Maintenance')).toBeInTheDocument();
  });
});
```

## Explain

```text
This tests visible component output, not CSS implementation.
```

---

# Step 7 - Component Test: ProtectedRoute

## File to add

```text
frontend/src/components/ProtectedRoute.test.jsx
```

## Test behaviour

```text
Unauthenticated user -> redirect to login.
Authenticated user -> protected content appears.
```

## Explain

```text
Frontend protected routing improves the user experience, but the backend still protects the API.
```

---

# Step 8 - Component Test: AssetFormWizard

## File to add

```text
frontend/src/components/AssetFormWizard.test.jsx
```

## Test behaviour

```text
Empty required fields should block progression.
A valid form should call the submit handler.
```

## Code pattern

```jsx
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
import AssetFormWizard, { emptyAssetForm } from './AssetFormWizard.jsx';

describe('AssetFormWizard', () => {
  it('does not submit when required fields are empty', async () => {
    const user = userEvent.setup();
    const handleSubmit = vi.fn();

    render(
      <AssetFormWizard
        mode="create"
        initialValues={emptyAssetForm}
        onSubmit={handleSubmit}
        saving={false}
        serverError=""
        successMessage=""
      />
    );

    await user.click(screen.getByRole('button', { name: 'Continue' }));

    expect(handleSubmit).not.toHaveBeenCalled();
  });
});
```

## Explain

```text
Use userEvent because the test should behave like a real user.
```

---

# Step 9 - Page Test: AssetsPage

## File to add

```text
frontend/src/pages/AssetsPage.test.jsx
```

## Important fix

Avoid broad query:

```js
screen.getByText('LAP-2026-001')
```

because the same text may appear in both:

```text
1. Asset list row
2. Asset detail panel
```

Use:

```js
expect(
  await screen.findByRole('button', { name: /LAP-2026-001/i })
).toBeInTheDocument();
```

## Explain

```text
The test should identify the actual element we care about, not any matching text on the page.
```

---

# Step 10 - Add Playwright Config

## File to add

```text
frontend/playwright.config.js
```

## Code

```js
import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './e2e',
  timeout: 30000,
  use: {
    baseURL: 'http://localhost:5173',
    trace: 'on-first-retry'
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] }
    }
  ]
});
```

## Install browser

```bash
npx playwright install chromium
```

---

# Step 11 - Add E2E Smoke Test

## File to add

```text
frontend/e2e/day15-smoke.spec.js
```

## Corrected version

```js
import { test, expect } from '@playwright/test';

test('admin can login and create an asset through the protected UI', async ({ page }) => {
  const uniqueSuffix = Date.now();
  const assetTag = `E2E-${uniqueSuffix}`;

  await page.goto('/login');
  await expect(page.getByRole('heading', { name: 'Login to Asset Tracker' })).toBeVisible();

  await page.getByLabel('Email').fill('admin@example.com');
  await page.getByLabel('Password').fill('Admin@12345');
  await page.getByRole('button', { name: 'Login' }).click();

  await expect(page).toHaveURL(/\/app\/dashboard/);
  await expect(page.getByRole('heading', { name: /welcome/i })).toBeVisible();

  const mainNav = page.getByRole('navigation', { name: 'Main navigation' });

  await mainNav.getByRole('link', { name: 'Assets', exact: true }).click();
  await expect(page.getByText('Server pagination and cache controls')).toBeVisible();

  await mainNav.getByRole('link', { name: 'Asset Form', exact: true }).click();
  await expect(page.getByRole('heading', { name: 'Create a new asset' })).toBeVisible();

  await page.getByRole('textbox', { name: 'Asset Tag', exact: true }).fill(assetTag);
  await page.getByRole('textbox', { name: 'Asset Name', exact: true }).fill('E2E Test Camera');
  await page.getByRole('textbox', { name: 'Category', exact: true }).fill('Camera');
  await page.getByRole('textbox', { name: 'Serial Number', exact: true }).fill(`SN-${uniqueSuffix}`);

  await page.getByRole('button', { name: 'Continue' }).click();

  await page.getByRole('textbox', { name: 'Location', exact: true }).fill('Testing Room');

  await page.getByRole('button', { name: 'Continue' }).click();

  await page
    .getByLabel('I have reviewed the asset details and they are ready to submit.', { exact: true })
    .check();

  await page.getByRole('button', { name: 'Create Asset' }).click();

  await expect(page.getByText('Asset created successfully.')).toBeVisible();
});
```

## Explain

This test proves:

```text
Login works.
Protected dashboard loads.
Navigation works.
Asset form opens.
Form submission works.
Success message appears.
```

---

# Step 12 - Explain Reliable Playwright Locators

## Fragile heading text

Bad:

```js
page.getByRole('heading', { name: 'Welcome back' })
```

Better:

```js
page.getByRole('heading', { name: /welcome/i })
```

Why:

```text
The app may say Welcome, Admin User. The exact phrase is less important than reaching the dashboard.
```

## Duplicate links

Bad:

```js
page.getByRole('link', { name: 'Assets' })
```

Better:

```js
const mainNav = page.getByRole('navigation', { name: 'Main navigation' });
await mainNav.getByRole('link', { name: 'Assets', exact: true }).click();
```

Why:

```text
The page may contain both Assets and View Assets.
```

## Ambiguous labels

Bad:

```js
page.getByLabel('Location')
```

Better:

```js
page.getByRole('textbox', { name: 'Location', exact: true })
```

Why:

```text
A section can be labelled Location and status, while the input is labelled Location.
```

---

# Step 13 - Add Stale Token / 401 Handling

## Problem

The dashboard may still render because localStorage has an old token, but protected endpoints may return:

```text
401 Unauthorized
```

## File to edit

```text
frontend/src/services/httpClient.js
```

## Code change

```js
if (!response.ok) {
  const message = data?.message || `Request failed with status ${response.status}`;
  const error = new Error(message);
  error.status = response.status;
  throw error;
}
```

## File to edit

```text
frontend/src/context/AuthContext.jsx
```

Store an expiry timestamp:

```js
expiresAt: Date.now() + expiresInMinutes * 60 * 1000
```

When reading stored auth, clear it if:

```text
- token is missing
- expiresAt is missing
- Date.now() > expiresAt
```

## File to edit

```text
frontend/src/context/AssetDataContext.jsx
```

Use:

```js
const { token, logout } = useAuth();
```

Catch 401:

```js
if (error.status === 401) {
  logout();
  return;
}
```

## File to edit

```text
frontend/src/pages/ReportsPage.jsx
```

Use:

```js
const { token, logout } = useAuth();
```

Catch 401:

```js
if (err.status === 401) {
  logout();
  return;
}
```

## Explain

```text
Frontend route protection checks if a token exists.
Backend security checks if the token is valid.
```

---

# Step 14 - Fix Assets Nav Highlight

## File to edit

```text
frontend/src/components/AppShell.jsx
```

## Fix

```jsx
<NavLink to="/app/assets" end>Assets</NavLink>
<NavLink to="/app/assets/new">Asset Form</NavLink>
```

## Explain

```text
NavLink treats child routes as active by default. The end prop makes Assets active only on /app/assets.
```

---

# Step 15 - Improve Optimistic Update Button Styling

## File to edit

```text
frontend/src/components/OptimisticStatusControls.jsx
```

## Button logic

```jsx
<div className="action-row">
  {STATUSES.map((status) => {
    const isCurrentStatus = status === asset.status;

    return (
      <button
        key={status}
        type="button"
        className={
          isCurrentStatus
            ? 'status-action-button status-action-active'
            : 'status-action-button'
        }
        disabled={isUpdating || isCurrentStatus}
        aria-pressed={isCurrentStatus}
        onClick={() => onStatusChange(asset.id, status)}
      >
        {status}
      </button>
    );
  })}
</div>
```

## File to edit

```text
frontend/src/styles.css
```

## CSS

```css
.status-action-button {
  border: 1px solid #d9e2ef;
  border-radius: 999px;
  padding: 10px 16px;
  font-weight: 800;
  cursor: pointer;
  color: #334155;
  background: #f8fafc;
}

.status-action-button:hover:not(:disabled) {
  border-color: #3b82f6;
  background: #eff6ff;
}

.status-action-active {
  background: #3b82f6;
  border-color: #3b82f6;
  color: white;
}

.status-action-button:disabled {
  opacity: 1;
  cursor: not-allowed;
}

.status-action-button:focus-visible {
  outline: 3px solid #bfdbfe;
  outline-offset: 2px;
}
```

## Explain optimistic update

```text
The UI updates immediately before the backend confirms.
If the backend succeeds, the new status stays.
If the backend fails, the UI rolls back.
```

---

# Step 16 - Run Tests

## Unit/component tests

```bash
npm run test
```

Expected:

```text
All Vitest tests pass.
```

## E2E test

Terminal 1:

```bash
mvn spring-boot:run
```

Terminal 2:

```bash
cd frontend
npm run dev
```

Terminal 3:

```bash
cd frontend
npm run test:e2e
```

Expected:

```text
1 passed
```

---

# Step 17 - Map to Student Support Desk Project

| Asset Tracker Demo | Support Desk Student Project |
|---|---|
| Asset list test | Ticket list test |
| Summary cards | Ticket summary cards |
| Asset form validation | Ticket form validation |
| Protected asset route | Protected ticket route |
| Create asset E2E | Create ticket E2E |
| Asset status optimistic update | Ticket status optimistic update |

Student E2E flow:

```text
Login
Open protected Tickets page
Open Create Ticket form
Fill ticket fields
Submit
Confirm success message
Return to ticket list
```

---

# Step 18 - Review Questions

Ask students:

1. Which test was easiest to write?
2. Which test was hardest to write?
3. Why did Playwright complain about duplicate locators?
4. Why should we not test exact wording too much?
5. Why do we run Vitest and Playwright separately?
6. What does a 401 during frontend testing usually mean?
7. How did testing help us improve the UI?
8. Why is testing useful before refactoring?

---

# Closing Summary

Use this:

```text
Day 15 showed that testing is not only about proving that code works.
Testing also helped us discover unclear navigation state, stale authentication, fragile text assumptions and ambiguous locators.
```

Connect to Day 16:

```text
Tomorrow, when we use AI to refactor code, these tests will help us check whether the app still behaves correctly.
```
