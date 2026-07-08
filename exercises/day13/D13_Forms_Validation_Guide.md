# Day 13 Forms & Validation Guide

## Controlled inputs

A controlled input is connected to React state.

```jsx
<input value={formValues.name} onChange={handleChange} />
```

Use controlled inputs when the UI must validate, transform, or submit values.

## Uncontrolled inputs

An uncontrolled input keeps its own value in the DOM. React reads it only when needed.

In Day 13, the review checkbox uses `useRef` to demonstrate this.

## Client-side validation

Client-side validation is for user experience.

Examples:

- required fields
- simple format checks
- step-by-step blocking
- inline error messages

## Backend validation

Backend validation is for system protection.

Examples:

- duplicate asset tag
- duplicate serial number
- invalid status
- unauthorised user

## Accessibility reminders

- Use visible labels.
- Connect errors near the field.
- Use clear button text.
- Do not rely on colour only.
- Keep form steps understandable.

## Teaching phrase

```text
Frontend validation helps the user. Backend validation protects the data.
```
