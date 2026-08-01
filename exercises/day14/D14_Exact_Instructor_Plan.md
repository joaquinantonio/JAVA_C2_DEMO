# Day 14 Exact Trainer Plan - Expanded With Files and Methods

## Day 14 Theme

**Data Fetching Patterns & State Management**

Day 13 proved that the frontend can submit data through forms. Day 14 improves how the frontend manages backend data once the application becomes more complex.

Use this teaching phrase:

```text
Day 13: The user can create and update records.
Day 14: The frontend manages backend data in a cleaner, reusable and more scalable way.
```

## Important Teaching Constraint

The frontend visual style is frozen from Day 13.

Do **not** redesign the dashboard, cards, header, buttons, spacing, colours or component sizes unless the new data-fetching feature absolutely needs a small supporting style.

Day 14 is mainly a **frontend architecture** day, not a UI design day.

---

# Files Changed Summary

## Backend

No backend code changes are required for Day 14.

Day 14 uses backend endpoints already available from earlier days:

```http
GET /api/v1/assets/paged?page=0&size=5&sortBy=assetTag&direction=asc
GET /api/v1/assets/{id}
PUT /api/v1/assets/{id}
```

If these endpoints fail, first confirm the Day 13 backend is running and restarted.

## Frontend files to add

```text
frontend/src/services/httpClient.js
frontend/src/context/AssetDataContext.jsx
frontend/src/components/DataControls.jsx
frontend/src/components/PaginationControls.jsx
frontend/src/components/OptimisticStatusControls.jsx
```

## Frontend files to edit

```text
frontend/src/services/api.js
frontend/src/App.jsx
frontend/src/pages/AssetsPage.jsx
frontend/src/components/AppShell.jsx
frontend/src/styles.css
frontend/package.json
frontend/index.html
```

## Request file to add

```text
requests/day14-data-fetching-state.http
```

---

# Methods and Functions Added or Edited

## `frontend/src/services/httpClient.js` - new file

Add these functions:

```js
apiRequest(path, options = {})
buildQueryString(params)
```

Purpose:

- centralise `fetch`
- automatically attach bearer token
- automatically stringify request body
- automatically parse JSON responses
- throw clean errors when response is not OK
- build query string parameters for pagination

## `frontend/src/services/api.js` - edit existing file

Refactor existing functions to use `apiRequest()`.

Keep or update these existing functions:

```js
fetchApiInfo()
fetchApiDocs()
loginRequest(email, password)
fetchAssets(token)
fetchAssetById(id, token)
createAsset(token, payload)
updateAsset(id, token, payload)
fetchReport(path, token)
fetchAssetReports(token)
```

Add this new function:

```js
fetchPagedAssets(token, params)
```

Purpose:

- use the Day 10/Day 13 backend pagination endpoint
- move query string creation away from page components

## `frontend/src/context/AssetDataContext.jsx` - new file

Add these constants/helpers:

```js
AssetDataContext
initialState
makeCacheKey(params)
replaceAsset(items, updatedAsset)
replaceAssetInCache(cache, updatedAsset)
toPageInfo(data, fallback)
toUpdatePayload(asset)
```

Add reducer:

```js
assetReducer(state, action)
```

Reducer action cases:

```text
LOAD_START
LOAD_SUCCESS
LOAD_ERROR
SET_SEARCH_TEXT
SET_STATUS_FILTER
SELECT_ASSET
OPTIMISTIC_UPDATE
UPDATE_SUCCESS
ROLLBACK_UPDATE
```

Add provider and hook:

```js
AssetDataProvider({ children })
useAssetData()
```

Main provider methods exposed to components:

```js
loadAssetsPage(overrides = {})
refreshAssets()
setSearchText(value)
setStatusFilter(value)
selectAsset(assetId)
changeAssetStatus(assetId, nextStatus)
```

Computed values exposed to components:

```js
visibleAssets
selectedAsset
```

## `frontend/src/App.jsx` - edit existing file

Add imports:

```js
import { lazy, Suspense } from 'react';
import { AssetDataProvider } from './context/AssetDataContext.jsx';
import LoadingMessage from './components/LoadingMessage.jsx';
```

Optional but recommended:

```js
const AssetFormPage = lazy(() => import('./pages/AssetFormPage.jsx'));
const ReportsPage = lazy(() => import('./pages/ReportsPage.jsx'));
const DocsPage = lazy(() => import('./pages/DocsPage.jsx'));
const NotFoundPage = lazy(() => import('./pages/NotFoundPage.jsx'));
```

Add helper:

```js
function withFallback(element) {
  return <Suspense fallback={<LoadingMessage message="Loading page..." />}>{element}</Suspense>;
}
```

Wrap protected app layout:

