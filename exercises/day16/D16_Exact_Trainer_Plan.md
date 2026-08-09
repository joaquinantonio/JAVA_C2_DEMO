# Day 16 Exact Trainer Plan - AI-Assisted Coding, Prompt Engineering & Refactoring

## Day 16 positioning

Day 16 starts the AI-assisted development part of the course. By this point, students have already built a meaningful full-stack application:

```text
Spring Boot API → MongoDB → JWT security → React routing → protected views → forms → data fetching → tests
```

This is the correct time to teach AI-assisted coding because students now have real code to reason about. Avoid using toy examples. Use the existing Asset Tracker project as the trainer demo and the Support Desk Ticket project as the student exercise.

## Main teaching message

```text
AI is useful when the developer gives it context, constraints and tests. AI is risky when the developer blindly accepts generated code.
```

## Learning outcomes

By the end of Day 16, students should be able to:

1. Write a coding prompt with context, task, constraints and expected output.
2. Ask AI to explain unfamiliar code before changing it.
3. Ask AI to suggest safe refactoring without changing behaviour.
4. Review AI-generated code critically.
5. Ask AI to generate tests for changed logic.
6. Avoid sharing secrets, tokens, passwords or sensitive data with AI tools.
7. Refactor backend service logic into clearer helper methods.
8. Refactor frontend validation logic into a reusable utility.
9. Run tests to prove behaviour still works.
10. Write a short before/after rationale explaining what improved.

## Files used in the trainer demo

### Backend files

```text
src/main/java/com/example/assettracker/service/AssetService.java
requests/day16-ai-refactor.http
docs/day16-backend-refactor-rationale.md
```

### Frontend files

```text
frontend/src/components/AssetFormWizard.jsx
frontend/src/utils/assetFormValidation.js
frontend/src/utils/assetFormValidation.test.js
docs/day16-frontend-refactor-rationale.md
```

### Instructor files

```text
D16_AI_Assisted_Refactoring_Guide.md
D16_Prompt_Bank.md
D16_Code_To_Show_Step_By_Step.md
D16_Final_Working_Demo_Checklist.md
```

---

# Suggested day schedule

## 9:00 - 9:20 Recap Day 15 and introduce Day 16

### Recap

Review what Day 15 proved:

```text
- Unit tests can check utility logic.
- Component tests can check UI behaviour.
- Protected route tests can check auth flow.
- E2E tests can check the real user path through the app.
```

### Link to Day 16

Say:

```text
Yesterday, we created tests so we have confidence. Today, we use that confidence to refactor safely with AI assistance.
```

### Explain refactoring

Use this explanation:

```text
Refactoring means changing the internal structure of code without changing what the app does from the user's point of view.
```

Give examples:

```text
Good refactor:
- Extract duplicate code into a helper method.
- Move validation rules into a utility file.
- Rename a private helper method for clarity.

Not a refactor:
- Changing an endpoint URL.
- Changing the response JSON.
- Changing the button behaviour.
- Removing validation.
```

### Files to show

Show:

```text
frontend/src/components/AssetFormWizard.jsx
src/main/java/com/example/assettracker/service/AssetService.java
```

Explain that these files work, but they are good examples for refactoring because they contain logic that can be made clearer.

---

## 9:20 - 10:10 Prompt engineering for developers

This is the new major addition to Day 16.

### Teach the anatomy of a good coding prompt

Write this on the board:

```text
Context → Task → Constraints → Expected output → Tests → Review
```

Explain each part:

### 1. Context

What project is this? What framework? What does the code do?

Example:

```text
I am working on a Spring Boot REST API for an Asset Tracker app. The service uses MongoRepository and returns DTOs to the controller.
```

### 2. Task

What do you want AI to do?

Example:

```text
Refactor this service to reduce duplication and improve readability.
```

### 3. Constraints

What must AI not change?

Example:

```text
Do not change public method names, endpoint behaviour, DTO fields, exception types or validation rules.
```

### 4. Expected output

What format should AI return?

Example:

```text
Return the refactored code, a summary of changes, risks introduced and tests to run.
```

### 5. Tests

Ask AI how to prove the change works.

Example:

```text
Suggest tests that prove create and update behaviour are unchanged.
```

### 6. Review

Ask AI to critique its own output.

Example:

```text
Review your answer and tell me whether you changed any public API behaviour.
```

### Bad prompt vs better prompt

#### Bad prompt

```text
Fix this code.
```

Why it is bad:

```text
- No context.
- No constraint.
- No expected output.
- AI may rewrite too much.
```

#### Better prompt

