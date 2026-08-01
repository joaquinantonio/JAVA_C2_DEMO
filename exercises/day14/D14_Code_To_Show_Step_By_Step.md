# Day 14 Code To Show Step By Step - Expanded With Exact Files and Methods

## Teaching Goal

Do not show the final Day 14 code all at once.

Day 14 can easily overwhelm students because it introduces several frontend architecture ideas together:

- API abstraction
- context
- reducer
- caching
- pagination
- optimistic update
- lazy loading

Teach it as a gradual refactor from Day 13.

Use this framing:

```text
We are not replacing Day 13. We are organising it better because the application is getting larger.
```

---

# Step 0 - Show the Day 13 Problem First

## File to open

```text
frontend/src/pages/AssetsPage.jsx
```

## What to point out

Day 13 `AssetsPage` handles many responsibilities directly:

```text
1. It stores the asset list.
2. It stores the selected asset.
3. It stores search and filter state.
4. It stores loading state.
5. It stores error state.
6. It calls the backend directly.
7. It decides what to render.
```

## What to say

```text
This works, but as the app grows, the page becomes too responsible. A page component should focus mainly on UI. Reusable data logic should move somewhere else.
```

## Student question

Ask:

```text
If tomorrow we need the same asset data on the dashboard, should we copy the same useEffect and useState code there?
```

Expected answer:

```text
No, that would duplicate logic.
```

---

# Step 1 - Add `httpClient.js`

## File to add

```text
frontend/src/services/httpClient.js
```

## Code to show

```js
export async function apiRequest(path, options = {}) {
  const {
    method = 'GET',
    token = '',
    body,
    headers = {}
  } = options;

  const requestHeaders = { ...headers };

  if (token) {
    requestHeaders.Authorization = `Bearer ${token}`;
  }

  if (body !== undefined) {
    requestHeaders['Content-Type'] = 'application/json';
  }

  const response = await fetch(path, {
    method,
    headers: requestHeaders,
    body: body !== undefined ? JSON.stringify(body) : undefined
  });

  const contentType = response.headers.get('content-type') ?? '';
  const data = contentType.includes('application/json') ? await response.json() : null;

  if (!response.ok) {
    const message = data?.message || `Request failed with status ${response.status}`;
    throw new Error(message);
  }

  return data;
}
```

## What to explain line by line

### `method = 'GET'`

Most requests that read data are GET requests, so GET is the default.

### `token = ''`

Some endpoints are public and do not need a token. Protected endpoints pass a token.

### `body`

Only POST and PUT usually send a body. If body exists, we convert it to JSON.

### `Authorization`

```js
requestHeaders.Authorization = `Bearer ${token}`;
```

This is how the frontend sends the JWT received from login.

### `Content-Type`

```js
requestHeaders['Content-Type'] = 'application/json';
```

This tells Spring Boot that the request body is JSON.

### JSON parsing

```js
const data = contentType.includes('application/json') ? await response.json() : null;
```

This avoids trying to parse JSON from an empty response.

### Error handling

```js
if (!response.ok) {
  throw new Error(message);
}
```

This lets page components use `try/catch` instead of checking HTTP status manually every time.

---

# Step 2 - Add `buildQueryString()`

## Same file

```text
frontend/src/services/httpClient.js
```

## Code to show

```js
export function buildQueryString(params) {
  const searchParams = new URLSearchParams();

  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      searchParams.set(key, value);
    }
  });

  return searchParams.toString();
}
```

## Explain using Day 14 endpoint

The backend expects this:

```http
GET /api/v1/assets/paged?page=0&size=5&sortBy=assetTag&direction=asc
```

Instead of manually joining strings, we build the query string from an object:

```js
buildQueryString({
  page: 0,
  size: 5,
  sortBy: 'assetTag',
  direction: 'asc'
});
```

This produces:

```text
page=0&size=5&sortBy=assetTag&direction=asc
```

## Common mistake

Students may include empty values in query strings.

Example problem:

```text
status=&category=&location=
```

