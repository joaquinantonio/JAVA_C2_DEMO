# Day 8 Code to Show Step by Step

This guide shows what code to reveal during class and what to explain.

## Step 1: Repository query methods

Show `AssetRepository`:

```java
public interface AssetRepository extends MongoRepository<Asset, String> {

    List<Asset> findByStatusIgnoreCase(String status);

    List<Asset> findByCategoryIgnoreCase(String category);

    List<Asset> findByLocationContainingIgnoreCase(String location);
}
```

Explain:

- Spring Data can build queries based on method names.
- `findByStatusIgnoreCase` means search by the `status` field and ignore case.
- `Containing` means partial match.

## Step 2: Controller query parameters

Show `AssetController`:

```java
@GetMapping
public List<AssetResponse> getAssets(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String location) {

    return assetService.getAssets(status, category, location);
}
```

Explain:

- `@RequestParam` reads values from the query string.
- `required = false` means the URL can omit the parameter.

## Step 3: Service filtering logic

Show the `getAssets` method.

Teaching point:

- The controller reads the request.
- The service decides which query to run.
- The repository talks to MongoDB.

## Step 4: Pagination endpoint

Show:

```java
@GetMapping("/paged")
public Page<AssetResponse> getAssetsPaged(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "assetTag") String sortBy,
        @RequestParam(defaultValue = "asc") String direction) {

    return assetService.getAssetsPaged(page, size, sortBy, direction);
}
```

Explain:

- `page=0` means the first page.
- `size=5` means five records per page.
- `sortBy` controls the field used for sorting.
- `direction` controls ascending or descending order.

## Step 5: PageRequest and Sort

Show:

```java
Sort sort = direction.equalsIgnoreCase("desc")
        ? Sort.by(sortBy).descending()
        : Sort.by(sortBy).ascending();

Pageable pageable = PageRequest.of(page, size, sort);
```

Explain:

- `Sort` controls ordering.
- `PageRequest` contains page number, page size, and sorting.
- `MongoRepository` already supports `findAll(pageable)`.

## Step 6: Seed data

Show `AssetDataSeeder`.

Explain:

- It runs when the app starts.
- It inserts sample data only if the collection is empty.
- This makes filtering and pagination easier to test.

## Step 7: Logging

Show:

```java
private static final Logger logger = LoggerFactory.getLogger(AssetService.class);
```

Then show:

```java
logger.info("Fetching assets with status={}, category={}, location={}", status, category, location);
```

Explain:

- Logs are better than random `System.out.println` statements.
- Logs give useful evidence when debugging.

## Step 8: Indexes

Show fields in `Asset`:

```java
@Indexed(unique = true)
private String assetTag;

@Indexed(unique = true)
private String serialNumber;

@Indexed
private String status;
```

Explain:

- Unique indexes prevent duplicate values.
- Normal indexes speed up common filters.
- Do not index every field without reason.
