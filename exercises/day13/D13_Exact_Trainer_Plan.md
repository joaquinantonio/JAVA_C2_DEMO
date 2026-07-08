# Day 13 Exact Trainer Plan - Forms & Validation

## 9:00 - 9:20 Recap Day 12

Review:

- React Router
- ProtectedRoute
- AuthContext
- Login redirect flow
- Protected assets and reports pages

Explain today's move:

```text
Day 12: Users can navigate protected pages.
Day 13: Users can submit data through validated forms.
```

## 9:20 - 10:00 Form concepts

Teach:

- controlled inputs
- uncontrolled inputs
- form state
- validation state
- inline errors
- success state
- accessible labels

Use a simple input example before showing the full wizard.

## 10:00 - 10:45 Backend update endpoint

Add:

- `UpdateAssetRequest`
- `AssetService.updateAsset`
- `PUT /api/v1/assets/{id}`
- ADMIN security rule for PUT

Test with `requests/day13-forms-validation.http`.

## 11:00 - 12:15 Frontend API functions and routes

Add:

- `fetchAssetById`
- `createAsset`
- `updateAsset`
- `/app/assets/new`
- `/app/assets/:assetId/edit`

Explain that routing decides whether the form is in create mode or edit mode.

## 12:15 - 1:00 Asset form wizard

Build the form in stages:

1. Asset identity
2. Location and status
3. Review and submit

Show how validation blocks moving to the next step.

## 2:00 - 3:00 Complete form submission

Connect the form to the backend.

Show:

- successful create
- successful update
- duplicate asset tag error
- invalid status error

## 3:00 - 4:15 Student exercises

Students apply the same pattern to Support Desk Ticket API.

## 4:15 - 5:00 Review

Students submit:

- screenshots or HTTP evidence
- component tree
- validation test cases
- reflection on controlled vs uncontrolled inputs