```jsx
<ProtectedRoute>
  <AssetDataProvider>
    <AppShell />
  </AssetDataProvider>
</ProtectedRoute>
```

Purpose:

- all protected child routes can access shared asset data
- the provider only exists after login
- asset data does not need to be passed through props

## `frontend/src/pages/AssetsPage.jsx` - edit existing file heavily

Remove most page-level data state:

```js
useState([])
useState(null)
useState('')
useState('ALL')
useState(true)
useState('')
```

Replace direct fetching with:

```js
const assetData = useAssetData();
```

Use these values from context:

```js
items
visibleAssets
selectedAsset
selectedAssetId
loading
error
pageInfo
filters
cacheMessage
updatingId
```

Use these methods from context:

```js
loadAssetsPage
refreshAssets
setSearchText
setStatusFilter
selectAsset
changeAssetStatus
```

Add an initial load guard:

```js
const initialLoadRef = useRef(false);
```

Purpose:

- prevents repeated initial loads during render cycles
- keeps the first data load predictable for students

## `frontend/src/components/DataControls.jsx` - new file

Add component:

```js
DataControls({
  pageInfo,
  cacheMessage,
  loading,
  onRefresh,
  onPageSizeChange,
  onSortChange
})
```

Purpose:

- controls page size
- controls sort field
- controls sort direction
- displays cache message
- provides manual refresh button

## `frontend/src/components/PaginationControls.jsx` - new file

Add component:

```js
PaginationControls({ pageInfo, loading, onPageChange })
```

Purpose:

- show current page
- show total pages
- show total backend records
- disable Previous on first page
- disable Next on last page

## `frontend/src/components/OptimisticStatusControls.jsx` - new file

Add component:

```js
OptimisticStatusControls({ asset, updatingId, onStatusChange })
```

Purpose:

- update asset status quickly
- demonstrate optimistic UI update
- disable the active status button
- show saving message while backend confirms

## `frontend/src/components/AppShell.jsx` - small edit

Update the day label and subtitle only:

```text
Day 14 Data Fetching & State Management
```

Do not redesign the shell.

## `frontend/src/styles.css` - very small edit only

Add disabled button support:

```css
button:disabled,
.button-link:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}
```

Do not change general style.

## `frontend/package.json` - small edit

The Day 13 dependencies should already work. No new package is required.

Optional name update:

```json
"name": "asset-tracker-ui-day14"
```

## `frontend/index.html` - small edit

Optional title update:

```html
<title>Asset Tracker UI - Day 14</title>
```

---

# Exact Trainer Plan

## 9:00 - 9:20 | Recap Day 13 and Set the Problem

### Files to open

```text
frontend/src/pages/AssetsPage.jsx
frontend/src/services/api.js
frontend/src/pages/AssetFormPage.jsx
```

### What to explain

Day 13 was about forms:

- controlled inputs
- validation
- create mode
- edit mode
- POST and PUT requests
- backend success and error states

Now explain the Day 14 problem:

```text
The app now has several pages and several backend requests.
If every page owns its own fetch logic, loading state, error state, filters and selected record state, the code becomes repetitive and harder to maintain.
```

### What to ask students

Ask:

```text
If AssetsPage, ReportsPage and TicketPage all need loading and error state, should we copy the same fetch logic everywhere?
```

Expected answer:

```text
No. We should centralise repeated patterns.
```

### Teaching phrase

```text
Today we separate three responsibilities:
1. API functions know how to talk to the backend.
2. Context and reducer know how to manage frontend data state.
3. Page components focus mainly on rendering the UI.
```

---

## 9:20 - 10:00 | Explain the Day 14 Architecture

### Files to show first

No new code yet. Draw this flow on screen or board:

```text
AssetsPage
   ↓ uses
useAssetData()
   ↓ provided by
AssetDataContext
   ↓ calls
api.js
   ↓ calls
httpClient.js
   ↓ sends request to
Spring Boot API
```

### Explain each layer

#### `httpClient.js`

Lowest frontend API utility.

It answers:

```text
How do we send HTTP requests consistently?
```

#### `api.js`

Application-specific API functions.

It answers:

```text
What backend endpoint do we call for this feature?
```

#### `AssetDataContext.jsx`

Shared data state for assets.

It answers:

```text
What data do asset screens need and how do we update it?
```

#### `AssetsPage.jsx`

The page UI.

It answers:

```text
What should the user see and what actions can they perform?
```

### Common confusion to clarify

Students may ask:

```text
Why not just use useState and useEffect in the page?
```

Answer:

```text
You can for small pages. But once loading, errors, pagination, selected item, filters, cache and update behaviour all belong together, a reducer and context make the flow easier to organise.
```

---

## 10:00 - 10:45 | Add the API Client Layer

