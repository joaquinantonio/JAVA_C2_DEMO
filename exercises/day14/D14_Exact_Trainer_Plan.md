# Day 14 Exact Trainer Plan - Data Fetching Patterns & State Management

## 9:00 - 9:20 Recap Day 13

Review:

- create asset form
- edit asset form
- frontend validation
- backend validation
- successful POST and PUT requests

Explain today's move:

```text
Day 13: Users can submit data.
Day 14: The frontend manages backend data more professionally.
```

## 9:20 - 10:00 Data fetching problems

Discuss common problems:

- repeated fetch code in pages
- loading/error logic duplicated everywhere
- no shared cache
- page state spread across components
- UI feeling slow while waiting for backend

Introduce the solution:

```text
API helper -> data context -> reducer -> page components
```

## 10:00 - 10:45 API abstraction layer

Add:

- `services/httpClient.js`
- `apiRequest()`
- `buildQueryString()`
- refactored `services/api.js`

Show how this reduces repeated fetch/header/error parsing code.

## 11:00 - 12:15 Context and reducer state

Add:

- `AssetDataContext`
- `assetReducer`
- initial state
- loading/success/error actions
- selected asset state
- filter state

Explain why reducer state is useful when many related state values change together.

## 12:15 - 1:00 Pagination and filters

Connect `GET /api/v1/assets/paged`.

Add:

- page number
- page size
- sort field
- sort direction
- local search and status filter for the current page

Clarify:

```text
The backend paginates the data.
The frontend filters the current page for classroom simplicity.
```

## 2:00 - 2:45 Cache pattern

Add a simple page cache.

Demonstrate:

- first page load fetches from backend
- returning to the same page loads from cache
- refresh button forces backend reload

## 2:45 - 3:30 Optimistic update

Add quick status update controls.

Explain:

```text
Optimistic update means the UI changes first.
The backend confirms later.
If the backend fails, the UI rolls back.
```

Use `PUT /api/v1/assets/{id}`.

## 3:45 - 4:30 Student exercises

Students apply the same ideas to Support Desk Ticket API.

## 4:30 - 5:00 Review

Students explain:

- what the API layer does
- what the reducer manages
- what is cached
- how optimistic update works
- where rollback happens
