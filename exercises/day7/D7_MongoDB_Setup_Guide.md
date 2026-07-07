# Day 7 MongoDB Setup Guide

## Required Tools

Students need:

1. MongoDB Community Server
2. MongoDB Compass
3. MongoDB Shell (`mongosh`)

## Option A: Local Installation

This is the recommended classroom option.

### Windows

1. Download MongoDB Community Server for Windows.
2. Use the MSI installer.
3. Install MongoDB as a Windows Service.
4. Install MongoDB Compass.
5. Install MongoDB Shell if it is not already installed.

Verify in PowerShell:

```powershell
mongosh
```

Inside mongosh:

```javascript
db.runCommand({ ping: 1 })
```

Expected result includes:

```text
ok: 1
```

### macOS

Using Homebrew:

```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

Then verify:

```bash
mongosh
```

Inside mongosh:

```javascript
db.runCommand({ ping: 1 })
```

### Linux

Use the official MongoDB installation instructions for the specific Linux distribution.

Verify:

```bash
mongosh
```

Inside mongosh:

```javascript
db.runCommand({ ping: 1 })
```

---

## Option B: Docker Fallback

Only use this if students already have Docker installed.

```bash
docker run --name mongodb-day7 -p 27017:27017 -d mongo:latest
```

Verify:

```bash
docker ps
```

Connect using:

```text
mongodb://localhost:27017
```

---

# MongoDB Compass First-Time Activity

1. Open MongoDB Compass.
2. Connect to:

```text
mongodb://localhost:27017
```

3. Create database:

```text
asset_tracker_db
```

4. Create collection:

```text
assets
```

5. Insert this document:

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

6. View it.
7. Edit one field.
8. Delete it.

---

# Common Installation Issues

## `mongosh` command not found

The MongoDB Shell may not be installed or may not be in PATH.

Use Compass first if the database server is running, then fix the shell later.

## Compass cannot connect

Check:

1. MongoDB server is running.
2. The connection string is correct:

```text
mongodb://localhost:27017
```

3. Port 27017 is not blocked.

## Spring Boot cannot connect

Check:

1. MongoDB is running.
2. `application-local.properties` has the correct URI.
3. The app was started with the local profile.

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Windows PowerShell:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```
