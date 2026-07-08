# Day 10 Branch Notes

## Recommended Branch

Create a new branch from your working Day 9 code:

```bash
git checkout test/9
git checkout -b test/10
```

## Copy Changed Files

Copy files from:

```text
day10-changed-files-for-existing-test9-repo/
```

into the same paths in your repository.

## Run

```bash
mvn spring-boot:run
```

## Test

Use:

```text
requests/day10-api-quality.http
```

## Commit Message Suggestion

```bash
git add .
git commit -m "Day 10 API quality versioning docs and reports"
git push origin test/10
```
