# Day 15 Branch Notes

## Starting point

Start from the working Day 14 branch.

```bash
git checkout test/14
git checkout -b test/15
```

## Copy files

Copy the contents of:

```text
day15-changed-files-for-existing-test14-repo/
```

into the root of the repository.

## Files added or edited

### Edited

```text
frontend/package.json
frontend/vite.config.js
frontend/index.html
frontend/README.md
frontend/src/components/AppShell.jsx
```

### Added

```text
frontend/src/test/setup.js
frontend/src/test/testUtils.jsx
frontend/src/utils/assets.test.js
frontend/src/services/httpClient.test.js
frontend/src/components/SummaryCards.test.jsx
frontend/src/components/ProtectedRoute.test.jsx
frontend/src/components/AssetFormWizard.test.jsx
frontend/src/pages/AssetsPage.test.jsx
frontend/playwright.config.js
frontend/e2e/day15-smoke.spec.js
requests/day15-testing-smoke.http
```

## Commit message suggestion

```bash
git add .
git commit -m "Day 15 frontend tests and e2e smoke"
git push origin test/15
```

## Important reminder

If the E2E test fails with backend connection errors, check that Spring Boot is running on port `8080` and the Vite proxy still points to `http://localhost:8080`.