Explain that the helper skips empty values.

---

# Step 3 - Refactor `api.js`

## File to edit

```text
frontend/src/services/api.js
```

## Add import

```js
import { apiRequest, buildQueryString } from './httpClient.js';
```

## Show before and after

### Before Day 14 style

```js
export async function fetchAssets(token) {
  const response = await fetch('/api/v1/assets', {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  return parseJsonResponse(response);
}
```

### After Day 14 style

```js
export async function fetchAssets(token) {
  return apiRequest('/api/v1/assets', { token });
}
```

## Add new method

```js
export async function fetchPagedAssets(token, params) {
  const queryString = buildQueryString({
    page: params.page,
    size: params.size,
    sortBy: params.sortBy,
    direction: params.direction
  });

  return apiRequest(`/api/v1/assets/paged?${queryString}`, { token });
}
```

## Keep existing methods but refactor them

```js
export async function loginRequest(email, password) {
  return apiRequest('/api/auth/login', {
    method: 'POST',
    body: { email, password }
  });
}

export async function createAsset(token, payload) {
  return apiRequest('/api/v1/assets', {
    method: 'POST',
    token,
    body: payload
  });
}

export async function updateAsset(id, token, payload) {
  return apiRequest(`/api/v1/assets/${id}`, {
    method: 'PUT',
    token,
    body: payload
  });
}
```

## What to say

```text
api.js should describe our application endpoints. It should not repeat low-level fetch setup each time.
```

---

# Step 4 - Create `AssetDataContext.jsx`

## File to add

```text
frontend/src/context/AssetDataContext.jsx
```

## Start with imports

```js
import { createContext, useCallback, useContext, useMemo, useReducer } from 'react';
import { fetchPagedAssets, updateAsset } from '../services/api.js';
import { filterAssets } from '../utils/assets.js';
import { useAuth } from './AuthContext.jsx';
```

## Explain each import

```text
createContext/useContext: share asset data with child components.
useReducer: manage related state changes.
useCallback: keep functions stable between renders.
useMemo: calculate derived values efficiently.
fetchPagedAssets/updateAsset: communicate with backend.
filterAssets: reuse Day 11/12 filtering logic.
useAuth: get the JWT token.
```

---

# Step 5 - Add `initialState`

## File

```text
frontend/src/context/AssetDataContext.jsx
```

## Code to show

```js
const initialState = {
  items: [],
  selectedAssetId: '',
  loading: false,
  error: '',
  cacheMessage: 'No cached page loaded yet.',
  updatingId: '',
  cache: {},
  pageInfo: {
    page: 0,
    size: 5,
    sortBy: 'assetTag',
    direction: 'asc',
    totalPages: 0,
    totalElements: 0
  },
  filters: {
    searchText: '',
    statusFilter: 'ALL'
  }
};
```

## Explain each part

### `items`

The current page of assets loaded from backend.

### `selectedAssetId`

Stores only the selected asset ID, not the full object. This avoids stale selected object problems.

### `loading`

Used when the page is fetching or refreshing.

### `error`

Stores the latest backend or update error message.

### `cacheMessage`

Used for teaching. It shows whether the page came from backend or cache.

### `updatingId`

Stores which asset is currently being updated optimistically.

### `cache`

An object storing previously loaded pages.

### `pageInfo`

Stores backend pagination information.

### `filters`

Stores frontend-only search and status filtering for the current page.

---

# Step 6 - Add Helper Methods

## File

```text
frontend/src/context/AssetDataContext.jsx
```

## `makeCacheKey(params)`

```js
function makeCacheKey(params) {
  return `${params.page}|${params.size}|${params.sortBy}|${params.direction}`;
}
```

Explain:

```text
Different page settings should have different cache entries.
Page 0 with size 5 is not the same as page 0 with size 10.
```

## `replaceAsset(items, updatedAsset)`

```js
function replaceAsset(items, updatedAsset) {
  return items.map((asset) => (asset.id === updatedAsset.id ? updatedAsset : asset));
}
```

