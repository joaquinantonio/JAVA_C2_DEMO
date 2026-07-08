# Day 12 Exact Trainer Plan: Routing & Protected Views

## Learning outcomes

By the end of Day 12, students should be able to:

1. Explain client-side routing in a React SPA.
2. Add React Router to a Vite React app.
3. Define public and protected routes.
4. Build a login page that calls the backend.
5. Store auth state in a React context.
6. Redirect users after login and logout.
7. Use nested routes with Outlet.
8. Protect pages based on JWT/auth state.

## Schedule

### 9:00–9:30 — Recap Day 11

Review components, state, effects, list/detail UI and backend info fetching.

### 9:30–10:15 — Routing concept briefing

Explain SPA routing, URL paths, page components, layout routes, and why full page reloads are avoided.

### 10:15–10:45 — Install and wrap with BrowserRouter

Add `react-router`, update `main.jsx`, and explain BrowserRouter.

### 11:00–12:00 — Build routes and page components

Create `/login`, `/docs`, `/app/dashboard`, `/app/assets`, `/app/reports`, and `*` route.

### 12:00–1:00 — Nested protected layout

Build `AppShell`, navigation, `Outlet`, and active navigation styles.

### 2:00–3:00 — Login and auth context

Build `AuthContext`, call backend login, save token, and redirect after login.

### 3:00–3:45 — ProtectedRoute

Build route guard with `Navigate` and `useLocation`. Test unauthenticated route access.

### 4:00–4:40 — Student exercise

Students add routing and protected pages to Support Desk UI.

### 4:40–5:00 — Review

Students demonstrate login, protected route rejection, redirect, and logout.
