# Day 14 Data Fetching & State Management Guide - Expanded Instructor Notes

## 1. Purpose of Day 14

Day 14 moves the frontend from simple page-level fetching into a more structured data management pattern.

By the end of Day 13, students already have:

- protected frontend routes
- login and JWT storage
- asset listing
- asset detail
- create asset form
- edit asset form
- backend `POST` and `PUT` integration

Day 14 answers this question:

```text
As the frontend grows, how do we avoid putting fetch, loading, error, cache, filter, pagination, and update logic inside every page?
```

The teaching focus is:

```text
Separate API communication, shared data state, derived UI state, and user actions.
```

## 2. Suggested opening explanation

Use this script:

```text
Yesterday we learned how to submit data through forms.
Today we improve how the frontend manages backend data.

At first, it is okay for one page to call fetch directly.
But as the app grows, the same problems appear again and again:
- loading state
- error state
- token headers
- JSON parsing
- pagination
- filters
- refresh after update
- avoiding repeated fetch calls

If every page solves these separately, the frontend becomes messy.
So today we introduce patterns to organise data fetching and state management.
```

## 3. What students should understand by the end

Students should be able to explain and implement:

- an API abstraction layer
- a reusable HTTP client helper
- authenticated requests
- context for shared data
- reducer for connected state changes
- page-level loading and error states
- backend pagination
- current-page filtering
- simple cache key pattern
- optimistic update
- rollback after failed optimistic update
- `useMemo` for derived data
- `useCallback` for stable action functions
- `lazy` and `Suspense` for route-level loading

## 4. The problem with direct fetch inside pages

In earlier days, a page could do this:

```jsx
useEffect(() => {
  async function loadAssets() {
    const data = await fetchAssets(token);
    setAssets(data);
  }

  loadAssets();
}, [token]);
```

This is acceptable for beginners, but it creates problems as the app grows.

### 4.1 Problems

If every page fetches directly, each page may repeat:

- how to attach the JWT token
- how to parse JSON
- how to handle errors
- how to track loading
- how to refresh after updates
- how to remember previously loaded data
- how to keep the selected asset consistent

Teaching phrase:

```text
Repeated fetch code is a sign that the app needs a data layer.
```

## 5. API abstraction layer

An API abstraction layer hides HTTP details from page components.

### 5.1 Before abstraction

A page directly knows too much:

```jsx
const response = await fetch('/api/v1/assets', {
  headers: {
    Authorization: `Bearer ${token}`
  }
});

const data = await response.json();
```

### 5.2 After abstraction

The page or context calls a named function:

```js
const data = await fetchAssetsPage(token, params);
```

Teaching explanation:

```text
The component should not care how the HTTP request is built.
It should care about what data it needs.
```

## 6. HTTP client helper

A small `httpClient.js` can centralise common request logic.

Typical responsibilities:

```text
base request structure
Authorization header
Content-Type header
JSON.stringify for request body
JSON parsing for response body
consistent error messages
```

Example pattern:

```js
export async function apiRequest(path, options = {}) {
  const response = await fetch(path, {
    ...options,
    headers: {
      ...(options.token ? { Authorization: `Bearer ${options.token}` } : {}),
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
      ...options.headers
    },
    body: options.body ? JSON.stringify(options.body) : undefined
  });

  const contentType = response.headers.get('content-type') ?? '';
  const body = contentType.includes('application/json') ? await response.json() : null;

  if (!response.ok) {
    throw new Error(body?.message || `Request failed with status ${response.status}`);
  }

  return body;
}
```

### 6.1 Teaching explanation

```text
Instead of teaching every component how to use fetch, we teach one helper how to use fetch.
Then the rest of the app uses clear API functions.
```

### 6.2 Common mistake

Students may forget to include the token.

Symptom:

```text
401 Unauthorized
```

Fix:

```text
Make sure the API helper receives the token and sets Authorization: Bearer <token>.
```

## 7. API function layer

After creating an HTTP client, create named API functions.

Examples:

```js
export function fetchAssetsPage(token, params) {
  const query = new URLSearchParams(params).toString();
  return apiRequest(`/api/v1/assets/paged?${query}`, { token });
}

export function updateAsset(id, token, payload) {
  return apiRequest(`/api/v1/assets/${id}`, {
    method: 'PUT',
    token,
    body: payload
  });
}
```

Teaching point:

```text
httpClient knows how to send requests.
api.js knows which backend endpoints exist.
components know what user action happened.
```

This separation makes the frontend easier to change later.

## 8. Context for shared data

### 8.1 What problem does Context solve?

Without Context, data and functions must be passed through props.

