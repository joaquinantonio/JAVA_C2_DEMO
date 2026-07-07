# Day 7 Exact Trainer Plan: MongoDB Foundations & Spring Boot Integration

## Trainer Demo

Asset Tracker API

## Student Exercise

Support Desk Ticket API

## Main Teaching Goal

Move students from this Day 6 architecture:

```text
Controller -> Service -> Java List
```

To this Day 7 architecture:

```text
Controller -> Service -> Repository -> MongoDB
```

The REST endpoints should feel familiar. The main change is the storage layer.

---

## Day 7 Learning Outcomes

By the end of the day, students should be able to:

1. Explain database, collection, document, field, and `_id`.
2. Explain how MongoDB differs from relational databases at a beginner level.
3. Start MongoDB locally and connect using MongoDB Compass.
4. Insert and inspect a document manually in Compass.
5. Add Spring Data MongoDB to a Spring Boot project.
6. Create a MongoDB model class using `@Document` and `@Id`.
7. Create a repository using `MongoRepository`.
8. Replace in-memory list access with repository access.
9. Prove that data remains after restarting Spring Boot.

---

# 9:00 - 9:30: Recap Day 6 and Introduce the Problem

## What to ask students

Ask:

```text
What happens to our Asset or Ticket data when we stop and restart the Spring Boot server?
```

Expected answer:

```text
The data disappears because it is only stored in memory.
```

## What to explain

Day 6 was about REST API structure:

```text
Controller -> Service -> DTO
```

Day 7 adds persistence:

```text
Controller -> Service -> Repository -> MongoDB
```

## What to show

Show the old Day 6 idea:

```java
private final List<AssetResponse> assets = new ArrayList<>();
```

Then say:

```text
Today this list will be replaced with MongoDB.
```

---

# 9:30 - 10:15: MongoDB Theory for First-Time Users

## Key terms

| Relational Database | MongoDB |
|---|---|
| Database | Database |
| Table | Collection |
| Row | Document |
| Column | Field |
| Primary Key | `_id` |

## What to show

Show this document:

```json
{
  "_id": "generated-by-mongodb",
  "assetTag": "LAP-2026-001",
  "name": "Dell Latitude 5440",
  "category": "Laptop",
  "serialNumber": "SN-LAP-001",
  "status": "AVAILABLE",
  "location": "HQ Level 3",
  "assignedTo": null
}
```

## Explain simply

A MongoDB document looks like JSON.

Spring Boot will convert Java objects into MongoDB documents.

---

# 10:15 - 10:30: Installation Options Briefing

## Default option

MongoDB Community Server + MongoDB Compass + mongosh.

## Fallback option

Docker container, only for students already comfortable with Docker.

## Tell students

```text
Compass is the GUI.
mongosh is the command-line shell.
Spring Boot connects using a MongoDB URI.
```

---

# 10:45 - 11:45: Installation and Verification

## What students should do

1. Install or start MongoDB.
2. Open MongoDB Compass.
3. Connect to:

```text
mongodb://localhost:27017
```

4. Open `mongosh` and run:

```javascript
db.runCommand({ ping: 1 })
```

Expected result should include:

```text
ok: 1
```

## Trainer note

If installation takes longer than expected, keep the stronger students moving with Compass and help weaker students one by one.

---

# 11:45 - 12:15: Compass First-Time Activity

## Activity

In MongoDB Compass:

1. Create database: `asset_tracker_db`
2. Create collection: `assets`
3. Insert one document manually.
4. View the document.
5. Edit one field.
6. Delete the test document.

## Sample document

```json
{
  "assetTag": "LAP-TEST-001",
  "name": "Test Laptop",
  "category": "Laptop",
  "serialNumber": "TEST-SN-001",
  "status": "AVAILABLE",
  "location": "Training Room",
  "assignedTo": null
}
```

---

# 12:15 - 1:00: Spring Boot MongoDB Setup

## What to show

Add dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

Add local config:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/asset_tracker_db
```

## Explain

```text
localhost = database runs on this computer
27017 = MongoDB default port
asset_tracker_db = database name
```

## Test

Start the app. If MongoDB is not running, Spring Boot should fail when trying to access MongoDB.

---

# 2:00 - 2:40: Demo Asset Model and Repository

## Create

```text
model/Asset.java
repository/AssetRepository.java
```

## Explain

```text
Model = database document shape
Repository = database access layer
DTO = API input/output shape
```

## Important comparison

```text
Asset model goes to MongoDB.
AssetResponse goes to the client.
CreateAssetRequest comes from the client.
```

---

# 2:40 - 3:30: Convert Asset Service to MongoDB

## What to show

Replace manual list logic with repository logic.

Day 6:

```java
assets.stream()
```

Day 7:

```java
assetRepository.findAll()
assetRepository.findById(id)
assetRepository.save(asset)
```

## Test using `.http`

Run:

```http
GET http://localhost:8080/api/assets
POST http://localhost:8080/api/assets
```

Then check Compass to see the saved document.

---

# 3:45 - 4:30: Student Exercises

Students now convert the Support Desk Ticket API.

They should create:

```text
model/Ticket.java
repository/TicketRepository.java
```

Then update:

```text
GET /api/tickets
GET /api/tickets/{id}
POST /api/tickets
```

---

# 4:30 - 5:00: Persistence Proof and Review

## Persistence proof

Students must:

1. POST a new ticket.
2. GET all tickets and confirm it appears.
3. Stop Spring Boot.
4. Start Spring Boot again.
5. GET all tickets again.
6. Confirm the ticket is still there.

## Review questions

1. What is a MongoDB document?
2. What is a collection?
3. What does `@Document` do?
4. What does `@Id` do?
5. What does `MongoRepository` provide?
6. Why do we still keep DTOs?
7. What changed from Day 6 to Day 7?
