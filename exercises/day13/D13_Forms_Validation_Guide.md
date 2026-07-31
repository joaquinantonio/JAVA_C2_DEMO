# Day 13 Forms & Validation Guide - Expanded Trainer Notes

## 1. Purpose of Day 13

Day 13 is the day where the frontend stops being mostly read-only and starts allowing users to submit data.

Up to Day 12, students already have:

- a React frontend
- React Router
- protected routes
- login using the backend JWT
- protected asset pages
- protected report pages

Day 13 adds the next real application skill:

```text
A user can open a form, enter data, validate it, submit it to the backend, and see success or error feedback.
```

The teaching focus is not simply “make a form”. The bigger lesson is:

```text
Forms are controlled user input flows. React must track what the user typed, validate it, and decide when it is safe to submit.
```

## 2. Suggested opening explanation

Use this framing before showing code:

```text
So far, our React app can display data from the backend.
Today we let users send data back to the backend.
This is where frontend development becomes more realistic, because most business applications are built around forms.

Think of leave applications, support tickets, customer records, asset registration, profile updates, claims, purchases, and approvals. All of these are forms.

A good form does three things:
1. It helps the user enter the correct data.
2. It prevents obvious mistakes before sending the request.
3. It still relies on backend validation to protect the system.
```

Then introduce the key teaching phrase:

```text
Frontend validation helps the user. Backend validation protects the data.
```

## 3. What students should understand by the end

By the end of Day 13, students should be able to explain and implement:

- controlled inputs
- uncontrolled inputs
- form state
- validation state
- inline validation messages
- create mode vs edit mode
- `POST` for create
- `PUT` for update
- loading state while saving
- success state after saving
- backend error state when the request fails
- basic accessibility requirements for forms

## 4. Controlled inputs

### 4.1 Simple explanation

A controlled input is an input where React controls the displayed value.

Example:

```jsx
const [name, setName] = useState('');

<input
  value={name}
  onChange={(event) => setName(event.target.value)}
/>
```

Explain it like this:

```text
The value shown in the textbox comes from React state.
When the user types, onChange runs.
onChange updates the state.
React re-renders the input with the new value.
```

### 4.2 Why controlled inputs matter

Controlled inputs are useful because React can:

- validate the value while the user is typing
- clear the field after submit
- pre-fill the field during edit mode
- disable or enable buttons based on the input
- transform the value before sending it
- show error messages based on the current value

Use this classroom explanation:

```text
If React does not know what the user typed, React cannot validate it properly.
Controlled inputs make the form value part of the application state.
```

### 4.3 Common mistake: forgetting `onChange`

If students write this:

```jsx
<input value={formValues.name} />
```

The input becomes read-only because React is controlling the value but there is no way to update it.

Fix:

```jsx
<input
  value={formValues.name}
  onChange={handleChange}
/>
```

### 4.4 Common mistake: updating one field but deleting the rest

Wrong:

```jsx
setFormValues({ name: event.target.value });
```

This replaces the whole form object and removes the other fields.

Correct:

```jsx
setFormValues((currentValues) => ({
  ...currentValues,
  name: event.target.value
}));
```

Explain:

```text
The spread operator keeps the existing fields. Then we overwrite only the field that changed.
```

## 5. Generic change handler pattern

For a larger form, avoid creating one handler per input.

Example:

```jsx
function handleChange(event) {
  const { name, value } = event.target;

  setFormValues((currentValues) => ({
    ...currentValues,
    [name]: value
  }));
}
```

Then each input must have a `name` attribute:

```jsx
<input
  name="assetTag"
  value={formValues.assetTag}
  onChange={handleChange}
/>
```

Teaching explanation:

```text
The name attribute tells the handler which field changed.
The square brackets [name] mean use the value of the variable as the object key.
So if name is assetTag, React updates formValues.assetTag.
```

Common mistake:

```jsx
<input value={formValues.assetTag} onChange={handleChange} />
```

Problem:

```text
The handler cannot know which field changed because the input has no name.
```

## 6. Uncontrolled inputs

### 6.1 Simple explanation

An uncontrolled input keeps its current value inside the browser DOM. React does not update state every time the value changes.

In Day 13, the review checkbox is used to demonstrate this idea with `useRef`.

Example:

```jsx
const reviewCheckboxRef = useRef(null);

<input type="checkbox" ref={reviewCheckboxRef} />
```

Then read it during validation:

```jsx
const isChecked = reviewCheckboxRef.current?.checked;
```

### 6.2 When uncontrolled input is acceptable

Uncontrolled inputs can be acceptable when:

- the value is only needed at submit time
- the field does not need live validation
- you do not need to display the value elsewhere
- you want to demonstrate how refs work

### 6.3 What to tell students

```text
Most business forms are easier to manage with controlled inputs.
Uncontrolled inputs are still useful to understand because some real-world components and libraries use refs internally.
```

## 7. Form state model

The Day 13 form should make students notice that a real form needs more than field values.

Typical form state:

```text
formValues      -> what the user typed
errors          -> validation messages
currentStep     -> which wizard step is active
saving          -> whether the submit request is running
serverError     -> backend error message
successMessage  -> success feedback after save
```