Example problem:

```text
AssetsPage loads assets.
AssetList needs assets.
PaginationControls need page info.
AssetDetail needs selected asset.
Status buttons need update functions.
```

Passing everything through multiple layers becomes messy.

Context allows shared access:

```jsx
const { assets, loading, error, loadPage, updateStatusOptimistically } = useAssetData();
```

### 8.2 Teaching explanation

```text
Context is useful when several components need the same state or actions.
It avoids passing props through components that do not actually use them.
```

### 8.3 Do not overuse Context

Tell students:

```text
Not every state needs Context.
If state is only used by one component, keep it local.
Use Context when the state is shared across a feature area.
```

## 9. Reducer

### 9.1 Why use a reducer?

A reducer is useful when several state values change together.

Example data fetching flow:

```text
FETCH_START
- loading becomes true
- error is cleared

FETCH_SUCCESS
- loading becomes false
- items are updated
- page info is updated
- cache is updated

FETCH_ERROR
- loading becomes false
- error is stored
```

With many separate `useState` calls, it is easy to forget one update.

Teaching phrase:

```text
useState is good for simple state. useReducer is better when state changes follow a workflow.
```

### 9.2 Reducer action example

```js
function assetDataReducer(state, action) {
  switch (action.type) {
    case 'FETCH_START':
      return {
        ...state,
        loading: true,
        error: ''
      };

    case 'FETCH_SUCCESS':
      return {
        ...state,
        loading: false,
        assets: action.payload.assets,
        pageInfo: action.payload.pageInfo
      };

    case 'FETCH_ERROR':
      return {
        ...state,
        loading: false,
        error: action.payload
      };

    default:
      return state;
  }
}
```

### 9.3 Explain reducer in plain English

```text
A reducer is just a function that receives the current state and an action.
Based on the action type, it returns the next state.
```

### 9.4 Common reducer mistake

Wrong:

```js
state.loading = true;
return state;
```

Problem:

```text
This mutates the existing state object.
React may not re-render correctly and the code becomes harder to reason about.
```

Correct:

```js
return {
  ...state,
  loading: true
};
```

## 10. Suggested state shape

A useful Day 14 state shape:

```js
const initialState = {
  assets: [],
  selectedAssetId: null,
  pageInfo: {
    page: 0,
    size: 5,
    totalPages: 0,
    totalElements: 0,
    sortBy: 'assetTag',
    direction: 'asc'
  },
  cache: {},
  loading: false,
  error: '',
  updatingAssetId: ''
};
```

Explain each part:

| State | Meaning |
|---|---|
| `assets` | assets currently displayed |
| `selectedAssetId` | currently selected asset |
| `pageInfo` | backend pagination information |
| `cache` | previously fetched pages |
| `loading` | whether page data is loading |
| `error` | fetch or update error |
| `updatingAssetId` | which asset is currently being updated |

## 11. Backend pagination

Day 14 should use the backend paged endpoint:

```http
GET /api/v1/assets/paged?page=0&size=5&sortBy=assetTag&direction=asc
```

### 11.1 Why pagination matters

Tell students:

```text
Returning all records may work with 10 records.
It will not work well with 10,000 or 1,000,000 records.
Pagination lets the frontend request data in smaller chunks.
```

### 11.2 Frontend pagination controls

The frontend should track:

```text
current page
page size
sort field
sort direction
total pages
total records
```

### 11.3 Common pagination mistake

Backend pages usually start at zero.

```text
First page = page 0
Second page = page 1
Third page = page 2
```

Students may think first page is page 1. Make this explicit.

## 12. Filtering and derived state

Day 14 may apply search and status filtering to the currently loaded page.

Example:

```js
const filteredAssets = useMemo(
  () => filterAssets(assets, searchText, statusFilter),
  [assets, searchText, statusFilter]
);
```

### 12.1 What is derived state?

Derived state is data calculated from other state.

Example:

```text
assets + searchText + statusFilter = filteredAssets
```

You usually do not need to store `filteredAssets` in state because it can be calculated.

Teaching phrase:

```text
Store the source data. Calculate the view data.
```

### 12.2 Explain current-page filtering

If the app filters only the assets already loaded on the current page, explain that limitation:

```text
This search filters the current page only.
A full application may send search filters to the backend so it can search across all records.
```

This is a useful teaching point because it shows the difference between frontend filtering and backend filtering.

## 13. Simple cache pattern

Day 14 introduces a simple in-memory cache.

### 13.1 What is a cache?

A cache stores data that was already fetched so the app does not need to request it again immediately.

Example:

