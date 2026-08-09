# Day 16 Instructor Guide: AI-Assisted Coding, Prompt Engineering & Safe Refactoring

## Purpose of this guide

This file is for the instructor. It is written as a teaching guide, not as a student handout. Use it to plan the lesson, explain concepts, run live demos, anticipate student mistakes, and close the day with a meaningful review.

The matching student file is:

```text
D16_Student_Handout_AI_Assisted_Refactoring.md
```

## Day 16 theme

Day 16 is not a generic prompt engineering class. It is a developer-focused lesson on using AI safely and practically inside an existing full-stack project.

The main teaching message is:

```text
AI-assisted refactoring is not finished when the code looks cleaner.
It is finished when the app still works, the tests pass, and the behaviour remains correct.
```

By this point, students already have:

```text
Spring Boot backend
MongoDB persistence
JWT authentication
Role-based authorisation
React frontend
Protected routes
Forms and validation
Pagination and filters
Optimistic update
Frontend tests
E2E smoke test
```

That makes Day 16 meaningful because they are now using AI on a real application instead of small toy snippets.

---

# 1. Instructor learning outcomes

By the end of Day 16, students should be able to:

1. Explain what AI coding assistants can and cannot do.
2. Write developer prompts with context, task, constraints, expected output, tests, and review instructions.
3. Use AI to explain existing code before changing it.
4. Use AI to suggest safe refactoring without changing public behaviour.
5. Review AI-generated code critically.
6. Extract repeated or messy logic into smaller functions.
7. Generate or improve tests for refactored logic.
8. Identify regression risk after AI-assisted changes.
9. Avoid sharing secrets, tokens, private data, or restricted code with AI tools.
10. Produce before/after evidence showing that the refactor improved maintainability without breaking the app.

---

# 2. Recommended lesson structure

## Morning focus

```text
Concepts, prompt engineering, safety, backend refactoring
```

## Afternoon focus

```text
Frontend refactoring, testing, regression checks, student exercise
```

## Exact teaching flow

| Time | Segment | Instructor goal |
|---|---|---|
| 9:00 - 9:15 | Recap Day 15 | Remind students that tests are now part of the development workflow. |
| 9:15 - 9:35 | What AI coding assistants are | Position AI as an assistant, not an authority. |
| 9:35 - 10:10 | Prompt engineering for developers | Teach the prompt structure used throughout the day. |
| 10:10 - 10:30 | Safety and responsibility | Explain secrets, privacy, licensing, and ownership of generated code. |
| 10:45 - 11:15 | Generate → Explain → Test workflow | Show the repeatable AI workflow. |
| 11:15 - 12:15 | Backend refactoring demo | Refactor `AssetService` while preserving behaviour. |
| 12:15 - 1:00 | Backend review and questions | Discuss what changed, what did not change, and what tests prove. |
| 2:00 - 2:45 | Frontend validation refactor | Extract validation from `AssetFormWizard` into a utility. |
| 2:45 - 3:20 | Test hardening | Add/adjust tests for validation and existing UI behaviour. |
| 3:35 - 4:30 | Student exercise | Students apply the same approach to the Support Desk Ticket app. |
| 4:30 - 5:00 | Regression review and closing | Review final checklist and reflection questions. |

---

# 3. Instructor opening script

Use this opening explanation:

```text
Today we are not learning how to ask AI to write an entire application for us.
We already built the application ourselves.
Today we are learning how a developer can use AI to understand code, improve code, generate tests, and check for mistakes.

The key idea is control. The AI can suggest changes, but the developer is still responsible for the final code.
```

Then connect to the project:

```text
Our Asset Tracker app already works. That means today's job is risky in a different way.
When a system already works, a refactor must improve structure without changing behaviour.
So we will make small changes, run tests, and check the UI after each important step.
```

---

# 4. Prompt engineering concept to teach

Teach this formula clearly and keep referring back to it:

```text
Context + Task + Constraints + Expected Output + Tests + Review
```

## Explain each part