Explain:

```text
React state should be treated as immutable. Instead of changing an asset inside the array directly, we create a new array with the updated asset swapped in.
```

## `replaceAssetInCache(cache, updatedAsset)`

```js
function replaceAssetInCache(cache, updatedAsset) {
  const nextCache = {};

  Object.entries(cache).forEach(([key, pageData]) => {
    nextCache[key] = {
      ...pageData,
      content: replaceAsset(pageData.content ?? [], updatedAsset)
    };
  });

  return nextCache;
}
```

Explain:

```text
If we update an asset, the same asset may exist in a cached page. We update the cache too so the old value does not reappear later.
```

## `toPageInfo(data, fallback)`

```js
function toPageInfo(data, fallback) {
  return {
    page: data.number ?? fallback.page,
    size: data.size ?? fallback.size,
    sortBy: fallback.sortBy,
    direction: fallback.direction,
    totalPages: data.totalPages ?? 0,
    totalElements: data.totalElements ?? 0
  };
}
```

Explain:

```text
The backend returns page number, size, total pages and total elements. The frontend also keeps sortBy and direction because those came from the request.
```

---

# Step 7 - Add the Reducer

## File

```text
frontend/src/context/AssetDataContext.jsx
```

## Code skeleton to show first

```js
function assetReducer(state, action) {
  switch (action.type) {
    case 'LOAD_START':
      return state;

    case 'LOAD_SUCCESS':
      return state;

    case 'LOAD_ERROR':
      return state;

    default:
      return state;
  }
}
```

Then fill in each case gradually.

## `LOAD_START`

```js
case 'LOAD_START':
  return {
    ...state,
    loading: true,
    error: '',
    cacheMessage: action.fromCache ? 'Reading from cache...' : 'Fetching from backend...'
  };
```

Explain:

```text
When loading starts, clear old error and show loading state.
```

## `LOAD_SUCCESS`

```js
case 'LOAD_SUCCESS': {
  const items = action.data.content ?? [];
  const selectedStillVisible = items.some((asset) => asset.id === state.selectedAssetId);
  const selectedAssetId = selectedStillVisible ? state.selectedAssetId : items[0]?.id ?? '';
  const nextCache = action.fromCache
    ? state.cache
    : { ...state.cache, [action.cacheKey]: action.data };

  return {
    ...state,
    items,
    selectedAssetId,
    loading: false,
    error: '',
    pageInfo: toPageInfo(action.data, action.params),
    cache: nextCache,
    cacheMessage: action.fromCache ? 'Loaded from cache.' : 'Fetched from backend and cached.'
  };
}
```

Explain:

```text
A successful load updates many related values at the same time. This is why reducer is helpful.
```

## `LOAD_ERROR`

```js
case 'LOAD_ERROR':
  return {
    ...state,
    loading: false,
    error: action.message,
    cacheMessage: 'Could not load data.'
  };
```

Explain:

```text
Errors should not crash the page. Store the error and show it to the user.
```

## Filter and selection cases

```js
case 'SET_SEARCH_TEXT':
  return {
    ...state,
    filters: { ...state.filters, searchText: action.value }
  };

case 'SET_STATUS_FILTER':
  return {
    ...state,
    filters: { ...state.filters, statusFilter: action.value }
  };

case 'SELECT_ASSET':
  return {
    ...state,
    selectedAssetId: action.assetId
  };
```

Explain:

```text
These actions update user interface state without calling the backend.
```

---

# Step 8 - Add Provider Methods

## File

```text
frontend/src/context/AssetDataContext.jsx
```

## Provider shell

```js
export function AssetDataProvider({ children }) {
  const { token } = useAuth();
  const [state, dispatch] = useReducer(assetReducer, initialState);

  return <AssetDataContext.Provider value={value}>{children}</AssetDataContext.Provider>;
}
```

## Add `loadAssetsPage()`

