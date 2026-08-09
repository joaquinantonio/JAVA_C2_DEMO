# Day 16 Code To Show Step By Step - Expanded With Prompt Engineering

This guide tells the instructor what to show, in what order, and what to explain while coding.

## Day 16 rule

Do not show AI as magic. Show AI as a tool that needs clear instructions, careful review and tests.

```text
Prompt → Review → Edit → Test → Commit
```

---

# Step 0 - Start from Day 15

## Branch setup

```bash
git checkout test/15
git checkout -b test/16
```

## Confirm the app still works

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

## Explain why this matters

Say:

```text
Before we refactor, we need a working baseline. If tests fail before the refactor, we cannot blame the refactor.
```

---

# Step 1 - Show the problem: working code that can be improved

## Backend file

Show:

```text
src/main/java/com/example/assettracker/service/AssetService.java
```

Ask students:

```text
What repeated logic do you see?
```

Expected answers:

```text
- Trimming values
- Duplicate asset tag check
- Duplicate serial number check
- Status validation
- findById plus not-found exception
```

## Frontend file

Show:

```text
frontend/src/components/AssetFormWizard.jsx
```

Ask:

```text
What logic is mixed together here?
```

Expected answers:

```text
- UI rendering
- form state
- step movement
- validation rules
- submit behaviour
```

Explain:

```text
The files work. We are not fixing broken code. We are improving maintainability.
```

---

# Step 2 - Teach the coding prompt structure

Write this on screen:

```text
Context
Task
Constraints
Expected output
Tests
Review
```

## Show a weak prompt

```text
Refactor this code.
```

Ask students why this is risky.

Expected points:

```text
- AI may change behaviour.
- AI may rename methods.
- AI may remove validation.
- AI may add unnecessary libraries.
```

## Show a stronger prompt

```text
I am working on a Spring Boot service class in an Asset Tracker API.

Task:
Refactor the service to reduce duplication and improve readability.

Constraints:
- Do not change public method names.
- Do not change controller endpoints.
- Do not change DTO fields.
- Do not change repository method names.
- Do not change exception types.
- Do not add dependencies.
- Keep the result understandable for beginner Java students.

Return:
1. Refactored code
2. Explanation of each helper method
3. Risks introduced
4. Tests I should run
```

Explain:

```text
The better prompt gives AI a smaller and safer target.
```

---

# Step 3 - Backend refactor: AssetService

## File to edit

```text
src/main/java/com/example/assettracker/service/AssetService.java
```

## Public methods to keep unchanged

Before refactoring, show that these public methods are the same contract used by controllers:

```java
public List<AssetResponse> getAssets(String status, String category, String location)
public Page<AssetResponse> getAssetsPaged(int page, int size, String sortBy, String direction)
public AssetResponse getAssetById(String id)
public AssetResponse createAsset(CreateAssetRequest request)
public AssetResponse updateAsset(String id, UpdateAssetRequest request)
```

Explain:

```text
Public methods are the service contract. If we change them, other files may break.
```

## Helper methods to add

Add or show these private methods:

```java
private Asset findAssetOrThrow(String id) {
    return assetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset " + id + " was not found"));
}
```

Explain:

```text
This removes repeated findById and exception logic.
```

```java
private void ensureAssetTagIsUniqueForCreate(String assetTag) {
    if (assetRepository.existsByAssetTag(assetTag)) {
        throw new DuplicateResourceException("Asset tag already exists: " + assetTag);
    }
}
```

```java
private void ensureSerialNumberIsUniqueForCreate(String serialNumber) {
    if (assetRepository.existsBySerialNumber(serialNumber)) {
        throw new DuplicateResourceException("Serial number already exists: " + serialNumber);
    }
}
```

Explain:

```text
Create mode checks whether the value already exists anywhere in the database.
```

```java
private void ensureAssetTagIsUniqueForUpdate(Asset asset, String assetTag) {
    if (!asset.getAssetTag().equalsIgnoreCase(assetTag) && assetRepository.existsByAssetTag(assetTag)) {
        throw new DuplicateResourceException("Asset tag already exists: " + assetTag);
    }
}
```

```java
private void ensureSerialNumberIsUniqueForUpdate(Asset asset, String serialNumber) {
    if (!asset.getSerialNumber().equalsIgnoreCase(serialNumber) && assetRepository.existsBySerialNumber(serialNumber)) {
        throw new DuplicateResourceException("Serial number already exists: " + serialNumber);
    }
}
```

Explain:

```text
Update mode is different from create mode. It should allow the asset to keep its own asset tag and serial number.
```

```java
private String normalizeRequired(String value) {
    return value.trim();
}
```

```java
private String normalizeStatus(String status) {
    String normalizedStatus = status.trim().toUpperCase();
    validateStatus(normalizedStatus);
    return normalizedStatus;
}
```

