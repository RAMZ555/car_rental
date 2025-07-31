package com.example.rentalrequest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    // Anyone can access this (no login needed)
    @GetMapping("/info")
    public ResponseEntity<?> getPublicInfo() {
        return ResponseEntity.ok(Map.of(
                "message", "Welcome to Car Rental System!",
                "userAccess", "You can view cars and submit rental requests without login",
                "adminAccess", "Admin login required for managing rentals, cars, and accessories",
                "currentTime", LocalDateTime.now()
        ));
    }

    // Test endpoint for users
    @GetMapping("/test")
    public ResponseEntity<?> testUserAccess() {
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "This is a public endpoint - no authentication required",
                "availableActions", Map.of(
                        "viewCars", "GET /api/cars",
                        "submitRentalRequest", "POST /api/rentals",
                        "publicInfo", "GET /api/public/info"
                )
        ));
    }
}
