# Day 7 Code to Show Step-by-Step

This guide tells you what code to reveal and what to explain before giving each exercise.

---

# Stage 1: Show the New Dependency

## Show

`pom.xml`

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

## Explain

This dependency gives Spring Boot the ability to work with MongoDB using Spring Data MongoDB.

It gives us annotations such as:

```text
@Document
@Id
```

And repository support through:

```text
MongoRepository
```

Do not explain advanced Spring Data features yet.

---

# Stage 2: Show the MongoDB URI

## Show

`application-local.properties`

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/asset_tracker_db
```

## Explain

This tells Spring Boot where MongoDB is running.

```text
mongodb://localhost:27017 = MongoDB server on this machine
asset_tracker_db = database name
```

If the database does not exist yet, MongoDB can create it when data is inserted.

---

# Stage 3: Show the Asset Model

## Show

`model/Asset.java`

```java
@Document(collection = "assets")
public class Asset {

    @Id
    private String id;

    private String assetTag;
    private String name;
    private String category;
    private String serialNumber;
    private String status;
    private String location;
    private String assignedTo;
}
```

## Explain

```text
@Document maps this Java class to a MongoDB collection.
collection = "assets" means the documents go into the assets collection.
@Id marks the field that maps to MongoDB's _id field.
```

Important explanation:

```text
MongoDB stores the ID as _id.
In Java, we usually call it id and mark it with @Id.
```

---

# Stage 4: Show the Repository

## Show

`repository/AssetRepository.java`

```java
public interface AssetRepository extends MongoRepository<Asset, String> {
}
```

## Explain

This interface looks empty, but it is powerful.

Spring Data gives it methods such as:

```text
findAll()
findById(id)
save(asset)
deleteById(id)
count()
```

Explain the generic types:

```text
Asset = the document type
String = the ID type
```

---

# Stage 5: Show the Service Constructor Injection

## Show

`AssetService.java`

```java
private final AssetRepository assetRepository;

public AssetService(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
}
```

## Explain

Yesterday the service depended on a Java list.

Today the service depends on a repository.

Spring creates the repository object and injects it into the service.

---

# Stage 6: Show GET All Assets

## Show

```java
public List<AssetResponse> getAllAssets() {
    return assetRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
}
```

## Explain

```text
assetRepository.findAll() gets all documents from MongoDB.
The stream converts each Asset model into an AssetResponse DTO.
```

Reinforce:

```text
Model is for database.
DTO is for API response.
```

---

# Stage 7: Show GET Asset by ID

## Show

```java
public AssetResponse getAssetById(String id) {
    Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset " + id + " was not found"));

    return toResponse(asset);
}
```

## Explain

```text
findById(id) returns Optional<Asset>.
If the asset exists, we map it to a response.
If not, we throw ResourceNotFoundException.
```

Then test a real ID and a missing ID.

---

# Stage 8: Show POST Create Asset

## Show

```java
public AssetResponse createAsset(CreateAssetRequest request) {
    Asset asset = new Asset(
            request.getAssetTag().trim(),
            request.getName().trim(),
            request.getCategory().trim(),
            request.getSerialNumber().trim(),
            "AVAILABLE",
            request.getLocation().trim(),
            null
    );

    Asset savedAsset = assetRepository.save(asset);
    return toResponse(savedAsset);
}
```

## Explain

```text
CreateAssetRequest comes from the client.
We create an Asset model.
assetRepository.save(asset) stores it in MongoDB.
MongoDB generates the ID.
We return AssetResponse to the client.
```

Important:

```text
The API client does not send status or assignedTo yet.
The backend decides default values.
```

---

# Stage 9: Show the Mapper Method

## Show

```java
private AssetResponse toResponse(Asset asset) {
    return new AssetResponse(
            asset.getId(),
            asset.getAssetTag(),
            asset.getName(),
            asset.getCategory(),
            asset.getSerialNumber(),
            asset.getStatus(),
            asset.getLocation(),
            asset.getAssignedTo()
    );
}
```

## Explain

This method converts database objects into API response objects.

This is not exciting, but it is important because it keeps database structure separate from API structure.

---

# Stage 10: Show the Seeder

## Show

`config/AssetDataSeeder.java`

```java
@Bean
CommandLineRunner seedAssets(AssetRepository assetRepository) {
    return args -> {
        if (assetRepository.count() > 0) {
            return;
        }

        assetRepository.save(new Asset(...));
    };
}
```

## Explain

This runs once when the application starts.

It adds sample data only if the database is empty.

This gives us data to test without manually inserting everything.

Make it clear:

```text
This is just for learning and demo data.
Production applications handle seeding differently.
```

---

# When to Give Student Exercises

## After Stage 4

Give Exercise 2: Ticket Model and Repository.

## After Stage 7

Give Exercise 3: Convert Ticket Read API to MongoDB.

## After Stage 8

Give Exercise 4: Save Tickets to MongoDB.

---

# Final Proof to Show

1. Run `GET /api/assets`.
2. Run `POST /api/assets`.
3. Check MongoDB Compass.
4. Stop Spring Boot.
5. Start Spring Boot again.
6. Run `GET /api/assets` again.
7. Show that the new asset is still there.