Explain:

```text
Normalising means cleaning or standardising input before using it.
```

## Update createAsset

Show the before/after idea:

```java
public AssetResponse createAsset(CreateAssetRequest request) {
    String assetTag = normalizeRequired(request.getAssetTag());
    String serialNumber = normalizeRequired(request.getSerialNumber());

    ensureAssetTagIsUniqueForCreate(assetTag);
    ensureSerialNumberIsUniqueForCreate(serialNumber);

    Asset asset = new Asset(
            assetTag,
            normalizeRequired(request.getName()),
            normalizeRequired(request.getCategory()),
            serialNumber,
            "AVAILABLE",
            normalizeRequired(request.getLocation()),
            null
    );

    return toResponse(assetRepository.save(asset));
}
```

## Update updateAsset

```java
public AssetResponse updateAsset(String id, UpdateAssetRequest request) {
    logger.info("Updating asset id={}", id);

    Asset asset = findAssetOrThrow(id);
    String assetTag = normalizeRequired(request.getAssetTag());
    String serialNumber = normalizeRequired(request.getSerialNumber());
    String status = normalizeStatus(request.getStatus());

    ensureAssetTagIsUniqueForUpdate(asset, assetTag);
    ensureSerialNumberIsUniqueForUpdate(asset, serialNumber);

    asset.setAssetTag(assetTag);
    asset.setName(normalizeRequired(request.getName()));
    asset.setCategory(normalizeRequired(request.getCategory()));
    asset.setSerialNumber(serialNumber);
    asset.setStatus(status);
    asset.setLocation(normalizeRequired(request.getLocation()));
    asset.setAssignedTo(normalizeOptional(request.getAssignedTo()));

    return toResponse(assetRepository.save(asset));
}
```

## Test after backend refactor

Run:

```bash
mvn spring-boot:run
```

Use:

```text
requests/day16-ai-refactor.http
```

Test these manually:

```text
- Login as admin
- Create asset
- Update asset
- Duplicate asset tag returns 409
- Invalid status returns 400
```

---

# Step 4 - Ask AI to explain the refactor

Use this prompt after showing the refactored code:

```text
Explain this refactor to beginner Java students.

Focus on:
1. Which helper methods were extracted
2. Why create and update duplicate checks are different
3. Why public methods stayed the same
4. What tests should be run to prove behaviour did not change
```

Then ask students:

```text
Do you agree with the explanation? Did AI miss anything?
```

---

# Step 5 - Frontend refactor: extract form validation

## File to show before refactor

```text
frontend/src/components/AssetFormWizard.jsx
```

Explain:

```text
This component currently owns too many responsibilities. It renders the UI and contains validation logic.
```

## File to add

```text
frontend/src/utils/assetFormValidation.js
```

## Main function to show

```js
export function validateAssetFormStep(step, values, mode, reviewChecked) {
  const errors = {};

  if (step === 1) {
    if (isBlank(values.assetTag)) {
      errors.assetTag = 'Asset tag is required.';
    } else if (!isValidAssetTag(values.assetTag)) {
      errors.assetTag = 'Use uppercase letters, numbers and dashes only.';
    }

    if (isBlank(values.name)) {
      errors.name = 'Asset name is required.';
    }

    if (isBlank(values.category)) {
      errors.category = 'Category is required.';
    }

    if (isBlank(values.serialNumber)) {
      errors.serialNumber = 'Serial number is required.';
    }
  }

  if (step === 2) {
    if (isBlank(values.location)) {
      errors.location = 'Location is required.';
    }

    if (mode === 'edit' && !isBlank(values.assignedTo) && !isValidEmail(values.assignedTo)) {
      errors.assignedTo = 'Assigned to must be a valid email address.';
    }
  }

  if (step === 3 && !reviewChecked) {
    errors.review = 'Please confirm the review checkbox before submitting.';
  }

  return errors;
}
```

## Explain the helper methods

```js
function isBlank(value) {
  return !value || value.trim().length === 0;
}
```

```js
function isValidAssetTag(value) {
  return /^[A-Z0-9-]+$/.test(value);
}
```

```js
function isValidEmail(value) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
}
```

## File to edit

```text
frontend/src/components/AssetFormWizard.jsx
```

Import the utility:

```js
import { validateAssetFormStep } from '../utils/assetFormValidation.js';
```

Update validation call:

```js
const nextErrors = validateAssetFormStep(currentStep, formValues, mode, reviewCheckboxRef.current?.checked);
```

Explain:

```text
The component still decides when to validate. The utility decides what is valid.
```

---

# Step 6 - Add tests for the extracted validation

## File to add

```text
frontend/src/utils/assetFormValidation.test.js
```

