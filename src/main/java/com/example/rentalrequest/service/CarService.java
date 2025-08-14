// ==================== UPDATED CAR SERVICE ====================
package com.example.rentalrequest.service;

import com.example.rentalrequest.dto.CarDTO;
import com.example.rentalrequest.model.Car;
import com.example.rentalrequest.model.Accessory;
import com.example.rentalrequest.repository.CarRepository;
import com.example.rentalrequest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CarService {

    private final CarRepository carRepository;

    @Transactional(readOnly = true)
    public List<CarDTO> getAllCarsWithAccessories() {
        log.info("Fetching all cars with accessories");

        List<Car> cars = carRepository.findAll();
        log.info("Found {} cars in database", cars.size());

        return cars.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllCarsSimple() {
        log.info("Fetching all cars - simple format");

        List<Car> cars = carRepository.findAll();
        log.info("Found {} cars for simple endpoint", cars.size());

        return cars.stream()
                .map(this::convertToSimpleMap)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CarDTO getCarById(Long id) {
        log.info("Fetching car with ID: {}", id);

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + id));

        return convertToDTO(car);
    }

    public CarDTO createCar(Car car) {
        log.info("Creating new car: {} {}", car.getBrand(), car.getModel());

        validateCarData(car);

        // Set bidirectional relationship for accessories
        if (car.getAccessories() != null) {
            car.getAccessories().forEach(accessory -> accessory.setCar(car));
        }

        Car savedCar = carRepository.save(car);
        log.info("Car created successfully with ID: {}", savedCar.getId());

        return convertToDTO(savedCar);
    }

    public CarDTO updateCar(Long id, Car updatedCarData) {
        log.info("Updating car with ID: {}", id);

        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + id));

        validateCarData(updatedCarData);
        updateCarFields(existingCar, updatedCarData);

        Car savedCar = carRepository.save(existingCar);
        log.info("Car updated successfully with ID: {}", savedCar.getId());

        return convertToDTO(savedCar);
    }

    public void deleteCar(Long id) {
        log.info("Deleting car with ID: {}", id);

        if (!carRepository.existsById(id)) {
            throw new ResourceNotFoundException("Car not found with id: " + id);
        }

        carRepository.deleteById(id);
        log.info("Car deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return carRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<Car> findAvailableCars() {
        log.info("Fetching available cars");
        return carRepository.findByAvailableTrue();
    }

    @Transactional(readOnly = true)
    public List<Car> findCarsByLocation(String location) {
        log.info("Fetching cars by location: {}", location);
        return carRepository.findByMainLocationContainingIgnoreCase(location);
    }

    // Private helper methods
    private CarDTO convertToDTO(Car car) {
        try {
            CarDTO dto = new CarDTO();
            BeanUtils.copyProperties(car, dto);

            if (car.getAccessories() != null) {
                Set<String> accessoryNames = car.getAccessories().stream()
                        .map(Accessory::getName)
                        .collect(Collectors.toSet());
                dto.setAccessories(accessoryNames);
            } else {
                dto.setAccessories(new HashSet<>());
            }

            return dto;
        } catch (Exception e) {
            log.warn("Error converting car {} to DTO: {}", car.getId(), e.getMessage());
            CarDTO dto = new CarDTO();
            BeanUtils.copyProperties(car, dto);
            dto.setAccessories(new HashSet<>());
            return dto;
        }
    }

    private Map<String, Object> convertToSimpleMap(Car car) {
        Map<String, Object> carMap = new HashMap<>();
        carMap.put("id", car.getId());
        carMap.put("brand", car.getBrand());
        carMap.put("model", car.getModel());
        carMap.put("fuel", car.getFuel());
        carMap.put("carType", car.getCarType());
        carMap.put("plateNumber", car.getPlateNumber());
        carMap.put("mainLocation", car.getMainLocation());
        carMap.put("color", car.getColor());
        carMap.put("transmission", car.getTransmission());
        carMap.put("year", car.getYear());
        carMap.put("passengers", car.getPassengers());
        carMap.put("noOfAirBags", car.getNoOfAirBags());
        carMap.put("description", car.getDescription());
        carMap.put("dailyPrice", car.getDailyPrice());
        carMap.put("weeklyPrice", car.getWeeklyPrice());
        carMap.put("monthlyPrice", car.getMonthlyPrice());
        carMap.put("available", car.isAvailable());
        return carMap;
    }

    private void validateCarData(Car car) {
        if (car.getBrand() == null || car.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("Car brand cannot be empty");
        }
        if (car.getModel() == null || car.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Car model cannot be empty");
        }
        if (car.getDailyPrice() != null && car.getDailyPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Daily price cannot be negative");
        }
    }

    private void updateCarFields(Car existingCar, Car updatedData) {
        existingCar.setBrand(updatedData.getBrand());
        existingCar.setModel(updatedData.getModel());
        existingCar.setFuel(updatedData.getFuel());
        existingCar.setCarType(updatedData.getCarType());
        existingCar.setPlateNumber(updatedData.getPlateNumber());
        existingCar.setMainLocation(updatedData.getMainLocation());
        existingCar.setColor(updatedData.getColor());
        existingCar.setTransmission(updatedData.getTransmission());
        existingCar.setYear(updatedData.getYear());
        existingCar.setPassengers(updatedData.getPassengers());
        existingCar.setNoOfAirBags(updatedData.getNoOfAirBags());
        existingCar.setDescription(updatedData.getDescription());
        existingCar.setDailyPrice(updatedData.getDailyPrice());
        existingCar.setWeeklyPrice(updatedData.getWeeklyPrice());
        existingCar.setMonthlyPrice(updatedData.getMonthlyPrice());
        existingCar.setAvailable(updatedData.isAvailable());

        // Handle accessories update
        if (updatedData.getAccessories() != null) {
            existingCar.getAccessories().clear();
            for (Accessory accessory : updatedData.getAccessories()) {
                accessory.setCar(existingCar);
                existingCar.getAccessories().add(accessory);
            }
        }
    }
}



