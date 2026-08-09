# Day 16 Branch Notes - Updated

## Starting point

Start from the completed and tested Day 15 branch.

```bash
git checkout test/15
git checkout -b test/16
```

## Day 16 purpose

Day 16 focuses on AI-assisted coding, prompt engineering and safe refactoring.

The goal is not to add a major new feature. The goal is to improve existing code while keeping behaviour unchanged.

## What changed from the first Day 16 draft

The instructor materials have been expanded to include:

```text
- Prompt engineering for developers
- Bad prompt vs better prompt examples
- Larger prompt bank
- AI safety rules
- Generate → Explain → Test workflow
- AI output review checklist
- More detailed backend refactor instructions
- More detailed frontend refactor instructions
- Test hardening guidance
- Student submission guidance
```

## Backend changes

Updated:

```text
src/main/java/com/example/assettracker/service/AssetService.java
```

The backend refactor extracts helper methods such as:

```text
findAssetOrThrow
ensureAssetTagIsUniqueForCreate
ensureSerialNumberIsUniqueForCreate
ensureAssetTagIsUniqueForUpdate
ensureSerialNumberIsUniqueForUpdate
normalizeRequired
normalizeStatus
normalizeOptional
```

## Frontend changes

Updated:

```text
frontend/src/components/AssetFormWizard.jsx
```

Added:

```text
frontend/src/utils/assetFormValidation.js
frontend/src/utils/assetFormValidation.test.js
```

## Behaviour should stay the same

The following should still work exactly as before:

```text
- Login
- Protected routes
- Assets page
- Reports page
- Create asset form
- Edit asset form
- Form validation
- Optimistic status update
- Unit/component tests
- E2E smoke test
```

## Verification commands

Backend:

```bash
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
npm run test
npm run test:e2e
```

## Suggested commit message

```bash
git add .
git commit -m "Day 16 AI-assisted refactoring and prompt engineering"
git push origin test/16
```

## Instructor reminder

Tell students:

```text
A good prompt can produce useful code. A good developer still reviews, tests and owns that code.
```

---

# v3 correction notes

This regenerated package includes the fixes discovered after applying the Day 16 frontend changes.

## Fixed in v3

```text
1. Corrected httpClient.js so JSON request bodies and Content-Type headers are handled.
2. Corrected api.js so paged asset URLs are built cleanly.
3. Preserved stale-token handling for 401 responses.
4. Preserved Assets NavLink end behaviour.
5. Restored the improved optimistic update button styling.
6. Preserved Vitest e2e exclusion.
7. Preserved precise Playwright locators.
8. Added post-implementation preservation notes to instructor materials.
```

## Teaching point

```text
This is exactly why Day 16 exists: AI-assisted refactoring must be reviewed, tested and compared against the previous working behaviour.
```