| Prompt part | Meaning | Example |
|---|---|---|
| Context | What project/file/framework the AI is looking at | “This is a Spring Boot service class for an asset tracking API.” |
| Task | What you want changed or explained | “Refactor this service to reduce duplication.” |
| Constraints | What must not change | “Do not change endpoint behaviour, DTOs, or public method names.” |
| Expected output | What format you want | “Return changed files only and explain each change.” |
| Tests | What proof you want | “Suggest tests to prove behaviour is unchanged.” |
| Review | Ask AI to criticise its own output | “List possible regressions introduced by this change.” |

## Bad prompt

```text
Refactor this code.
```

## Better prompt

```text
I am working on a Spring Boot REST API for an asset tracking system.

Refactor this service class to improve readability and reduce repeated validation logic.

Constraints:
- Do not change public method names.
- Do not change endpoint behaviour.
- Do not change DTO names or response fields.
- Do not change exception types.
- Keep the code understandable for beginner Java students.

Return:
1. The refactored code
2. A summary of what changed
3. Risks introduced by the refactor
4. Tests I should run to confirm behaviour is unchanged
```

## Instructor teaching point

The better prompt does not just ask for code. It protects the existing project.

---

# 5. AI safety rules to emphasise

Do not skip this section. Students must understand that AI-assisted development has professional risks.

## Never paste into AI tools

```text
Real passwords
JWT tokens
API keys
Database credentials
Private customer data
Production database dumps
Confidential company code unless allowed by policy
Exam answers or restricted assessment material
Anything covered by NDA without permission
```

## Professional rule

```text
AI can suggest code, but the developer owns the result.
```

Explain this clearly:

```text
If AI-generated code breaks production, the answer cannot be “the AI wrote it.”
As developers, we must read, understand, test, and take responsibility for the final code.
```

## Instructor note

Students may ask, “Can I paste company code into ChatGPT or Gemini?”
Do not give a universal yes. The correct answer is:

```text
Only if your organisation allows it and the data is safe to share.
When unsure, remove secrets and sensitive information, or ask using simplified sample code.
```

---

# 6. Core workflow: Generate → Explain → Test → Review

Use this as the main workflow for the day.

## Step 1: Generate

Ask AI for a change.

Example:

```text
Suggest a refactor for this class. Do not change behaviour.
```

## Step 2: Explain

Ask AI to explain what it changed.

Example:

```text
Explain each change and why it improves the code.
```

## Step 3: Test

Ask AI what tests are needed.

Example:

```text
What tests prove that this refactor did not change behaviour?
```

## Step 4: Review

Ask AI to find risks in its own suggestion.

Example:

```text
Review your suggested refactor. What could break? What should I manually verify?
```

## Instructor emphasis

Students should not use AI in one step only.
The real value comes from the conversation:

```text
Ask → inspect → question → test → adjust
```

---

# 7. Backend demo: AssetService refactor

## Teaching objective

Show students how to use AI to improve a Java service class without changing API behaviour.

## Before refactor problem

The service works, but some logic may be repeated or crowded inside large methods.

Typical issues:

```text
Repeated duplicate checks
Repeated string trimming
Validation logic mixed with business flow
Large create/update methods
Harder to test smaller rules independently
```

## Refactor goal

Extract private helper methods such as:

```text
findAssetOrThrow()
ensureAssetTagIsUniqueForCreate()
ensureSerialNumberIsUniqueForCreate()
ensureAssetTagIsUniqueForUpdate()
ensureSerialNumberIsUniqueForUpdate()
normalizeRequired()
normalizeStatus()
```

## Prompt to show

```text
I am working on a Spring Boot service class for an Asset Tracker API.

Refactor this service to improve readability by extracting private helper methods.

Constraints:
- Do not change public method names.
- Do not change controller endpoints.
- Do not change DTOs.
- Do not change exception types.
- Do not change status values.
- Do not change duplicate checking behaviour.
- Keep the code suitable for students who are still learning Java.

Return:
1. Refactored code
2. Explanation of each helper method
3. Behaviour that must remain the same
4. Tests or HTTP requests I should run after refactoring
```

## Instructor explanation after code

Say:

```text
Notice that most of the change is not new functionality. The service already worked.
The improvement is that the rules now have names.
A method name like ensureAssetTagIsUniqueForCreate tells the next developer exactly what rule is being enforced.
```

