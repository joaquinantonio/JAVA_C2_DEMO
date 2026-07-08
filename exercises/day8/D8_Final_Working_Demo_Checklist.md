# Day 8 Final Working Demo Checklist

Use this checklist before teaching.

## MongoDB

- [ ] MongoDB is running locally.
- [ ] Compass can connect to `mongodb://localhost:27017`.
- [ ] Database `asset_tracker_db` is available after app starts.
- [ ] `assets` collection has sample seed data.

## Spring Boot

- [ ] App starts with local profile.
- [ ] `GET /api/health` works.
- [ ] `GET /api/assets` returns all assets.
- [ ] `GET /api/assets?status=AVAILABLE` filters by status.
- [ ] `GET /api/assets?category=Laptop` filters by category.
- [ ] `GET /api/assets?location=HQ` filters by location.
- [ ] `GET /api/assets/paged?page=0&size=5` returns paginated data.
- [ ] `GET /api/assets/paged?page=1&size=5` returns second page.
- [ ] `sortBy` and `direction` work.
- [ ] Logs appear in the terminal.
- [ ] Index annotations are present in `Asset`.

## Teaching evidence

Show students:

1. Request URL
2. Status code
3. Response body
4. Terminal log
5. Data in MongoDB Compass

## Before running the Day 8 demo

If you already inserted test data during Day 7, clear the collection before the Day 8 demo so unique indexes and seed data work cleanly:

```javascript
use asset_tracker_db
db.assets.drop()
```

Then restart Spring Boot. The Day 8 seeder will insert a larger sample dataset.
