# Day 12 Final Working Demo Checklist

## Backend

- [ ] Spring Boot Day 10 backend is running on port 8080.
- [ ] Seeded admin login works.
- [ ] `/api/auth/login` returns a JWT.
- [ ] `/api/v1/assets` requires a token.
- [ ] `/api/v1/reports/assets-by-status` requires a token.

## Frontend

- [ ] `npm install` completed.
- [ ] `npm run dev` starts Vite on port 5173.
- [ ] `/login` loads.
- [ ] `/docs` loads without login.
- [ ] `/app/assets` redirects to `/login` when logged out.
- [ ] Login redirects to the intended protected route.
- [ ] Dashboard shows logged-in user.
- [ ] Assets page fetches protected asset data.
- [ ] Reports page fetches protected report data.
- [ ] Logout clears auth state and returns to login.

## Student evidence

- [ ] Screenshot of login page.
- [ ] Screenshot of protected redirect.
- [ ] Screenshot of assets after login.
- [ ] Screenshot of reports after login.
- [ ] Short explanation of BrowserRouter, Routes, Navigate and Outlet.
