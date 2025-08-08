package com.example.rentalrequest.controller;

import com.example.rentalrequest.dto.CarDTO;
import com.example.rentalrequest.model.Car;
import com.example.rentalrequest.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCars() {
        List<CarDTO> cars = carService.getAllCarsWithAccessories();

        Map<String, Object> response = Map.of(
                "data", cars,
                "totalData", cars.size(),
                "status", "success"
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/simple")
    public ResponseEntity<List<Map<String, Object>>> getAllCarsSimple() {
        List<Map<String, Object>> cars = carService.getAllCarsSimple();
        return ResponseEntity.ok(cars);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id) {
        CarDTO car = carService.getCarById(id);
        return ResponseEntity.ok(car);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCar(@RequestBody Car car) {
        CarDTO createdCar = carService.createCar(car);

        Map<String, Object> response = Map.of(
                "message", "Car added successfully",
                "carId", createdCar.getId(),
                "brand", createdCar.getBrand(),
                "model", createdCar.getModel()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCar(@PathVariable Long id, @RequestBody Car car) {
        CarDTO updatedCar = carService.updateCar(id, car);

        Map<String, Object> response = Map.of(
                "message", "Car updated successfully",
                "carId", updatedCar.getId()
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok(Map.of("message", "Car deleted successfully"));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAvailableCars() {
        List<Car> availableCars = carService.findAvailableCars();
        return ResponseEntity.ok(availableCars);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Car>> getCarsByLocation(@PathVariable String location) {
        List<Car> cars = carService.findCarsByLocation(location);
        return ResponseEntity.ok(cars);
    }
}




















