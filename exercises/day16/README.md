# Day 16 AI-Assisted Coding, Prompt Engineering & Refactoring Package v3

This regenerated Day 16 package includes the original AI-assisted refactoring materials plus the post-testing fixes discovered during implementation.

## Day 16 theme

```text
Use AI to improve code, but verify everything with tests and manual checks.
```

## Main topics

```text
1. Prompt engineering for developers
2. Safe AI-assisted coding
3. Generate → Explain → Test workflow
4. Backend service refactoring
5. Frontend validation extraction
6. Test hardening
7. Regression prevention
8. Before/after rationale writing
```

## Important v3 fixes included

```text
- Corrected API client JSON body handling
- Corrected Content-Type handling
- Preserved Authorization bearer token handling
- Preserved stale-token 401 handling
- Restored optimistic update button styling
- Preserved Assets NavLink end behaviour
- Preserved Vitest and Playwright testing fixes
- Added instructor notes about AI refactor regressions
```

## Package structure

```text
day16_ai_refactoring_package_v3/
├── frontend-day16/
│   └── Full trainer frontend with Day 16 refactor and fixes
├── day16-changed-files-for-existing-test15-repo/
│   └── Files to copy into the existing Day 15 repo
├── docs/
│   ├── day16-backend-refactor-rationale.md
│   └── day16-frontend-refactor-rationale.md
├── exercise-md-files/
│   ├── D16_Exercise_00_Prompt_Engineering_Warmup.md
│   ├── D16_Exercise_01_AI_Refactor_Safety_Checklist.md
│   ├── D16_Exercise_02_Backend_Ticket_Service_Refactor.md
│   ├── D16_Exercise_03_Extract_Ticket_Form_Validation.md
│   ├── D16_Exercise_04_Generate_Then_Harden_Tests.md
│   ├── D16_Exercise_05_Before_After_Diff_Rationale.md
│   └── D16_Exercise_06_AI_Reflection.md
├── requests/
│   └── day16-ai-refactor.http
├── D16_Exact_Trainer_Plan.md
├── D16_Code_To_Show_Step_By_Step.md
├── D16_AI_Assisted_Refactoring_Guide.md
├── D16_Prompt_Bank.md
├── D16_Final_Working_Demo_Checklist.md
├── D16_Post_Implementation_Fixes.md
└── D16_Branch_Notes.md
```

## Recommended branch flow

```bash
git checkout test/15
git checkout -b test/16
```

Copy the contents of:

```text
day16-changed-files-for-existing-test15-repo/
```

into the root of your repo.

## Verification commands

Backend:

```bash
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm run test
npm run test:e2e
```

## Teaching note

Do not hide the regressions discovered during implementation. Use them as the lesson:

```text
AI can help refactor faster, but it can also accidentally undo working behaviour. Tests and developer review are mandatory.
```


## v4 finalisation note

This v4 package includes the final confirmed test patches after classroom-style implementation:

```text
1. testUtils.jsx now stores expiresAt in mock auth.
2. AssetsPage.test.jsx uses a precise asset-row button locator.
3. AssetsPage.test.jsx accepts default fetch GET behaviour instead of requiring method: 'GET'.
4. D16_Final_Test_Patches.md documents the final fixes and teaching lesson.
```

Confirmed target result after applying the package:

```bash
cd frontend
npm run test
npm run test:e2e
```

Both should pass when the backend/frontend setup matches Day 15/16.
