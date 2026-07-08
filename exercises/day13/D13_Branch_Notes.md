# Day 13 Branch Notes

Start from the working Day 12 branch.

```bash
git checkout test/12
git checkout -b test/13
```

Copy the files from:

```text
day13-changed-files-for-existing-test12-repo/
```

into the root of the repository.

## What changed

### Backend

- Added `UpdateAssetRequest`
- Added `PUT /api/v1/assets/{id}`
- Added asset update logic in `AssetService`
- Added `setAssignedTo` to `Asset`
- Allowed `PUT /api/v1/assets/**` for ADMIN users

### Frontend

- Added create asset route: `/app/assets/new`
- Added update asset route: `/app/assets/:assetId/edit`
- Added `AssetFormPage`
- Added `AssetFormWizard`
- Added inline field validation
- Added create/update API functions
- Preserved Day 12 visual style and component sizing

## Style freeze note

Avoid changing existing card sizes, grid widths, header layout, colours, or spacing. Day 13 should look like Day 12 with a form added.
