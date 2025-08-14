package com.example.rentalrequest.controller;

import com.example.rentalrequest.dto.AccessoryDTO;
import com.example.rentalrequest.model.Accessory;
import com.example.rentalrequest.service.AccessoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accessories")
@RequiredArgsConstructor
public class AccessoryController {

    private final AccessoryService accessoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Accessory>> getAllAccessories() {
        List<Accessory> accessories = accessoryService.getAllAccessories();
        return ResponseEntity.ok(accessories);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Accessory> getAccessoryById(@PathVariable Long id) {
        Accessory accessory = accessoryService.getAccessoryById(id);
        return ResponseEntity.ok(accessory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAccessory(@RequestBody AccessoryDTO accessoryDTO) {
        Map<String, Object> result = accessoryService.createAccessory(accessoryDTO);

        Map<String, Object> response = Map.of(
                "message", "Accessory added successfully",
                "accessoryId", result.get("id"),
                "name", result.get("name"),
                "carId", result.get("carId"),
                "carInfo", result.get("carInfo")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAccessory(@PathVariable Long id, @RequestBody AccessoryDTO accessoryDTO) {
        Map<String, Object> result = accessoryService.updateAccessory(id, accessoryDTO);

        Map<String, Object> response = Map.of(
                "message", "Accessory updated successfully",
                "accessoryId", result.get("id"),
                "name", result.get("name")
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAccessory(@PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return ResponseEntity.ok(Map.of("message", "Accessory deleted successfully"));
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<Accessory>> getAccessoriesByCarId(@PathVariable Long carId) {
        List<Accessory> accessories = accessoryService.getAccessoriesByCarId(carId);
        return ResponseEntity.ok(accessories);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Accessory>> getAvailableAccessories() {
        List<Accessory> accessories = accessoryService.getAvailableAccessories();
        return ResponseEntity.ok(accessories);
    }
}









