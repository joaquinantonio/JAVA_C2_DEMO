package com.example.assettracker.controller;

import java.time.Instant;
import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReadinessController {

    private final MongoTemplate mongoTemplate;

    public ReadinessController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/readiness")
    public ResponseEntity<Map<String, Object>> readiness() {
        try {
            Document result = mongoTemplate.executeCommand("{ ping: 1 }");
            Object okValue = result.get("ok");
            boolean mongoReady = okValue instanceof Number number && number.doubleValue() == 1.0;

            if (!mongoReady) {
                return ResponseEntity.status(503).body(Map.of(
                        "status", "NOT_READY",
                        "database", "MongoDB ping did not return ok=1",
                        "checkedAt", Instant.now().toString()
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "READY",
                    "database", "MongoDB connection is ready",
                    "checkedAt", Instant.now().toString()
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(503).body(Map.of(
                    "status", "NOT_READY",
                    "database", "MongoDB connection failed",
                    "error", ex.getClass().getSimpleName(),
                    "checkedAt", Instant.now().toString()
            ));
        }
    }
}
