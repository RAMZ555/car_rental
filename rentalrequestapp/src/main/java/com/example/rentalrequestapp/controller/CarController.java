package com.example.rentalrequest.controller;

import com.example.rentalrequest.model.Car;
import com.example.rentalrequest.model.Accessory;
import com.example.rentalrequest.dto.CarDTO;
import com.example.rentalrequest.repository.CarRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarRepository carRepository;

    // PUBLIC - Users can view available cars (no authentication needed)
    @GetMapping
    @Transactional
    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream().map(car -> {
            CarDTO dto = new CarDTO();
            BeanUtils.copyProperties(car, dto);

            Set<String> accessoryNames = car.getAccessories().stream()
                    .map(Accessory::getName)
                    .collect(Collectors.toSet());
            dto.setAccessories(accessoryNames);

            return dto;
        }).collect(Collectors.toList());
    }

    // ADMIN ONLY - Create new car
    @PostMapping
    public ResponseEntity<?> createCar(@RequestBody Car car) {
        for (Accessory a : car.getAccessories()) {
            a.setCar(car); // Set back reference
        }
        Car savedCar = carRepository.save(car);
        return ResponseEntity.status(201).body(Map.of(
                "message", "Car added successfully",
                "carId", savedCar.getId(),
                "brand", savedCar.getBrand(),
                "model", savedCar.getModel()
        ));
    }

    // ADMIN ONLY - Update car
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestBody Car updatedCar) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

        car.setBrand(updatedCar.getBrand());
        car.setModel(updatedCar.getModel());
        car.setFuel(updatedCar.getFuel());
        car.setCarType(updatedCar.getCarType());
        car.setPlateNumber(updatedCar.getPlateNumber());
        car.setMainLocation(updatedCar.getMainLocation());
        car.setColor(updatedCar.getColor());
        car.setTransmission(updatedCar.getTransmission());
        car.setYear(updatedCar.getYear());
        car.setPassengers(updatedCar.getPassengers());
        car.setNoOfAirBags(updatedCar.getNoOfAirBags());
        car.setDescription(updatedCar.getDescription());
        car.setDailyPrice(updatedCar.getDailyPrice());
        car.setWeeklyPrice(updatedCar.getWeeklyPrice());
        car.setMonthlyPrice(updatedCar.getMonthlyPrice());
        car.setAvailable(updatedCar.isAvailable());

        // Update accessories
        car.getAccessories().clear();
        for (Accessory a : updatedCar.getAccessories()) {
            a.setCar(car);
            car.getAccessories().add(a);
        }

        Car saved = carRepository.save(car);
        return ResponseEntity.ok(Map.of("message", "Car updated successfully", "carId", saved.getId()));
    }

    // ADMIN ONLY - Delete car
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Car not found with id: " + id);
        }
        carRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Car deleted successfully"));
    }

    // ADMIN ONLY - Get specific car
    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

        CarDTO dto = new CarDTO();
        BeanUtils.copyProperties(car, dto);

        Set<String> accessoryNames = car.getAccessories().stream()
                .map(Accessory::getName)
                .collect(Collectors.toSet());
        dto.setAccessories(accessoryNames);

        return ResponseEntity.ok(dto);
    }
}














