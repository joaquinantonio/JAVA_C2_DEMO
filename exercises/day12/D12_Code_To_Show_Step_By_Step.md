# Day 12 Code To Show Step By Step

## Step 1 — Add React Router

Show `package.json` and add:

```json
"react-router": "latest"
```

Then run:

```bash
npm install
```

## Step 2 — Wrap app with BrowserRouter

Show `src/main.jsx`:

```jsx
<BrowserRouter>
  <AuthProvider>
    <App />
  </AuthProvider>
</BrowserRouter>
```

Explain that BrowserRouter listens to the browser URL.

## Step 3 — Replace single screen with routes

Show `src/App.jsx`:

```jsx
<Routes>
  <Route path="/login" element={<LoginPage />} />
  <Route path="/docs" element={<DocsPage />} />
  <Route path="/app" element={<ProtectedRoute><AppShell /></ProtectedRoute>}>
    <Route path="dashboard" element={<DashboardPage />} />
    <Route path="assets" element={<AssetsPage />} />
    <Route path="reports" element={<ReportsPage />} />
  </Route>
</Routes>
```

## Step 4 — Build AuthContext

Show how login saves:

```js
localStorage.setItem(STORAGE_KEY, JSON.stringify(nextAuth));
```

Explain this is for classroom demo. Production token handling needs stronger design.

## Step 5 — Build LoginPage

Show controlled email/password fields and submit handler.

## Step 6 — Build ProtectedRoute

Show:

```jsx
if (!isAuthenticated) {
  return <Navigate to="/login" replace state={{ from: location }} />;
}
```

Explain redirect memory.

## Step 7 — Build AppShell and nested pages

Show `NavLink`, `Outlet`, and logout.

## Step 8 — Fetch protected backend data

Show `Authorization: Bearer ${token}` in `services/api.js`.

## Step 9 — Test

1. Go to `/app/assets` without login.
2. Confirm redirect to `/login`.
3. Login with admin.
4. Confirm redirect back.
5. Open Assets and Reports.
6. Logout.
