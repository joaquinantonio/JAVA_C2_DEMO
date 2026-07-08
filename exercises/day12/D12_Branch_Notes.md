# Day 12 Branch Notes

Recommended branch flow:

```bash
git checkout test/11
git checkout -b test/12
```

Apply the changed frontend files:

```text
day12-changed-files-for-existing-test11-repo/frontend/
```

Then run:

```bash
cd frontend
npm install
npm run dev
```

Important: `package.json` adds `react-router`, so `npm install` is required.

The backend must be running:

```bash
mvn spring-boot:run
```

Seeded login:

```text
admin@example.com
Admin@12345
```

Suggested commit message:

```text
Day 12 routing and protected views
```