```text
I am working on a Spring Boot service class in an Asset Tracker API.

Task:
Refactor the service to improve readability and reduce duplication.

Constraints:
- Do not change public method names.
- Do not change endpoint behaviour.
- Do not change DTO fields.
- Do not change exception messages unless necessary.
- Do not add new dependencies.
- Keep the code understandable for beginner Java students.

Return:
1. Refactored code
2. Explanation of each change
3. Risks introduced
4. Tests I should run
```

### Mini classroom activity

Ask students to improve this weak prompt:

```text
Make my React form better.
```

Expected improved version:

```text
I have a React form wizard for creating and editing support tickets.
Move validation logic into a separate utility function.
Keep the same fields, CSS classes, route paths and submit payload.
Return the changed files and Vitest tests for the validation function.
```

---

## 10:10 - 10:35 AI safety and code ownership

### Explain the safety rules

Use this section before any AI demo.

```text
AI-generated code is not automatically correct, secure or legal to use.
```

### Never paste into AI tools

```text
- Real passwords
- JWT tokens
- API keys
- Database connection strings
- Customer data
- Private company data
- Production logs with personal information
- Full confidential repositories unless your organisation permits it
```

### Student-friendly explanation

Say:

```text
If you would not post it in a public forum, do not paste it into an AI tool without permission.
```

### Developer responsibility

Explain:

```text
AI is the assistant. The developer is the owner of the final decision.
```

Students should always:

```text
1. Read generated code.
2. Compare it to existing behaviour.
3. Run tests.
4. Check security-sensitive parts.
5. Commit small changes.
6. Document what changed.
```

---

## 10:35 - 11:00 Introduce the Generate → Explain → Test workflow

This is the core Day 16 workflow.

### Step 1: Generate

Ask AI for a proposed change.

### Step 2: Explain

Ask AI to explain each change and why it helps.

### Step 3: Test

Ask AI what tests prove the behaviour is unchanged.

### Step 4: Review

The human developer reviews the AI output.

### Step 5: Apply selectively

Do not paste everything blindly. Use only the parts that are safe.

### Step 6: Run tests

Run unit/component/E2E tests from Day 15.

### Trainer phrase

```text
Generate is not the finish line. It is the starting point for review.
```

---

## 11:00 - 12:15 Backend AI-assisted refactoring demo

### File to show first

```text
src/main/java/com/example/assettracker/service/AssetService.java
```

### What to explain before changing code

Ask students to identify repeated patterns:

```text
- Trimming required string fields
- Checking duplicate asset tags
- Checking duplicate serial numbers
- Finding asset by id
- Validating status
- Mapping Asset to AssetResponse
```

### Prompt to demonstrate

Use this prompt:

```text
I am working on a Spring Boot service class for an Asset Tracker API.

Task:
Refactor this service to improve readability and reduce duplication.

Constraints:
- Do not change public method names.
- Do not change controller endpoints.
- Do not change response DTOs.
- Do not change repository method names.
- Keep exception types the same.
- Do not add new dependencies.
- Keep the code understandable for beginner Java students.

Expected output:
1. Refactored service code
2. Explanation of helper methods created
3. Risks introduced
4. HTTP tests I should run
```

### Methods to add/extract

The updated trainer code extracts helper methods such as:

```java
private Asset findAssetOrThrow(String id)
private void ensureAssetTagIsUniqueForCreate(String assetTag)
private void ensureSerialNumberIsUniqueForCreate(String serialNumber)
private void ensureAssetTagIsUniqueForUpdate(Asset asset, String assetTag)
private void ensureSerialNumberIsUniqueForUpdate(Asset asset, String serialNumber)
private String normalizeRequired(String value)
private String normalizeStatus(String status)
private String normalizeOptional(String value)
```

### What students should notice

```text
The public methods are still the same:
- getAssets(...)
- getAssetsPaged(...)
- getAssetById(...)
- createAsset(...)
- updateAsset(...)

The API behaviour should stay the same.
Only the inside of the service becomes clearer.
```

### Test immediately

Run the backend:

```bash
mvn spring-boot:run
```

Then test:

```text
requests/day16-ai-refactor.http
```

Test cases:

```text
- Login as admin
- GET /api/v1/assets
- POST /api/v1/assets
- PUT /api/v1/assets/{id}
- Duplicate assetTag still returns 409
- Invalid status still returns 400
```

---

## 12:15 - 1:00 Backend refactor rationale

### File to show

```text
docs/day16-backend-refactor-rationale.md
```

### Explain the purpose

A refactor should be documented because reviewers need to know whether behaviour changed.

### Rationale structure

```text
Before:
The service had repeated logic inside create and update methods.

After:
Repeated logic was extracted into private helper methods.

Behaviour change:
No intended behaviour change.

Risk:
Helper methods must preserve the same validation rules and exception types.

Verification:
Run HTTP tests for create, update, duplicate and invalid status cases.
```

### Student mini-task

Ask students to write one paragraph:

