package com.example.rentalrequest.controller;

import com.example.rentalrequest.dto.RentalRequestDTO;
import com.example.rentalrequest.model.RentalRequest;
import com.example.rentalrequest.service.RentalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalRequestController {

    @Autowired
    private RentalRequestService service;

    // ADMIN ONLY - Get all rental requests
    @GetMapping
    public List<RentalRequest> getAllRequests() {
        return service.getAllRequests();
    }

    // PUBLIC - Anyone can post rental request (Users)
    @PostMapping
    public ResponseEntity<?> createRentalRequest(@RequestBody RentalRequestDTO dto) {
        RentalRequest rental = new RentalRequest();
        rental.setCustomerName(dto.getName());
        rental.setPickupLocation(dto.getPickupLocation());
        rental.setDropLocation(dto.getDropLocation());
        rental.setPickupDateTime(dto.getPickupDateTime());
        rental.setDropDateTime(dto.getDropDateTime());
        rental.setRentalDays(
                (int) ChronoUnit.DAYS.between(dto.getPickupDateTime(), dto.getDropDateTime())
        );

        RentalRequest savedRequest = service.saveRequest(rental);
        return ResponseEntity.status(201).body(Map.of(
                "message", "Rental request submitted successfully! Admin will review your request.",
                "requestId", savedRequest.getId(),
                "customerName", savedRequest.getCustomerName(),
                "status", "Pending Admin Approval"
        ));
    }

    // ADMIN ONLY - Update rental request
    @PutMapping("/{id}")
    public RentalRequest updateRequest(@PathVariable Long id, @RequestBody RentalRequest updatedRequest) {
        RentalRequest existing = service.getRequestById(id);
        existing.setCustomerName(updatedRequest.getCustomerName());
        existing.setCarModel(updatedRequest.getCarModel());
        existing.setRentalDays(updatedRequest.getRentalDays());
        existing.setPickupLocation(updatedRequest.getPickupLocation());
        existing.setDropLocation(updatedRequest.getDropLocation());
        existing.setPickupDateTime(updatedRequest.getPickupDateTime());
        existing.setDropDateTime(updatedRequest.getDropDateTime());
        return service.saveRequest(existing);
    }

    // ADMIN ONLY - Delete rental request
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id) {
        service.deleteRequestById(id);
        return ResponseEntity.ok(Map.of("message", "Rental request deleted successfully"));
    }

    // ADMIN ONLY - Get specific rental request
    @GetMapping("/{id}")
    public RentalRequest getRequestById(@PathVariable Long id) {
        return service.getRequestById(id);
    }
}