```js
const loadAssetsPage = useCallback(async (overrides = {}) => {
  const params = {
    page: overrides.page ?? state.pageInfo.page,
    size: overrides.size ?? state.pageInfo.size,
    sortBy: overrides.sortBy ?? state.pageInfo.sortBy,
    direction: overrides.direction ?? state.pageInfo.direction
  };

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

  dispatch({ type: 'LOAD_START', fromCache: false });

  try {
    const data = await fetchPagedAssets(token, params);
    dispatch({
      type: 'LOAD_SUCCESS',
      data,
      params,
      cacheKey,
      fromCache: false
    });
  } catch (error) {
    dispatch({
      type: 'LOAD_ERROR',
      message: error.message || 'Could not load paged assets.'
    });
  }
}, [state.cache, state.pageInfo, token]);
```

## Explain the `overrides` object

Examples:

```js
loadAssetsPage({ page: 1 })
loadAssetsPage({ size: 10, page: 0 })
loadAssetsPage({ sortBy: 'name', direction: 'desc', page: 0 })
loadAssetsPage({ force: true })
```

This makes one method flexible enough for pagination, sorting and refresh.

## Add small action functions

```js
const refreshAssets = useCallback(() => {
  return loadAssetsPage({ force: true });
}, [loadAssetsPage]);

const setSearchText = useCallback((value) => {
  dispatch({ type: 'SET_SEARCH_TEXT', value });
}, []);

const setStatusFilter = useCallback((value) => {
  dispatch({ type: 'SET_STATUS_FILTER', value });
}, []);

const selectAsset = useCallback((assetId) => {
  dispatch({ type: 'SELECT_ASSET', assetId });
}, []);
```

---

# Step 9 - Add Derived Data with `useMemo`

## File

```text
frontend/src/context/AssetDataContext.jsx
```

## Code to show

```js
const visibleAssets = useMemo(
  () => filterAssets(state.items, state.filters.searchText, state.filters.statusFilter),
  [state.items, state.filters]
);
```

Explain:

```text
visibleAssets is derived from items plus filters. We do not need to store it separately in state.
```

Then show:

```js
const selectedAsset = useMemo(() => {
  return visibleAssets.find((asset) => asset.id === state.selectedAssetId) ?? visibleAssets[0] ?? null;
}, [state.selectedAssetId, visibleAssets]);
```

Explain:

```text
The selected asset is calculated from the selected ID and visible assets. If the selected asset disappears because of filtering, the page falls back to the first visible asset.
```

---

# Step 10 - Add `useAssetData()` Hook

## File

```text
frontend/src/context/AssetDataContext.jsx
```

## Code to show

```js
export function useAssetData() {
  const value = useContext(AssetDataContext);

  if (!value) {
    throw new Error('useAssetData must be used inside AssetDataProvider');
  }

  return value;
}
```

## Explain

```text
This custom hook gives components a clean way to access asset data. The error helps us detect if we forgot to wrap the component with AssetDataProvider.
```

---

# Step 11 - Wrap Routes in `App.jsx`

## File to edit

```text
frontend/src/App.jsx
```

## Add import

```js
import { AssetDataProvider } from './context/AssetDataContext.jsx';
```

## Replace protected route element

```jsx
<Route
  path="/app"
  element={
    <ProtectedRoute>
      <AssetDataProvider>
        <AppShell />
      </AssetDataProvider>
    </ProtectedRoute>
  }
>
```

## Explain

```text
The provider wraps AppShell, so dashboard, assets, reports and nested protected pages can access asset data if needed.
```

## Optional code splitting

Show briefly:

```js
const ReportsPage = lazy(() => import('./pages/ReportsPage.jsx'));
```

Explain:

```text
lazy loading means the browser can load some page code only when that page is needed. This is a performance concept, but do not spend too long here today.
```

---

# Step 12 - Refactor `AssetsPage.jsx`

## File to edit

```text
frontend/src/pages/AssetsPage.jsx
```

## Imports to show