```text
User opens page 0 -> fetch from backend -> store page 0 in cache
User opens page 1 -> fetch from backend -> store page 1 in cache
User returns to page 0 -> use cached page 0
```

### 13.2 Cache key

A cache key should uniquely describe the request.

Example:

```text
page|size|sortBy|direction
```

Example values:

```text
0|5|assetTag|asc
1|5|assetTag|asc
0|10|name|desc
```

### 13.3 Teaching explanation

```text
The cache key is like a label on a box.
If the request parameters are different, the cache key must also be different.
```

### 13.4 Important limitation

This simple cache is for teaching only.

Explain:

```text
This is not a full replacement for tools such as React Query or SWR.
We are building it manually so students understand the idea first.
```

## 14. Cache invalidation after create or update

Caching creates a new question:

```text
What happens if the data changes?
```

If an asset is updated, the cached page may become outdated.

Simple Day 14 strategy:

```text
After an update, update the current UI and clear or refresh affected cache entries.
```

Teaching phrase:

```text
Caching improves speed, but it also means we must think about stale data.
```

## 15. Optimistic update

### 15.1 Simple explanation

Optimistic update means the UI updates immediately before the backend confirms the change.

Flow:

```text
1. User clicks update status.
2. UI changes immediately.
3. Frontend sends PUT request.
4. If backend succeeds, keep the change.
5. If backend fails, roll back to the old value.
```

### 15.2 Why use optimistic update?

It makes the app feel faster.

Example:

```text
The user clicks Mark as Maintenance.
The status badge changes immediately instead of waiting for the network.
```

### 15.3 Why rollback is important

If the backend rejects the update, the UI must not lie.

Example:

```text
Frontend shows ASSIGNED, but backend rejected the request.
```

Rollback restores the previous value.

### 15.4 Teaching phrase

```text
Optimistic UI improves responsiveness, but rollback keeps the UI honest.
```

## 16. Optimistic update example

A simple optimistic flow:

```js
async function changeAssetStatus(asset, nextStatus) {
  const previousAsset = asset;
  const optimisticAsset = {
    ...asset,
    status: nextStatus
  };

  dispatch({ type: 'ASSET_UPDATED_OPTIMISTICALLY', payload: optimisticAsset });

  try {
    const savedAsset = await updateAsset(asset.id, token, optimisticAsset);
    dispatch({ type: 'ASSET_UPDATE_CONFIRMED', payload: savedAsset });
  } catch (err) {
    dispatch({ type: 'ASSET_UPDATE_ROLLED_BACK', payload: previousAsset });
    dispatch({ type: 'FETCH_ERROR', payload: err.message });
  }
}
```

Explain the sequence slowly:

```text
First we remember the old asset.
Then we update the UI immediately.
Then we call the backend.
If the backend succeeds, we keep the saved version.
If the backend fails, we put the old asset back.
```

## 17. Performance concepts

Day 14 introduces some performance-related React tools, but keep the explanation practical.

### 17.1 `useMemo`

Use `useMemo` when deriving data that does not need to be recalculated on every render.

Example:

```js
const filteredAssets = useMemo(
  () => filterAssets(assets, searchText, statusFilter),
  [assets, searchText, statusFilter]
);
```

Teaching explanation:

```text
React will reuse the previous filtered result until assets, searchText, or statusFilter changes.
```

Avoid overcomplicating this. Tell students:

```text
Do not use useMemo everywhere. Use it when you are deriving data and the calculation depends on specific values.
```

### 17.2 `useCallback`

Use `useCallback` when passing functions through Context or to child components and you want the function reference to remain stable unless dependencies change.

Example:

```js
const loadAssetsPage = useCallback(async (nextPage) => {
  // fetch page
}, [token]);
```

Teaching explanation:

```text
useCallback helps React keep the same function reference between renders unless its dependencies change.
```

### 17.3 `lazy` and `Suspense`

`lazy` allows route components to load only when needed.

Example:

```jsx
const ReportsPage = lazy(() => import('./pages/ReportsPage.jsx'));
```

Wrap routes with `Suspense`:

```jsx
<Suspense fallback={<LoadingMessage message="Loading page..." />}>
  <Routes>
    ...
  </Routes>
</Suspense>
```

Teaching explanation:

```text
Instead of loading every page component immediately, lazy loading lets the app load some pages when the user navigates to them.
```

## 18. Recommended Day 14 demo flow

### Demo 1: Show the problem

Open the existing Day 13 `AssetsPage` and explain:

```text
This page fetches assets and manages its own loading, error, search, selected asset and update flow.
As more pages need asset data, this will become harder to maintain.
```

### Demo 2: Create `httpClient.js`

