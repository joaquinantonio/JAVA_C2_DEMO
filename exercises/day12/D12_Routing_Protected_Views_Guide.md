# Day 12 Routing & Protected Views Guide

## What is routing?

Routing means showing different React components for different browser URLs.

Example:

```text
/login          LoginPage
/app/assets     AssetsPage
/app/reports    ReportsPage
```

## What is a protected view?

A protected view is a frontend page that should only be shown to logged-in users.

The frontend protection improves user experience, but the backend must still enforce real security.

## Important components

### BrowserRouter

Wraps the React app and allows route matching from the browser URL.

### Routes and Route

Define which component should render for each path.

### NavLink

Creates navigation links and automatically applies an active state.

### Navigate

Redirects users from one route to another.

### Outlet

Marks where child/nested route components should appear.

### useNavigate

Redirects from code, such as after login or logout.

### useLocation

Remembers the current path, useful for redirecting back after login.

## Classroom token storage note

This demo stores JWT auth state in localStorage so students can see the route flow clearly. In production, teams must consider token lifetime, refresh strategy, XSS risk, logout behaviour, and server-side protections.