```js
import { useEffect, useRef } from 'react';
import { Link } from 'react-router';
import DataControls from '../components/DataControls.jsx';
import OptimisticStatusControls from '../components/OptimisticStatusControls.jsx';
import PaginationControls from '../components/PaginationControls.jsx';
import { useAssetData } from '../context/AssetDataContext.jsx';
```

## Main code to show

```js
const initialLoadRef = useRef(false);

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

## Explain

```text
AssetsPage no longer owns all the asset data logic. It asks the context for the data and actions it needs.
```

## Initial load code

```js
useEffect(() => {
  if (initialLoadRef.current) {
    return;
  }

  initialLoadRef.current = true;
  loadAssetsPage();
}, [loadAssetsPage]);
```

## Explain simply

```text
When the page opens for the first time, load the first page of assets.
```

---

# Step 13 - Add `DataControls.jsx`

## File to add

```text
frontend/src/components/DataControls.jsx
```

## Code idea to show

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

## Explain each callback

### `onRefresh`

Forces a backend reload.

### `onPageSizeChange`

Changes how many records the backend returns per page. Reset page to 0.

### `onSortChange`

Changes sort field or direction. Reset page to 0.

---

# Step 14 - Add `PaginationControls.jsx`

## File to add

```text
frontend/src/components/PaginationControls.jsx
```

## Code idea to show

```jsx
<PaginationControls
  pageInfo={pageInfo}
  loading={loading}
  onPageChange={(page) => loadAssetsPage({ page })}
/>
```

## Explain

```text
PaginationControls does not know how to fetch data. It only tells the parent which page the user requested.
```

## Buttons to explain

```js
disabled={loading || isFirstPage}
disabled={loading || isLastPage}
```

Explain:

```text
Disable buttons when clicking them would not make sense.
```

---

# Step 15 - Add Optimistic Update

## Files involved

```text
frontend/src/context/AssetDataContext.jsx
frontend/src/components/OptimisticStatusControls.jsx
frontend/src/pages/AssetsPage.jsx
```

## Add reducer actions

```js
case 'OPTIMISTIC_UPDATE':
  return {
    ...state,
    updatingId: action.asset.id,
    items: replaceAsset(state.items, action.asset),
    cache: replaceAssetInCache(state.cache, action.asset)
  };

case 'UPDATE_SUCCESS':
  return {
    ...state,
    updatingId: '',
    items: replaceAsset(state.items, action.asset),
    cache: replaceAssetInCache(state.cache, action.asset),
    cacheMessage: 'Optimistic update confirmed by backend.'
  };

case 'ROLLBACK_UPDATE':
  return {
    ...state,
    updatingId: '',
    items: replaceAsset(state.items, action.asset),
    cache: replaceAssetInCache(state.cache, action.asset),
    error: action.message,
    cacheMessage: 'Optimistic update rolled back.'
  };