Teaching point:

```text
A form is not just inputs. A form is a small workflow.
```

## 8. Form wizard concept

Day 13 uses a wizard pattern because it is easier for students to see form flow step by step.

### 8.1 Why use a wizard?

A wizard breaks a long form into smaller parts.

For example:

```text
Step 1: Asset identity
Step 2: Location and status
Step 3: Review and submit
```

Explain:

```text
Instead of showing everything at once, we guide the user through a process.
This reduces confusion and allows validation at each step.
```

### 8.2 Step validation

Before moving from Step 1 to Step 2, validate Step 1 fields.

Example:

```text
Do not allow the user to move forward if assetTag, name, category, or serialNumber is empty.
```

This is different from submit validation:

```text
Step validation checks the current section.
Submit validation checks the whole form.
```

## 9. Client-side validation

Client-side validation runs in the browser before the request is sent to the backend.

### 9.1 Why do it?

Client-side validation improves user experience.

It helps the user by:

- showing mistakes immediately
- reducing failed requests
- making required fields obvious
- explaining what format is expected
- preventing unnecessary backend calls

### 9.2 Examples for Asset Tracker

Useful Day 13 client-side validation rules:

```text
assetTag is required
assetTag should be uppercase
name is required
category is required
serialNumber is required
location is required
status is required during edit mode
assignedTo should look like an email if provided
review checkbox must be checked before submit
```

### 9.3 Inline errors

Show errors near the field, not only at the top of the page.

Good:

```text
Asset Tag
[              ]
Asset tag is required.
```

Less helpful:

```text
There are errors in the form.
```

Teaching phrase:

```text
Good validation tells the user exactly what to fix and where to fix it.
```

## 10. Backend validation

Backend validation is still required even if the frontend validates everything.

### 10.1 Why frontend validation is not enough

Tell students:

```text
The browser is controlled by the user. A user can bypass your React form and call the API directly using REST Client, Postman, curl, or another frontend.
```

Therefore, backend validation protects the database.

### 10.2 Backend checks for Day 13

The backend should still check:

- duplicate asset tag
- duplicate serial number
- invalid status
- missing required fields
- unauthorised user
- asset ID not found

### 10.3 HTTP response examples

```text
400 Bad Request       -> validation problem
401 Unauthorized      -> no token or invalid token
403 Forbidden         -> valid login but wrong role
404 Not Found         -> asset ID does not exist
409 Conflict          -> duplicate asset tag or serial number
```

## 11. Create mode vs edit mode

Day 13 uses one form page for both create and edit.

### 11.1 Create mode

Create mode means:

```text
There is no assetId in the URL.
The form starts empty.
The submit action uses POST.
```

Route:

```text
/app/assets/new
```

Backend call:

```http
POST /api/v1/assets
```

### 11.2 Edit mode

Edit mode means:

```text
There is an assetId in the URL.
The form loads existing asset data.
The submit action uses PUT.
```

Route:

```text
/app/assets/:assetId/edit
```

Backend call:

```http
PUT /api/v1/assets/{id}
```

### 11.3 How React knows the mode

Use `useParams()`:

```jsx
const { assetId } = useParams();
const isEditMode = Boolean(assetId);
```

Teaching explanation:

```text
If the URL contains an assetId, the page becomes an edit form.
If there is no assetId, the page becomes a create form.
```

## 12. Fetching existing data for edit mode

In edit mode, the form should first load the selected asset.

Typical pattern:

```jsx
useEffect(() => {
  async function loadAssetForEdit() {
    const asset = await fetchAssetById(assetId, token);
    setInitialValues({
      assetTag: asset.assetTag ?? '',
      name: asset.name ?? '',
      category: asset.category ?? '',
      serialNumber: asset.serialNumber ?? '',
      status: asset.status ?? 'AVAILABLE',
      location: asset.location ?? '',
      assignedTo: asset.assignedTo ?? ''
    });
  }

  if (assetId) {
    loadAssetForEdit();
  }
}, [assetId, token]);
```

Teaching explanation:

```text
The form cannot show edit values until it has loaded the existing record from the backend.
That is why edit mode needs loading and error states.
```

## 13. POST vs PUT

### 13.1 POST

Use `POST` when creating a new resource.

```http
POST /api/v1/assets
```

The backend creates a new MongoDB document and returns the created record.

### 13.2 PUT

Use `PUT` when updating an existing resource.

```http
PUT /api/v1/assets/{id}
```

The ID tells the backend which existing asset to update.

### 13.3 Classroom explanation

```text
POST means create something new.
PUT means replace or update something that already exists at this URL.
```

For this course, keep the explanation practical. Avoid spending too long on REST theory differences between `PUT` and `PATCH` unless students ask.

## 14. Frontend API helper functions

Day 13 should not put raw `fetch()` directly inside every component if the code is becoming repeated.

Example helper for create:

```js
export async function createAsset(token, payload) {
  const response = await fetch('/api/v1/assets', {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
  });

  return parseJsonResponse(response);
}
```

Example helper for update:

