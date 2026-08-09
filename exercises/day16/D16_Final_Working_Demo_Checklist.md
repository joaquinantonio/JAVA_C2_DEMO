# Day 16 Final Working Demo Checklist - Updated

## Pre-class setup

- [ ] Day 15 branch is working.
- [ ] Backend starts with `mvn spring-boot:run`.
- [ ] Frontend starts with `npm run dev`.
- [ ] `npm run test` passes before refactoring.
- [ ] `npm run test:e2e` passes before refactoring.
- [ ] Day 16 changed files are ready.
- [ ] Prompt bank is open and ready for demo.

## Prompt engineering section

- [ ] Explained why vague prompts are risky.
- [ ] Explained prompt anatomy: context, task, constraints, expected output, tests, review.
- [ ] Showed bad prompt vs better prompt.
- [ ] Students improved one weak prompt.
- [ ] Explained why constraints prevent AI from changing behaviour.

## AI safety section

- [ ] Explained that AI-generated code must be reviewed.
- [ ] Explained not to paste secrets into AI tools.
- [ ] Explained not to paste sensitive data into AI tools.
- [ ] Explained developer ownership of generated code.
- [ ] Explained that AI can be confidently wrong.

## Backend refactor demo

- [ ] Showed `AssetService.java` before refactor.
- [ ] Identified repeated logic.
- [ ] Explained public methods must stay the same.
- [ ] Added/explained `findAssetOrThrow`.
- [ ] Added/explained create duplicate helper methods.
- [ ] Added/explained update duplicate helper methods.
- [ ] Added/explained normalisation helper methods.
- [ ] Confirmed create/update behaviour should not change.
- [ ] Tested with `requests/day16-ai-refactor.http`.

## Frontend refactor demo

- [ ] Showed `AssetFormWizard.jsx` before refactor.
- [ ] Explained why validation inside large component is harder to test.
- [ ] Added `assetFormValidation.js`.
- [ ] Added `assetFormValidation.test.js`.
- [ ] Updated `AssetFormWizard.jsx` to use validation utility.
- [ ] Confirmed UI, CSS classes, routes and payloads stay the same.
- [ ] Ran `npm run test`.

## Test hardening

- [ ] Explained weak tests vs strong tests.
- [ ] Showed validation edge cases.
- [ ] Explained why tests should check behaviour, not implementation details.
- [ ] Ran final frontend unit/component tests.
- [ ] Ran final Playwright E2E smoke test.

## Manual checks

- [ ] Login works.
- [ ] Dashboard loads.
- [ ] Assets page loads.
- [ ] Reports page loads.
- [ ] Create asset form works.
- [ ] Edit asset form works.
- [ ] Validation errors still appear.
- [ ] Optimistic status update still works.
- [ ] Protected pages still handle auth correctly.

## Student submission

- [ ] Prompt used.
- [ ] Refactored code.
- [ ] Tests added/updated.
- [ ] Test evidence.
- [ ] Before/after rationale.
- [ ] AI reflection.

## Instructor wrap-up

- [ ] Asked students what AI got right.
- [ ] Asked students what AI got wrong.
- [ ] Asked students which constraints mattered most.
- [ ] Reinforced: AI speeds up development, tests make it safer.

---

# Post-implementation preservation checklist

Before freezing Day 16, verify:

```text
[ ] Login works with admin@example.com / Admin@12345
[ ] httpClient.js converts body objects to JSON
[ ] httpClient.js adds Content-Type: application/json when body exists
[ ] httpClient.js attaches Authorization: Bearer <token> when token exists
[ ] buildQueryString returns query text without a leading ?
[ ] fetchPagedAssets builds /api/v1/assets/paged?page=...
[ ] stale or expired auth is cleared when protected APIs return 401
[ ] Assets NavLink uses end
[ ] Optimistic update buttons show only one active status
[ ] Vitest excludes e2e/**
[ ] npm run test passes
[ ] npm run test:e2e passes
[ ] Manual optimistic update still works after the refactor
[ ] Frontend validation extraction did not change form behaviour
[ ] Backend AssetService refactor did not change endpoint behaviour
```

Instructor note:

```text
If any item fails, the refactor is not finished even if the code looks cleaner.
```


## Final test patches confirmed

- [ ] `frontend/src/test/testUtils.jsx` stores `expiresAt` in mock auth.
- [ ] `frontend/src/pages/AssetsPage.test.jsx` uses `findByRole('button', { name: /LAP-2026-001/i })`.
- [ ] `frontend/src/pages/AssetsPage.test.jsx` checks default GET behaviour using `options.method ?? 'GET'`.
- [ ] `npm run test` passes with all 7 test files and 16 tests.
- [ ] `npm run test:e2e` passes.

Instructor note:

```text
The final two unit-test fixes are useful teaching moments. The application worked, but tests needed to be updated after the refactor so they matched the new auth shape and avoided brittle implementation checks.
```
