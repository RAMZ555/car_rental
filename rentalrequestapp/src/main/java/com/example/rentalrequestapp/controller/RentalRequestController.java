package com.example.rentalrequest.controller;

import com.example.rentalrequest.dto.RentalRequestDTO;
import com.example.rentalrequest.model.RentalRequest;
import com.example.rentalrequest.service.RentalRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalRequestController {

    private final RentalRequestService rentalRequestService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RentalRequest>> getAllRequests() {
        List<RentalRequest> requests = rentalRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RentalRequest> getRequestById(@PathVariable Long id) {
        RentalRequest request = rentalRequestService.getRequestById(id);
        return ResponseEntity.ok(request);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRentalRequest(@RequestBody RentalRequestDTO dto) {
        Map<String, Object> result = rentalRequestService.createRentalRequest(dto);

        Map<String, Object> response = Map.of(
                "message", "Rental request submitted successfully! Admin will review your request.",
                "requestId", result.get("id"),
                "customerName", result.get("customerName"),
                "status", result.get("status"),
                "rentalDays", result.get("rentalDays")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRequest(@PathVariable Long id, @RequestBody RentalRequest updatedRequest) {
        RentalRequest updated = rentalRequestService.updateRentalRequest(id, updatedRequest);

        Map<String, Object> response = Map.of(
                "message", "Rental request updated successfully",
                "requestId", updated.getId(),
                "customerName", updated.getCustomerName(),
                "status", updated.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRequest(@PathVariable Long id) {
        rentalRequestService.deleteRentalRequest(id);
        return ResponseEntity.ok(Map.of("message", "Rental request deleted successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<List<RentalRequest>> getPendingRequests() {
        List<RentalRequest> pendingRequests = rentalRequestService.getPendingRequests();
        return ResponseEntity.ok(pendingRequests);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveRequest(@PathVariable Long id, @RequestParam Long carId) {
        RentalRequest approvedRequest = rentalRequestService.approveRequest(id, carId);

        Map<String, Object> response = Map.of(
                "message", "Rental request approved successfully",
                "requestId", approvedRequest.getId(),
                "customerName", approvedRequest.getCustomerName(),
                "carModel", approvedRequest.getCarModel(),
                "status", approvedRequest.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectRequest(@PathVariable Long id, @RequestParam String reason) {
        RentalRequest rejectedRequest = rentalRequestService.rejectRequest(id, reason);

        Map<String, Object> response = Map.of(
                "message", "Rental request rejected",
                "requestId", rejectedRequest.getId(),
                "customerName", rejectedRequest.getCustomerName(),
                "status", rejectedRequest.getStatus(),
                "reason", reason
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<RentalRequest>> getRequestsByCustomer(@PathVariable String customerName) {
        List<RentalRequest> requests = rentalRequestService.getRequestsByCustomerName(customerName);
        return ResponseEntity.ok(requests);
    }
}