```text
What improved after the backend refactor?
```

---

## 2:00 - 3:00 Frontend AI-assisted refactoring demo

### File to show first

```text
frontend/src/components/AssetFormWizard.jsx
```

### Explain the problem

The form works, but validation inside a large component can become difficult to test and maintain.

Say:

```text
When validation rules are mixed into UI components, testing the rules becomes harder. Moving validation into a utility makes it easier to test the logic separately.
```

### Prompt to demonstrate

```text
I have a React form wizard component for creating and editing assets.

Task:
Move validation logic into a separate utility file.

Constraints:
- Keep the UI exactly the same.
- Do not change CSS class names.
- Do not change form field names.
- Do not change route paths.
- Do not change API payload structure.
- Do not add new libraries.
- Keep the code understandable for beginner React students.

Expected output:
1. Updated component
2. New validation utility file
3. Vitest tests for validation
4. Explanation of what stayed the same
5. Risks to manually verify
```

### Files to add/edit

Add:

```text
frontend/src/utils/assetFormValidation.js
frontend/src/utils/assetFormValidation.test.js
```

Edit:

```text
frontend/src/components/AssetFormWizard.jsx
```

### Functions to show

```js
export function validateAssetFormStep(step, values, mode, reviewChecked)
```

Possible helper functions:

```js
function isBlank(value)
function isValidAssetTag(value)
function isValidEmail(value)
```

### What students should notice

```text
The form UI does not change.
The route does not change.
The backend payload does not change.
Only validation logic moved into a more testable place.
```

### Run tests

```bash
cd frontend
npm run test
```

Point out the new test file:

```text
frontend/src/utils/assetFormValidation.test.js
```

---

## 3:00 - 3:30 Test hardening with AI

### Explain

AI can generate tests, but generated tests are often too shallow.

### Bad AI-generated test pattern

```text
- Only checks that the component renders.
- Does not test edge cases.
- Does not test failure cases.
- Mocks too much.
```

### Better test request

```text
Generate Vitest tests for this validation function.

Cover:
- Valid identity step
- Missing asset tag
- Invalid asset tag format
- Missing serial number
- Missing location
- Invalid assigned email
- Review checkbox not checked

Keep the tests readable for beginner React students.
```

### Explain hardening

```text
Test hardening means improving tests so they catch meaningful mistakes, not just proving that files load.
```

---

## 3:30 - 4:30 Student exercises

Students apply the pattern to the Support Desk Ticket project.

### Student tasks

```text
1. Write a safe AI prompt for TicketService refactoring.
2. Refactor TicketService without changing endpoint behaviour.
3. Extract ticket form validation into a utility file.
4. Generate and improve tests for validation.
5. Write before/after rationale.
6. Reflect on AI usefulness and risk.
```

### Support Desk mapping

Asset Tracker trainer demo:

```text
AssetService
AssetFormWizard
assetFormValidation.js
```

Support Desk student exercise:

```text
TicketService
TicketFormWizard
ticketFormValidation.js
```

---

## 4:30 - 5:00 Review and wrap-up

### Review questions

Ask students:

1. What makes a coding prompt specific?
2. Why should constraints be included in a prompt?
3. Why should AI explain its changes?
4. Why should tests be run after refactoring?
5. What should you never paste into AI tools?
6. What is the difference between refactoring and changing behaviour?
7. What was the riskiest part of using AI today?
8. What AI suggestion did you reject or modify?

### Final teaching phrase

```text
AI can speed up development, but tests and developer judgement make it safe.
```

---

# Day 16 instructor checklist

Before class:

```text
- Day 15 branch works.
- npm run test passes.
- npm run test:e2e passes.
- Backend can run with mvn spring-boot:run.
- Day 16 changed files are ready.
- Prompt bank is open for reference.
```

During class:

```text
- Show bad prompt vs better prompt.
- Show AI safety rules before using AI.
- Demonstrate backend refactor first.
- Run HTTP tests after backend refactor.
- Demonstrate frontend validation extraction.
- Run Vitest after frontend refactor.
- Emphasise that AI output must be reviewed.
```

After class:

```text
- Students submit refactored code.
- Students submit before/after rationale.
- Students submit prompt used.
- Students submit test evidence.
```


---

# Final trainer note after implementation testing

After implementing Day 16, the final confirmed fixes were in the test layer, not the running application.

Before teaching, verify these three final patch points:

```text
1. Mock auth in testUtils.jsx includes expiresAt.
2. AssetsPage.test.jsx uses a precise role-based locator for LAP-2026-001.
3. AssetsPage.test.jsx treats missing fetch method as GET using options.method ?? 'GET'.
```

Use this as a teaching point:

```text
A refactor is only complete when the app works, the tests pass, and the tests still describe the correct behaviour.
```