## What must not change

```text
GET assets still works
GET paged assets still works
POST asset still works for admin
PUT asset still works for admin
Duplicate asset tag still returns conflict
Duplicate serial number still returns conflict
Invalid status still returns validation error
```

## Backend verification

Use HTTP requests or Postman/Thunder Client to test:

```http
GET http://localhost:8080/api/v1/assets/paged?page=0&size=5&sortBy=assetTag&direction=asc
```

```http
POST http://localhost:8080/api/v1/assets
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "assetTag": "AI-REF-001",
  "name": "AI Refactor Test Device",
  "category": "Testing",
  "serialNumber": "SN-AI-REF-001",
  "location": "Training Room"
}
```

---

# 8. Frontend demo: Extract form validation

## Teaching objective

Show students how to use AI to move validation logic out of a React component into a testable utility.

## Before refactor problem

`AssetFormWizard.jsx` may become too large because it contains:

```text
UI rendering
Form state
Wizard step navigation
Validation rules
Submit handling
Error display
```

This is common in React forms.

## Refactor goal

Move validation rules to:

```text
frontend/src/utils/assetFormValidation.js
```

Then test the validation separately:

```text
frontend/src/utils/assetFormValidation.test.js
```

## Prompt to show

```text
I have a React form wizard used to create and edit assets.

The validation logic is currently inside the component.
Refactor it by moving validation into a separate utility file.

Constraints:
- Do not change the UI layout.
- Do not change CSS class names.
- Do not change route paths.
- Do not change field names.
- Do not change the API payload structure.
- Do not add new libraries.
- Keep the component understandable for students.

Return:
1. Updated component code
2. New validation utility file
3. Vitest tests for the validation utility
4. Explanation of what behaviour stayed the same
```

## Instructor explanation after code

Say:

```text
This is a good refactor because validation is business logic. Once it is in a utility function, we can test it without rendering the whole form.
The form component becomes easier to read, and the validation rules become easier to maintain.
```

## What must not change

```text
Create asset form still opens
Edit asset form still loads existing data
Required fields still show errors
Step navigation still works
Submit payload still matches backend expectations
Success and error messages still appear
```

---

# 9. Important regression lesson from our Day 16 implementation

Use this section openly. It is a strong teaching moment.

During Day 16 preparation, the refactor introduced or revealed several issues:

```text
1. Login failed because httpClient.js did not JSON.stringify request bodies.
2. Login failed because Content-Type: application/json was not added when body existed.
3. Unit tests failed because mock auth did not include expiresAt after stale-token handling was added.
4. AssetsPage test failed because getByText('LAP-2026-001') matched multiple UI elements.
5. AssetsPage test was too strict because it expected method: 'GET', even though fetch uses GET by default.
6. Optimistic update button styling was accidentally reverted.
```

## Instructor explanation

Say:

```text
This is not embarrassing. This is exactly what happens in real development.
AI-assisted refactoring can make code cleaner, but it can also accidentally undo working behaviour or make tests outdated.
That is why we run unit tests, E2E tests, and manual UI checks.
```

## The final corrected lesson

```text
Refactoring is successful only when:
1. The code is cleaner.
2. The app still works.
3. Tests pass.
4. Existing UI behaviour is preserved.
5. The team understands what changed.
```

---

# 10. Final code checks to mention

Before declaring Day 16 complete, verify these.

## `httpClient.js`

Must:

```text
Convert body objects using JSON.stringify
Add Content-Type: application/json when body exists
Attach Authorization: Bearer <token> when token exists
Throw errors with status code
Support default GET requests
```

## Auth tests

Mock auth should include:

```text
token
user
expiresAt
```

## AssetsPage test

Avoid broad text match:

```js
screen.getByText('LAP-2026-001')
```

Prefer role-based locator:

```js
await screen.findByRole('button', { name: /LAP-2026-001/i })
```

## Fetch GET test

Avoid assuming `method: 'GET'` must be explicitly passed.

Better:

```js
const [url, options] = globalThis.fetch.mock.calls[0];

expect(url).toBe('/api/v1/assets/paged?page=0&size=5&sortBy=assetTag&direction=asc');
expect(options.method ?? 'GET').toBe('GET');
```

