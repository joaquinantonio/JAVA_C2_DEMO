# Day 13 Final Working Demo Checklist

## Backend

- [ ] `PUT /api/v1/assets/{id}` works
- [ ] ADMIN can update assets
- [ ] USER cannot create/update assets
- [ ] Duplicate asset tag returns conflict
- [ ] Invalid status returns bad request

## Frontend

- [ ] `/app/assets/new` opens the create wizard
- [ ] `/app/assets/:assetId/edit` opens the edit wizard
- [ ] Empty required fields show inline errors
- [ ] Invalid asset tag format shows inline error
- [ ] Invalid assigned email shows inline error
- [ ] Review checkbox is required before submit
- [ ] Successful create shows success state
- [ ] Successful update shows success state
- [ ] Existing Day 12 dashboard style still looks the same
- [ ] Existing assets, reports, docs and login flows still work

## Student evidence

- [ ] Screenshot of form validation
- [ ] Screenshot of successful create/update
- [ ] Explanation of controlled input
- [ ] Explanation of uncontrolled checkbox