### Files to add/edit

Add:

```text
frontend/src/services/httpClient.js
```

Edit:

```text
frontend/src/services/api.js
```

### Methods to add

In `httpClient.js`:

```js
apiRequest(path, options = {})
buildQueryString(params)
```

In `api.js`:

```js
fetchPagedAssets(token, params)
```

### Teaching sequence

1. Show Day 13 `api.js`.
2. Point out repeated code:
   - `fetch(...)`
   - headers
   - `Content-Type`
   - bearer token
   - JSON parsing
   - error handling
3. Create `httpClient.js`.
4. Move generic request logic into `apiRequest()`.
5. Use `buildQueryString()` for pagination params.
6. Refactor `api.js` to use these helper functions.

### Test after this section

Run backend and frontend.

Login and confirm these still work:

```text
Dashboard
Assets
Reports
API Docs
Create/Edit Asset
```

No visual change should happen yet.

---

## 11:00 - 12:15 | Add Context and Reducer State

### File to add

```text
frontend/src/context/AssetDataContext.jsx
```

### Main pieces to add

```js
const AssetDataContext = createContext(null);
const initialState = { ... };
function assetReducer(state, action) { ... }
export function AssetDataProvider({ children }) { ... }
export function useAssetData() { ... }
```

### State values to explain

```js
items
selectedAssetId
loading
error
cacheMessage
updatingId
cache
pageInfo
filters
```

### Reducer actions to explain slowly

Start with only three:

```text
LOAD_START
LOAD_SUCCESS
LOAD_ERROR
```

Then add:

```text
SET_SEARCH_TEXT
SET_STATUS_FILTER
SELECT_ASSET
```

Then add optimistic update actions later:

```text
OPTIMISTIC_UPDATE
UPDATE_SUCCESS
ROLLBACK_UPDATE
```

### What to say

```text
A reducer is useful when many state values change together. Instead of many separate setState calls across the page, we describe what happened using an action.
```

Example:

```text
LOAD_SUCCESS means the backend has returned data, so we need to update items, loading, error, page information, selected asset and cache message together.
```

### Student checkpoint

Ask:

```text
Which is easier to understand: five separate setState calls, or one LOAD_SUCCESS action that updates related state together?
```

---

## 12:15 - 1:00 | Wrap Protected Routes with Provider

### File to edit

```text
frontend/src/App.jsx
```

### Code to add

Import:

```js
import { AssetDataProvider } from './context/AssetDataContext.jsx';
```

Wrap the protected layout:

```jsx
<ProtectedRoute>
  <AssetDataProvider>
    <AppShell />
  </AssetDataProvider>
</ProtectedRoute>
```

### Why provider goes here

Explain:

```text
The asset data needs the login token. Therefore, the provider should only be used inside the protected area after authentication is available.
```

### Optional performance topic

Also introduce lazy loading here if you want:

```js
import { lazy, Suspense } from 'react';
```

But keep it short. The main Day 14 focus is state/data management, not code splitting.

---

## 2:00 - 2:45 | Refactor AssetsPage to Use Shared Data

### File to edit

```text
frontend/src/pages/AssetsPage.jsx
```

### Imports to add

```js
import { useEffect, useRef } from 'react';
import DataControls from '../components/DataControls.jsx';
import PaginationControls from '../components/PaginationControls.jsx';
import OptimisticStatusControls from '../components/OptimisticStatusControls.jsx';
import { useAssetData } from '../context/AssetDataContext.jsx';
```

### Imports to remove from Day 13 version

Remove direct fetch/data state imports such as:

```js
useMemo
useState
useAuth
fetchAssets
filterAssets
```

### Page-level state to remove

```js
assets
selectedAsset
searchText
statusFilter
loading
error
```

### Replace with context values

```js
const {
  items,
  visibleAssets,
  selectedAsset,
  selectedAssetId,
  loading,
  error,
  pageInfo,
  filters,
  cacheMessage,
  updatingId,
  loadAssetsPage,
  refreshAssets,
  setSearchText,
  setStatusFilter,
  selectAsset,
  changeAssetStatus
} = useAssetData();
```

### Add initial load

```js
const initialLoadRef = useRef(false);
```

Then:

```js
useEffect(() => {
  if (initialLoadRef.current) {
    return;
  }

  initialLoadRef.current = true;
  loadAssetsPage();
}, [loadAssetsPage]);
```

### Explain the ref

```text
useRef stores a value that survives re-rendering, but changing it does not cause another render. Here it is used as a simple flag so the first page load only happens once.
```

---

## 2:45 - 3:15 | Add Pagination and Data Controls

### Files to add

```text
frontend/src/components/DataControls.jsx
frontend/src/components/PaginationControls.jsx
```