Show how request handling is centralised.

Emphasise:

```text
One place to handle token, JSON and errors.
```

### Demo 3: Refactor `api.js`

Show API functions becoming shorter and clearer.

Example:

```js
fetchAssetsPage(token, params)
updateAsset(id, token, payload)
```

### Demo 4: Create context and reducer

Show:

```text
AssetDataContext
AssetDataProvider
assetDataReducer
useAssetData
```

Explain each role:

```text
Provider stores and manages the data.
Reducer controls how data changes.
Custom hook gives components access to the data.
```

### Demo 5: Add pagination

Use the backend endpoint:

```http
GET /api/v1/assets/paged?page=0&size=5&sortBy=assetTag&direction=asc
```

Show:

```text
Next page
Previous page
Page size change
Sort change
```

### Demo 6: Add cache

Use browser DevTools Network tab if possible.

Show:

```text
First visit to page 0 calls backend.
Move to page 1 calls backend.
Move back to page 0 can use cache.
```

### Demo 7: Add optimistic update

Show status update:

```text
AVAILABLE -> MAINTENANCE
```

Explain:

```text
The UI updates immediately, then the backend confirms.
```

If possible, simulate a failure by stopping backend and showing rollback.

## 19. Common Day 14 student mistakes

| Mistake | Symptom | Fix |
|---|---|---|
| Missing token in API request | 401 Unauthorized | Add Authorization header |
| Wrong page numbering | First page appears empty or wrong | Remember backend starts at page 0 |
| Cache key missing sort field | Wrong cached data appears | Include page, size, sort and direction |
| Mutating reducer state directly | UI does not update reliably | Return new state objects |
| Forgetting reducer default case | State becomes undefined | Return current state by default |
| Not handling failed optimistic update | UI shows wrong status | Roll back previous asset |
| Filtering all data assumption | Search only finds current page results | Explain current-page filtering limitation |
| Overusing useMemo/useCallback | Code becomes confusing | Use only where practical |
| Context used for everything | App becomes harder to maintain | Keep local state local |
| Backend not running | Fetch errors | Start Spring Boot first |

## 20. How to explain Context vs Reducer vs API layer

Use this simple comparison:

| Part | Responsibility |
|---|---|
| `httpClient.js` | How to send HTTP requests |
| `api.js` | Which backend endpoints exist |
| `AssetDataContext` | Makes asset data available to components |
| `assetDataReducer` | Decides how asset state changes |
| `AssetsPage` | Displays the asset feature UI |
| Components | Render specific UI pieces |

Teaching phrase:

```text
Each file should have one main job.
```

## 21. Mapping Day 14 to Support Desk student exercise

Trainer demo uses:

```text
Asset Tracker
```

Student exercise should use:

```text
Support Desk Ticket System
```

Mapping:

| Asset Tracker Demo | Support Desk Exercise |
|---|---|
| `AssetDataContext` | `TicketDataContext` |
| `assetDataReducer` | `ticketDataReducer` |
| `fetchAssetsPage` | `fetchTicketsPage` |
| `updateAsset` | `updateTicket` |
| status update | ticket status update |
| asset cache key | ticket cache key |
| `/api/v1/assets/paged` | `/api/v1/tickets/paged` |

Suggested ticket state:

```js
const initialState = {
  tickets: [],
  selectedTicketId: null,
  pageInfo: {
    page: 0,
    size: 5,
    totalPages: 0,
    totalElements: 0,
    sortBy: 'createdAt',
    direction: 'desc'
  },
  cache: {},
  loading: false,
  error: '',
  updatingTicketId: ''
};
```

Suggested optimistic update:

```text
OPEN -> IN_PROGRESS
IN_PROGRESS -> CLOSED
```

## 22. End-of-day review questions

Ask students:

1. Why should we avoid putting raw fetch logic in every page?
2. What is the job of an API abstraction layer?
3. What does Context solve?
4. When is `useReducer` better than many `useState` calls?
5. What is a cache key?
6. Why can cache data become stale?
7. What is optimistic update?
8. Why do we need rollback?
9. What does `useMemo` help with?
10. What does `lazy` loading help with?
11. What part of Day 14 still feels unclear?

## 23. Final teaching summary

Close Day 14 with this:

```text
Today we made the frontend more professional.
Instead of each page managing backend calls on its own, we created a clearer data structure:
API helper for requests, Context for shared access, reducer for predictable state changes, cache for repeated pages, and optimistic updates for faster user experience.
```

Then connect to Day 15:

```text
Tomorrow, we test the frontend and check whether these flows work reliably.
Testing becomes easier when our code is organised into clearer layers.
```
