# Day 11 React Concepts Guide

## Component

A component is a reusable piece of UI.

Example:

```jsx
function StatusBadge({ status }) {
  return <span>{status}</span>;
}
```

## JSX

JSX lets us write UI inside JavaScript.

It looks similar to HTML, but it is JavaScript syntax.

## Props

Props are inputs passed from a parent component to a child component.

```jsx
<StatusBadge status="AVAILABLE" />
```

Inside the child:

```jsx
function StatusBadge({ status }) {
  return <span>{status}</span>;
}
```

## State

State is data that can change while the app is running.

```jsx
const [selectedAsset, setSelectedAsset] = useState(null);
```

When state changes, React updates the UI.

## useEffect

`useEffect` is used for side effects.

Examples:

- API calls
- reading local storage
- setting up timers
- syncing with external systems

For Day 11, we use it to load public API information from the backend.

## Loading, error, empty and success states

Professional UIs do not only show successful data.

They also show:

```text
loading: data is being requested
error: something failed
empty: request succeeded but no data is available
success: data is available
```

## Component tree

A component tree shows how components are arranged.

Example:

```text
App
├── Layout
├── SummaryCards
├── FilterPanel
└── AssetList
```

This helps students plan a UI before coding.