```js
export async function updateAsset(id, token, payload) {
  const response = await fetch(`/api/v1/assets/${id}`, {
    method: 'PUT',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
  });

  return parseJsonResponse(response);
}
```

Teaching point:

```text
The component should focus on form behaviour.
The API helper should focus on HTTP details.
```

## 15. Saving, success and error states

A good form should not silently submit.

### 15.1 Saving state

Use saving state to prevent double submission:

```jsx
const [saving, setSaving] = useState(false);
```

When saving:

```jsx
<button type="submit" disabled={saving}>
  {saving ? 'Saving...' : 'Save Asset'}
</button>
```

### 15.2 Success state

After a successful request:

```jsx
setSuccessMessage('Asset saved successfully.');
```

### 15.3 Server error state

If backend rejects the request:

```jsx
setServerError(err.message || 'Could not save asset.');
```

Explain:

```text
A form needs to tell the user whether the save is still happening, completed successfully, or failed.
```

## 16. Accessibility reminders

Good form accessibility makes the form easier for everyone to use.

### 16.1 Use visible labels

Good:

```jsx
<label>
  Asset Tag
  <input name="assetTag" value={formValues.assetTag} onChange={handleChange} />
</label>
```

Avoid using only placeholders:

```jsx
<input placeholder="Asset Tag" />
```

Explain:

```text
A placeholder disappears when the user types. A label stays visible.
```

### 16.2 Error messages should be clear

Good:

```text
Asset tag is required.
```

Less helpful:

```text
Invalid input.
```

### 16.3 Do not rely only on colour

Bad:

```text
The red field is wrong.
```

Good:

```text
Show a text message explaining the problem.
```

## 17. Demo flow for trainer

Use this sequence during the live demo.

### Demo 1: Show empty form validation

1. Open `/app/assets/new`.
2. Click next or submit without entering data.
3. Show inline errors.
4. Explain controlled input and validation state.

### Demo 2: Create a valid asset

1. Fill in all required fields.
2. Submit.
3. Show success message.
4. Return to assets page and show the new asset.

### Demo 3: Duplicate backend error

1. Try to create another asset with the same asset tag or serial number.
2. Show backend error.
3. Explain why backend validation is still needed.

### Demo 4: Edit an asset

1. Open an existing asset.
2. Click edit.
3. Show pre-filled values.
4. Change status or location.
5. Submit with `PUT`.
6. Return to asset list and confirm changes.

### Demo 5: Troubleshoot 405

If `PUT` returns 405:

```text
Restart Spring Boot. The old running backend may not have loaded the new PUT endpoint.
```

This happened during testing and is a realistic troubleshooting example.

## 18. Common Day 13 student mistakes

| Mistake | Symptom | Fix |
|---|---|---|
| Missing `onChange` | Input cannot be typed into | Add `onChange` handler |
| Missing `name` attribute | Generic handler does not update correct field | Add `name="fieldName"` |
| Replacing whole form object | Other field values disappear | Use spread operator |
| Not preventing default submit | Page refreshes | Use `event.preventDefault()` |
| Missing token | 401 response | Use token from `AuthContext` |
| Logged in as USER | 403 response for create/update | Login as ADMIN |
| Wrong update URL | 405 response | Use `/api/v1/assets/{id}` |
| Old backend still running | New endpoint appears missing | Restart Spring Boot |
| Not checking duplicate asset tag | MongoDB duplicate error or 409 | Handle duplicate in service |
| Error message only at top | User unsure which field is wrong | Show inline errors |

## 19. Mapping Day 13 to Support Desk student exercise

Trainer demo uses:

```text
Asset Tracker
```

Student exercise should use:

```text
Support Desk Ticket System
```

Map concepts like this:

| Trainer Asset Tracker | Student Support Desk |
|---|---|
| Asset form | Ticket form |
| assetTag | ticket title or ticket code |
| category | ticket category |
| status | ticket status |
| assignedTo | assigned agent |
| POST `/api/v1/assets` | POST `/api/v1/tickets` |
| PUT `/api/v1/assets/{id}` | PUT `/api/v1/tickets/{id}` |
| AssetFormWizard | TicketFormWizard |

Suggested ticket fields:

```text
title
description
category
priority
status
```

Suggested validation:

```text
title is required
description is required
category is required
priority must be LOW, MEDIUM or HIGH
status must be OPEN, IN_PROGRESS or CLOSED
```

## 20. End-of-day review questions

Ask students:

1. What is a controlled input?
2. Why do we need `onChange`?
3. Why should we not rely only on frontend validation?
4. What is the difference between `POST` and `PUT`?
5. Why does edit mode need to fetch existing data first?
6. Why should a form show loading, success and error states?
7. What does a 401 mean during form submission?
8. What does a 403 mean during form submission?
9. What does a 409 mean during form submission?
10. What was the hardest part of Day 13?

## 21. Final teaching summary

Close the day with this:

```text
Today we turned our frontend from a read-only interface into an interactive data entry interface.
Forms are one of the most important parts of business applications.
A good form manages user input, validates mistakes early, sends the correct request, and explains what happened after submission.
```