### Methods/props involved

`DataControls` receives:

```js
pageInfo
cacheMessage
loading
onRefresh
onPageSizeChange
onSortChange
```

`PaginationControls` receives:

```js
pageInfo
loading
onPageChange
```

### In `AssetsPage.jsx`, add

```jsx
<DataControls
  pageInfo={pageInfo}
  cacheMessage={cacheMessage}
  loading={loading}
  onRefresh={refreshAssets}
  onPageSizeChange={(size) => loadAssetsPage({ page: 0, size })}
  onSortChange={(sortBy, direction) => loadAssetsPage({ page: 0, sortBy, direction })}
/>
```

And near the bottom:

```jsx
<PaginationControls
  pageInfo={pageInfo}
  loading={loading}
  onPageChange={(page) => loadAssetsPage({ page })}
/>
```

### Explain backend vs frontend responsibility

```text
The backend decides which page of records to return.
The frontend decides how to display that page and when to ask for the next page.
```

---

## 3:15 - 3:45 | Add Simple Cache Pattern

### File to edit

```text
frontend/src/context/AssetDataContext.jsx
```

### Methods/helpers involved

```js
makeCacheKey(params)
loadAssetsPage(overrides = {})
```

### What to show

Cache key:

```js
function makeCacheKey(params) {
  return `${params.page}|${params.size}|${params.sortBy}|${params.direction}`;
}
```

Inside `loadAssetsPage()`:

```js
const cacheKey = makeCacheKey(params);
const cachedPage = state.cache[cacheKey];

if (cachedPage && !overrides.force) {
  dispatch({
    type: 'LOAD_SUCCESS',
    data: cachedPage,
    params,
    cacheKey,
    fromCache: true
  });
  return;
}
```

### Demo

1. Load page 1.
2. Go to page 2.
3. Return to page 1.
4. Point out cache message.
5. Click refresh.
6. Explain that refresh forces backend reload.

### Important limitation to explain

```text
This is a simple classroom cache. It is stored only in React state. If the browser refreshes, the cache is gone.
```

---

## 3:45 - 4:20 | Add Optimistic Status Update

### File to add

```text
frontend/src/components/OptimisticStatusControls.jsx
```

### File to edit

```text
frontend/src/context/AssetDataContext.jsx
frontend/src/pages/AssetsPage.jsx
```

### Methods/actions involved

```js
changeAssetStatus(assetId, nextStatus)
toUpdatePayload(asset)
OPTIMISTIC_UPDATE
UPDATE_SUCCESS
ROLLBACK_UPDATE
```

### What to explain

```text
Normally, the UI waits for the backend before changing.
With optimistic update, the UI changes immediately because we expect the backend to succeed.
If the backend fails, we roll the UI back to the previous value.
```

### Demo

1. Select an asset.
2. Click a different status.
3. UI changes immediately.
4. Saving message appears.
5. Backend confirms.
6. Cache message says update confirmed.

### Optional failure demo

Stop the backend and click a status.

Expected behaviour:

```text
The UI changes first, then rolls back after the request fails.
```

Restart Spring Boot after the demo.

---

## 4:20 - 4:40 | Add Small Styling Support

### File to edit

```text
frontend/src/styles.css
```

### Add only this kind of support

```css
button:disabled,
.button-link:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}
```

### Explain

```text
We are not redesigning Day 14. We only add disabled-state styling because pagination and optimistic update buttons need a clear disabled state.
```

---

## 4:40 - 5:00 | Review and Student Mapping

### Student project mapping

Asset Tracker demo maps to Support Desk exercise like this:

```text
AssetDataContext       -> TicketDataContext
fetchPagedAssets       -> fetchPagedTickets
updateAsset            -> updateTicket
changeAssetStatus      -> changeTicketStatus
assets page            -> tickets page
status filter          -> ticket status filter
optimistic asset update -> optimistic ticket status update
```

### Review questions

Ask students:

1. What problem does `httpClient.js` solve?
2. What problem does `api.js` solve?
3. Why did we move asset state into context?
4. Why is reducer useful for this page?
5. What is stored in the cache?
6. What does optimistic update mean?
7. What should happen when an optimistic update fails?
8. Why did we avoid changing the CSS too much?

---

# End-of-Day Expected Demo

By the end of Day 14, the trainer demo should show:

```text
Login works
Assets page loads paged backend data
Page size can change
Sort field can change
Sort direction can change
Current page can be filtered locally
Page navigation works
Cache message changes between backend fetch and cache hit
Refresh forces backend reload
Selected asset displays detail
Selected asset status can be updated optimistically
Failed optimistic update rolls back
Existing Day 13 create/edit form still works
Existing Day 12 layout still looks mostly unchanged
```
