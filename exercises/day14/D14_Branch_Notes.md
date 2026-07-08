# Day 14 Branch Notes

Start from the working Day 13 branch.

```bash
git checkout test/13
git checkout -b test/14
```

Copy the files from:

```text
day14-changed-files-for-existing-test13-repo/
```

into the repository root.

## Backend changes

No backend code changes are required for Day 14.

Day 14 reuses:

- `GET /api/v1/assets/paged`
- `GET /api/v1/assets/{id}`
- `PUT /api/v1/assets/{id}`
- existing JWT authentication
- existing ADMIN role for updates

## Frontend changes

- Added `httpClient.js`
- Updated `api.js` to use the API abstraction layer
- Added `AssetDataContext`
- Refactored `AssetsPage` to use shared reducer state
- Added pagination controls
- Added data/cache controls
- Added optimistic status update controls
- Added lazy route loading

## Style freeze note

Do not change existing card width, grid width, header layout, dashboard design, colours, or spacing unless absolutely necessary.
