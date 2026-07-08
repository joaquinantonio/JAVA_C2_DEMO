# Day 8 Notes for JAVA_C2_DEMO `test/7`

This regenerated package is aligned to the current Day 7 branch style:

- Spring Boot parent: `4.1.0`
- Java: `21`
- Web dependency: `spring-boot-starter-webmvc`
- MongoDB connection property: `spring.mongodb.uri`
- MongoDB auto-index creation property: `spring.data.mongodb.auto-index-creation`
- Error properties: `spring.web.error.include-message` and `spring.web.error.include-binding-errors`

## Why not `spring.data.mongodb.uri`?

For this branch and its Spring Boot 4.1 metadata, connection settings are expected under `spring.mongodb.*`.

Use:

```properties
spring.mongodb.uri=mongodb://localhost:27017/asset_tracker_db
```

Keep this for Day 8 indexes:

```properties
spring.data.mongodb.auto-index-creation=true
```

## Files to update from Day 7 to Day 8

Copy the changed versions of these files into the Day 7 repo:

```text
src/main/java/com/example/assettracker/model/Asset.java
src/main/java/com/example/assettracker/repository/AssetRepository.java
src/main/java/com/example/assettracker/service/AssetService.java
src/main/java/com/example/assettracker/controller/AssetController.java
src/main/java/com/example/assettracker/config/AssetDataSeeder.java
src/main/resources/application.properties
requests/day08-assets-query-pagination.http
```

The remaining DTO and exception files are unchanged from Day 7.
