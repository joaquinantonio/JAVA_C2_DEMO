# Day 15 Final Working Demo Checklist

## Unit and component test setup

- [ ] `frontend/package.json` has Vitest scripts
- [ ] `frontend/package.json` has testing dependencies
- [ ] `frontend/vite.config.js` has `test.environment = 'jsdom'`
- [ ] `frontend/src/test/setup.js` exists
- [ ] `npm run test` starts correctly

## Unit tests

- [ ] `filterAssets()` is tested
- [ ] `countByStatus()` is tested
- [ ] `buildQueryString()` is tested
- [ ] `apiRequest()` headers/body behaviour is tested

## Component tests

- [ ] `SummaryCards` displays expected labels and counts
- [ ] `ProtectedRoute` redirects unauthenticated users
- [ ] `ProtectedRoute` allows authenticated users
- [ ] `AssetFormWizard` displays validation errors
- [ ] `AssetFormWizard` submits valid payload
- [ ] `AssetsPage` displays mocked backend data

## E2E smoke test

- [ ] Spring Boot backend is running
- [ ] Playwright is installed
- [ ] E2E test opens login page
- [ ] E2E test logs in as admin
- [ ] E2E test opens dashboard
- [ ] E2E test opens assets page
- [ ] E2E test creates an asset
- [ ] E2E test sees success message

## Milestone 2 evidence

- [ ] Screenshot or terminal output of passing Vitest tests
- [ ] Screenshot or terminal output of passing Playwright test
- [ ] Screenshot of successful login
- [ ] Screenshot of protected page
- [ ] Screenshot of form validation
- [ ] Screenshot of successful create/update

## Style freeze check

- [ ] Existing Day 14 dashboard style remains the same
- [ ] Existing card sizes remain the same
- [ ] Existing navigation remains the same
- [ ] No unnecessary redesign was introduced
