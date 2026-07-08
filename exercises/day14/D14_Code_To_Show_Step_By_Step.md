# Day 14 Code To Show Step By Step

## Step 1 - Show the Day 13 problem

Show `AssetsPage` from Day 13.

Point out that the page owns many separate state values:

- assets
- selected asset
- filters
- loading
- error

## Step 2 - Add `httpClient.js`

Show:

```js
apiRequest(path, options)
```

Explain that it centralises:

- JSON parsing
- error handling
- Authorization header
- request body handling

## Step 3 - Refactor `api.js`

Show:

```js
fetchPagedAssets(token, params)
updateAsset(id, token, payload)
```

## Step 4 - Add reducer state

Show:

```js
function assetReducer(state, action) {
  switch (action.type) {
    case 'LOAD_START':
    case 'LOAD_SUCCESS':
    case 'LOAD_ERROR':
  }
}
```

## Step 5 - Add `AssetDataProvider`

Wrap protected routes with the provider.

## Step 6 - Refactor `AssetsPage`

Replace page-level fetching state with:

```js
const assetData = useAssetData();
```

## Step 7 - Add pagination controls

Connect:

```http
GET /api/v1/assets/paged?page=0&size=5&sortBy=assetTag&direction=asc
```

## Step 8 - Add cache logic

Show the cache key:

```js
page|size|sortBy|direction
```

Explain cache hit vs backend fetch.

## Step 9 - Add optimistic update

Show:

```js
dispatch({ type: 'OPTIMISTIC_UPDATE', asset: optimisticAsset });
await updateAsset(...);
dispatch({ type: 'UPDATE_SUCCESS', asset: savedAsset });
```

Then show rollback:

```js
dispatch({ type: 'ROLLBACK_UPDATE', asset: currentAsset });
```