## Optimistic update styling

Current status should be the only active blue button.
Other statuses should stay neutral.

---

# 11. Common student mistakes

## Mistake 1: Asking AI to rewrite everything

Student prompt:

```text
Rewrite my whole project using best practices.
```

Instructor response:

```text
Too broad. Ask for a small, controlled improvement instead.
```

## Mistake 2: No constraints

Student prompt:

```text
Improve this React component.
```

Problem:

```text
AI may rename props, change CSS classes, change routes, or alter payloads.
```

Better:

```text
Improve readability without changing props, routes, CSS classes, or API payloads.
```

## Mistake 3: Accepting AI code without reading

Instructor response:

```text
Before you paste the code, explain what it does. If you cannot explain it, do not use it yet.
```

## Mistake 4: Refactoring and adding features at the same time

Instructor response:

```text
Separate refactoring from feature work. Today we are improving structure while preserving behaviour.
```

## Mistake 5: Ignoring tests

Instructor response:

```text
A refactor without tests is just a guess that nothing broke.
```

---

# 12. Student exercise instructions for instructor

Students should apply the same AI-assisted workflow to their Support Desk Ticket project.

Recommended task:

```text
Refactor TicketService or TicketForm validation using AI assistance.
```

They should produce:

```text
1. The original prompt they used
2. The improved prompt they used
3. The before/after code change
4. A short explanation of what improved
5. Evidence that tests or manual checks passed
6. A reflection on what they rejected or changed from the AI suggestion
```

## Support Desk sample prompt

```text
I am working on a Support Desk Ticket API built with Spring Boot and MongoDB.

Refactor the TicketService class to improve readability.

Constraints:
- Do not change public method names.
- Do not change controller endpoints.
- Do not change DTOs.
- Do not change status or priority values.
- Keep the code suitable for beginner Java students.

Return:
1. Refactored code
2. Explanation of each helper method
3. Risks introduced
4. Tests or HTTP requests I should run
```

---

# 13. Assessment rubric for Day 16 activity

Use this simple rubric if you want to grade the activity.

| Criteria | Marks |
|---|---:|
| Prompt includes context, task, constraints, expected output and testing request | 20 |
| Refactor is small and controlled | 20 |
| Existing behaviour is preserved | 20 |
| Student can explain before/after difference | 15 |
| Tests or manual verification are provided | 15 |
| Safety rules followed | 10 |
| Total | 100 |

Recommended passing threshold:

```text
60/100 for basic competency
75/100 for strong practical competency
```

---

# 14. Instructor questions for class discussion

Use these questions during review:

1. What is the difference between refactoring and adding a feature?
2. Why is “do not change behaviour” an important constraint?
3. What information should a good coding prompt include?
4. Why should we ask AI to explain its own suggestion?
5. What kind of data should we never paste into AI tools?
6. How can tests detect regressions after refactoring?
7. Why did the app pass E2E but fail unit tests during our Day 16 fixes?
8. What does it mean for a test to be too tightly coupled to implementation details?
9. Why is role-based querying better than broad text matching in React tests?
10. What would you manually check before committing an AI-assisted refactor?

---

# 15. Final instructor checklist

Before teaching:

```text
[ ] Backend runs
[ ] Frontend runs
[ ] Login works
[ ] Asset list loads
[ ] Create asset works
[ ] Edit asset works
[ ] Optimistic update buttons display correctly
[ ] npm run test passes
[ ] npm run test:e2e passes
[ ] Day 16 student handout is distributed
[ ] Prompt bank is ready
[ ] Student exercise files are ready
```

Before closing the day:

```text
[ ] Students can explain prompt structure
[ ] Students can identify unsafe AI usage
[ ] Students completed at least one refactor
[ ] Students ran tests or manual checks
[ ] Students wrote a short before/after reflection
```

---

# 16. Closing script

Use this closing statement:

```text
Today you learned that AI can help you code faster, but speed is not the only goal.
A professional developer uses AI with control.
You give it context, constraints, and tests.
You review the output.
You verify behaviour.
Then you decide what to keep.

That is the difference between using AI casually and using AI professionally as a software developer.
```
