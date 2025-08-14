package com.example.rentalrequest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getPublicInfo() {
        return ResponseEntity.ok(Map.of(
                "message", "Welcome to Car Rental System!",
                "userAccess", "You can view cars and submit rental requests without login",
                "adminAccess", "Admin login required for managing rentals, cars, and accessories",
                "currentTime", LocalDateTime.now(),
                "version", "1.0.0"
        ));
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testUserAccess() {
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "This is a public endpoint - no authentication required",
                "availableActions", Map.of(
                        "viewCars", "GET /api/cars",
                        "viewAvailableCars", "GET /api/cars/available",
                        "submitRentalRequest", "POST /api/rentals",
                        "publicInfo", "GET /api/public/info"
                )
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "service", "Car Rental API"
        ));
    }
}
