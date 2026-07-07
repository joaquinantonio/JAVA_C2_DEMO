# Day 7 Final Working Demo Checklist

Use this checklist before class and during the final demo.

## Before Class

- [ ] MongoDB is running locally.
- [ ] MongoDB Compass can connect to `mongodb://localhost:27017`.
- [ ] `asset-tracker-api-day07` opens in the IDE.
- [ ] `application-local.properties` contains:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/asset_tracker_db
```

- [ ] The app starts with:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Demo Proof

### 1. Health check

```http
GET http://localhost:8080/api/health
```

Expected:

```text
200 OK
```

### 2. Get all assets

```http
GET http://localhost:8080/api/assets
```

Expected:

```text
200 OK
```

The response should include seeded asset data if the database was empty.

### 3. Create asset

```http
POST http://localhost:8080/api/assets
Content-Type: application/json

{
  "assetTag": "TAB-2026-001",
  "name": "Samsung Galaxy Tab",
  "category": "Tablet",
  "serialNumber": "SN-TAB-001",
  "location": "HQ Level 4"
}
```

Expected:

```text
201 Created
```

### 4. Check MongoDB Compass

Open:

```text
asset_tracker_db -> assets
```

Confirm the new asset document exists.

### 5. Restart proof

1. Stop Spring Boot.
2. Start Spring Boot again.
3. Run:

```http
GET http://localhost:8080/api/assets
```

The created asset should still be there.

## Key Message

Day 6 used memory.

Day 7 uses MongoDB.

The endpoint behaviour is similar, but the data is now persistent.
