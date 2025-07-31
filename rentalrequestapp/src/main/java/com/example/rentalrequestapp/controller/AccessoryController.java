package com.example.rentalrequest.controller;

import com.example.rentalrequest.dto.AccessoryDTO;
import com.example.rentalrequest.model.Accessory;
import com.example.rentalrequest.model.Car;
import com.example.rentalrequest.repository.AccessoryRepository;
import com.example.rentalrequest.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accessories")
@RequiredArgsConstructor
public class AccessoryController {

    private final AccessoryRepository accessoryRepository;
    private final CarRepository carRepository;

    // ADMIN ONLY - GET all accessories
    @GetMapping
    public List<Accessory> getAllAccessories() {
        return accessoryRepository.findAll();
    }

    // ADMIN ONLY - POST new accessory with carId
    @PostMapping
    public ResponseEntity<?> createAccessory(@RequestBody AccessoryDTO accessoryDTO) {
        Car car = carRepository.findById(accessoryDTO.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found with ID: " + accessoryDTO.getCarId()));

        Accessory accessory = new Accessory();
        accessory.setName(accessoryDTO.getName());
        accessory.setDescription(accessoryDTO.getDescription());
        accessory.setAvailable(accessoryDTO.getAvailable());
        accessory.setCar(car);

        Accessory saved = accessoryRepository.save(accessory);
        return ResponseEntity.status(201).body(Map.of(
                "message", "Accessory added successfully",
                "accessoryId", saved.getId(),
                "name", saved.getName()
        ));
    }

    // ADMIN ONLY - PUT - update accessory
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccessory(@PathVariable Long id, @RequestBody AccessoryDTO updatedDTO) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accessory not found with ID: " + id));

        accessory.setName(updatedDTO.getName());
        accessory.setDescription(updatedDTO.getDescription());
        if (updatedDTO.getAvailable() != null) {
            accessory.setAvailable(updatedDTO.getAvailable());
        }

        Accessory saved = accessoryRepository.save(accessory);
        return ResponseEntity.ok(Map.of("message", "Accessory updated successfully", "accessoryId", saved.getId()));
    }

    // ADMIN ONLY - DELETE accessory by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccessory(@PathVariable Long id) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accessory not found with ID: " + id));
        accessoryRepository.delete(accessory);
        return ResponseEntity.ok(Map.of("message", "Accessory deleted successfully"));
    }

    // ADMIN ONLY - GET specific accessory
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccessoryById(@PathVariable Long id) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accessory not found with ID: " + id));
        return ResponseEntity.ok(accessory);
    }
}