## Test cases to show

```js
import { describe, expect, it } from 'vitest';
import { validateAssetFormStep } from './assetFormValidation.js';

const validValues = {
  assetTag: 'LAP-2026-001',
  name: 'Dell Latitude 5440',
  category: 'Laptop',
  serialNumber: 'SN-LAP-001',
  status: 'AVAILABLE',
  location: 'HQ Level 3',
  assignedTo: 'user@example.com'
};

describe('validateAssetFormStep', () => {
  it('returns no errors for valid identity fields', () => {
    expect(validateAssetFormStep(1, validValues, 'create', false)).toEqual({});
  });

  it('requires asset tag on step 1', () => {
    const errors = validateAssetFormStep(1, { ...validValues, assetTag: '' }, 'create', false);
    expect(errors.assetTag).toBe('Asset tag is required.');
  });

  it('requires location on step 2', () => {
    const errors = validateAssetFormStep(2, { ...validValues, location: '' }, 'create', false);
    expect(errors.location).toBe('Location is required.');
  });

  it('requires review confirmation on step 3', () => {
    const errors = validateAssetFormStep(3, validValues, 'create', false);
    expect(errors.review).toBe('Please confirm the review checkbox before submitting.');
  });
});
```

## Run tests

```bash
cd frontend
npm run test
```

Explain:

```text
Extracting validation is useful because we can now test it without rendering the whole form.
```

---

# Step 7 - Ask AI to review its own output

Use this prompt:

```text
Review the refactor you suggested.

Check:
1. Did you change any route paths?
2. Did you change any CSS class names?
3. Did you change the API payload?
4. Did you remove any validation rule?
5. Did you add any dependency?
6. What should I manually test before accepting this code?
```

Explain:

```text
AI can help review, but this does not replace human review.
```

---

# Step 8 - Run final Day 16 checks

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

Manual checks:

```text
- Login works
- Assets page loads
- Reports page loads
- Create asset form works
- Edit asset form works
- Validation errors still show
- Optimistic status update still works
```

---

# Step 9 - Show before/after rationale documents

## Backend rationale

```text
docs/day16-backend-refactor-rationale.md
```

## Frontend rationale

```text
docs/day16-frontend-refactor-rationale.md
```

Explain that good refactoring needs evidence:

```text
What changed?
Why did it improve the code?
What did not change?
How did you test it?
```

---

# Step 10 - Student exercise mapping

Trainer demo:

```text
AssetService → refactor helper methods
AssetFormWizard → extract validation utility
assetFormValidation.test.js → test extracted logic
```

Student Support Desk project:

```text
TicketService → refactor helper methods
TicketFormWizard → extract validation utility
ticketFormValidation.test.js → test extracted logic
```

Suggested student file names:

```text
src/main/java/com/example/supportdesk/service/TicketService.java
frontend/src/components/TicketFormWizard.jsx
frontend/src/utils/ticketFormValidation.js
frontend/src/utils/ticketFormValidation.test.js
```

---

# Instructor notes

## Do not let students paste huge files blindly

Instead, teach them to paste one focused file or method at a time.

## Do not let students accept generated code without running tests

Always end with:

```bash
npm run test
npm run test:e2e
```

and backend HTTP checks.

## Do not let students refactor and add features together

Say:

```text
Today, we are changing structure, not behaviour.
```


---

# Final Code Patch: Test Updates After Refactor

These are the final patches confirmed after running `npm run test` on Day 16.

## 1. Update mock auth helper

File:

```text
frontend/src/test/testUtils.jsx
```

Make sure `storeAdminAuth()` includes `expiresAt`:

```js
export function storeAdminAuth() {
  localStorage.setItem('assetTrackerAuth', JSON.stringify({
    token: 'test-admin-token',
    tokenType: 'Bearer',
    expiresInMinutes: 60,
    expiresAt: Date.now() + 60 * 60 * 1000,
    user: {
      id: 'U001',
      name: 'Admin User',
      email: 'admin@example.com',
      role: 'ADMIN'
    }
  }));
}
```

## 2. Update AssetsPage test locator and GET expectation

File:

```text
frontend/src/pages/AssetsPage.test.jsx
```

Use this assertion:

```js
expect(
  await screen.findByRole('button', { name: /LAP-2026-001/i })
).toBeInTheDocument();
```

Then check the fetch call like this:

```js
const [url, options] = globalThis.fetch.mock.calls[0];

expect(url).toBe('/api/v1/assets/paged?page=0&size=5&sortBy=assetTag&direction=asc');
expect(options.method ?? 'GET').toBe('GET');
```

Explain to students:

```text
GET is the default fetch method. The test should not fail just because the refactored HTTP client does not explicitly write method: 'GET'.
```
