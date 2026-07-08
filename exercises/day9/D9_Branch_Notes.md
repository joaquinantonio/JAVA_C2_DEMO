# Day 9 Branch Notes

This Day 9 package is based on the current Day 8 direction:

```text
Spring Boot 4.1.0
Spring WebMVC
Spring Data MongoDB
Day 8 query, pagination, indexes, logging, duplicate handling
```

## Recommended branch flow

```bash
git checkout test/8
git checkout -b test/9
```

Then copy files from:

```text
day9-changed-files-for-existing-test8-repo/
```

## Important

Do not remove Day 8 functionality. Day 9 should add security on top of existing endpoints.

## Demo-only secret

The `app.jwt.secret` default value is included only for classroom convenience. In real applications, use an environment variable:

```bash
export JWT_SECRET=your-real-secret-value
```