```

## Add `toUpdatePayload(asset)`

```js
function toUpdatePayload(asset) {
  return {
    assetTag: asset.assetTag,
    name: asset.name,
    category: asset.category,
    serialNumber: asset.serialNumber,
    status: asset.status,
    location: asset.location,
    assignedTo: asset.assignedTo ?? ''
  };
}
```

Explain:

```text
The backend PUT endpoint expects the full update request, not just the status.
```

## Add `changeAssetStatus()`

```js
const changeAssetStatus = useCallback(async (assetId, nextStatus) => {
  const currentAsset = state.items.find((asset) => asset.id === assetId);

  if (!currentAsset || currentAsset.status === nextStatus) {
    return;
  }

  const optimisticAsset = { ...currentAsset, status: nextStatus };
  dispatch({ type: 'OPTIMISTIC_UPDATE', asset: optimisticAsset });

  try {
    const savedAsset = await updateAsset(assetId, token, toUpdatePayload(optimisticAsset));
    dispatch({ type: 'UPDATE_SUCCESS', asset: savedAsset });
  } catch (error) {
    dispatch({
      type: 'ROLLBACK_UPDATE',
      asset: currentAsset,
      message: error.message || 'Could not update asset status. Reverted local change.'
    });
  }
}, [state.items, token]);
```

## Explain the flow

```text
1. Find the current asset.
2. Create a copy with the new status.
3. Update the UI immediately.
4. Send PUT request to backend.
5. If backend succeeds, keep the saved asset.
6. If backend fails, restore the previous asset.
```

---

# Step 16 - Add `OptimisticStatusControls.jsx`

## File to add

```text
frontend/src/components/OptimisticStatusControls.jsx
```

## Code idea to show

```js
const STATUSES = ['AVAILABLE', 'ASSIGNED', 'MAINTENANCE'];
```

Then:

```jsx
{STATUSES.map((status) => (
  <button
    key={status}
    type="button"
    className={status === asset.status ? 'button-link' : 'button-link secondary'}
    disabled={isUpdating || status === asset.status}
    onClick={() => onStatusChange(asset.id, status)}
  >
    {status}
  </button>
))}
```

## Explain

```text
This component does not call the backend directly. It only tells the context which status the user selected.
```

---

# Step 17 - Add Minimal CSS Support

## File to edit

```text
frontend/src/styles.css
```

## Code to show

```css
button:disabled,
.button-link:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}
```

## Explain

```text
This is not a redesign. It only helps users understand when a button is temporarily unavailable.
```

---

# Step 18 - Test in Small Stages

## Test 1 - Existing login

```text
Login using admin@example.com / Admin@12345
```

Expected:

```text
Redirect to dashboard.
```

## Test 2 - Assets page initial load

Expected:

```text
First page loads from backend.
Cache message says backend fetch happened.
```

## Test 3 - Pagination

Click Next.

Expected:

```text
Next page loads.
Previous button becomes available.
```

## Test 4 - Cache

Go back to previous page.

Expected:

```text
Page loads from cache.
```

## Test 5 - Refresh

Click Refresh from backend.

Expected:

```text
Backend is called again.
```

## Test 6 - Filter current page

Search or choose status.

Expected:

```text
Only current page records are filtered.
```

Important explanation:

```text
This is current-page filtering for classroom simplicity. Full backend filtering across all pages is a later improvement.
```

## Test 7 - Optimistic update

Change selected asset status.

Expected:

```text
Status changes immediately.
Saving message appears.
Backend confirms.
```

## Test 8 - Rollback demo

Optional: stop backend and change status.

Expected:

```text
Status changes first, then rolls back after failure.
```

---

# Common Mistakes and Fixes

## Mistake 1 - `useAssetData must be used inside AssetDataProvider`

Cause:

```text
AssetsPage is using useAssetData(), but App.jsx did not wrap AppShell with AssetDataProvider.
```

Fix:

```jsx
<AssetDataProvider>
  <AppShell />
</AssetDataProvider>
```

## Mistake 2 - 401 on assets page

Cause:

```text
Token is missing, expired, or not passed into apiRequest.
```

Check:

```js
apiRequest('/api/v1/assets/paged?...', { token })
```

## Mistake 3 - 403 on optimistic update

Cause:

```text
Logged-in user is authenticated but does not have ADMIN role.
```

Fix:

```text
Login using the seeded admin account.
```

## Mistake 4 - Pagination always loads same page

Cause:

```text
loadAssetsPage() is not receiving the new page number.
```

Check:

```jsx
onPageChange={(page) => loadAssetsPage({ page })}
```

## Mistake 5 - Page keeps reloading repeatedly

Cause:

```text
useEffect dependency loop or initial load not guarded.
```

Fix:

```js
const initialLoadRef = useRef(false);
```

## Mistake 6 - Old status appears again after cache navigation

Cause:

```text
Updated asset was changed in items but not in cache.
```

Fix:

```js
cache: replaceAssetInCache(state.cache, action.asset)
```

---

# Final Trainer Summary

End Day 14 with this summary:

```text
Today we improved the frontend architecture. We moved repeated fetch logic into an API client, moved asset data logic into Context and Reducer, added backend pagination, added a simple cache, and demonstrated optimistic updates. The UI looks almost the same, but the code is now more scalable.
```
