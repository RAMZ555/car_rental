package com.example.rentalrequestapp.controller;

import com.example.rentalrequestapp.model.RentalRequest;
import com.example.rentalrequestapp.service.RentalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalRequestController {

    @Autowired
    private RentalRequestService service;

    @GetMapping
    public List<RentalRequest> getAllRequests() {
        return service.getAllRequests();
    }

    @PostMapping
    public RentalRequest createRequest(@RequestBody RentalRequest request) {
        return service.saveRequest(request);
    }

    @PutMapping("/{id}")
    public RentalRequest updateRequest(@PathVariable Long id, @RequestBody RentalRequest updatedRequest) {
        RentalRequest existing = service.getRequestById(id);
        existing.setCustomerName(updatedRequest.getCustomerName());
        existing.setCarModel(updatedRequest.getCarModel());
        existing.setRentalDays(updatedRequest.getRentalDays());
        return service.saveRequest(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteRequest(@PathVariable Long id) {
        service.deleteRequestById(id);
    }
}



