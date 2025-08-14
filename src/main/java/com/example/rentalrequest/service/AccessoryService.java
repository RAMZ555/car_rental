package com.example.rentalrequest.service;

import com.example.rentalrequest.dto.AccessoryDTO;
import com.example.rentalrequest.model.Accessory;
import com.example.rentalrequest.model.Car;
import com.example.rentalrequest.repository.AccessoryRepository;
import com.example.rentalrequest.repository.CarRepository;
import com.example.rentalrequest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final CarRepository carRepository;

    @Transactional(readOnly = true)
    public List<Accessory> getAllAccessories() {
        log.info("Fetching all accessories");
        return accessoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Accessory getAccessoryById(Long id) {
        log.info("Fetching accessory with ID: {}", id);
        return accessoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accessory not found with ID: " + id));
    }

    public Map<String, Object> createAccessory(AccessoryDTO accessoryDTO) {
        log.info("Creating accessory: {}", accessoryDTO.getName());

        validateAccessoryDTO(accessoryDTO);

        Car car = carRepository.findById(accessoryDTO.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with ID: " + accessoryDTO.getCarId()));

        log.info("Found car: {} {}", car.getBrand(), car.getModel());

        Accessory accessory = buildAccessoryFromDTO(accessoryDTO, car);
        Accessory savedAccessory = accessoryRepository.save(accessory);

        log.info("Accessory created successfully with ID: {}", savedAccessory.getId());

        return Map.of(
                "id", savedAccessory.getId(),
                "name", savedAccessory.getName(),
                "carId", car.getId(),
                "carInfo", car.getBrand() + " " + car.getModel(),
                "available", savedAccessory.isAvailable()
        );
    }

    public Map<String, Object> updateAccessory(Long id, AccessoryDTO updatedDTO) {
        log.info("Updating accessory ID: {}", id);

        validateAccessoryDTO(updatedDTO);

        Accessory existingAccessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accessory not found with ID: " + id));

        updateAccessoryFields(existingAccessory, updatedDTO);
        Accessory savedAccessory = accessoryRepository.save(existingAccessory);

        log.info("Accessory updated successfully with ID: {}", savedAccessory.getId());

        return Map.of(
                "id", savedAccessory.getId(),
                "name", savedAccessory.getName(),
                "carId", savedAccessory.getCar().getId(),
                "available", savedAccessory.isAvailable()
        );
    }

    public void deleteAccessory(Long id) {
        log.info("Deleting accessory ID: {}", id);

        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accessory not found with ID: " + id));

        accessoryRepository.delete(accessory);
        log.info("Accessory deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<Accessory> getAccessoriesByCarId(Long carId) {
        log.info("Fetching accessories for car ID: {}", carId);
        return accessoryRepository.findByCarId(carId);
    }

    @Transactional(readOnly = true)
    public List<Accessory> getAvailableAccessories() {
        log.info("Fetching available accessories");
        return accessoryRepository.findByAvailableTrue();
    }

    // Private helper methods
    private void validateAccessoryDTO(AccessoryDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Accessory name cannot be empty");
        }
        if (dto.getCarId() == null) {
            throw new IllegalArgumentException("Car ID is required");
        }
    }

    private Accessory buildAccessoryFromDTO(AccessoryDTO dto, Car car) {
        Accessory accessory = new Accessory();
        accessory.setName(dto.getName());
        accessory.setDescription(dto.getDescription());
        accessory.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : true);
        accessory.setCar(car);
        return accessory;
    }

    private void updateAccessoryFields(Accessory accessory, AccessoryDTO updatedDTO) {
        accessory.setName(updatedDTO.getName());
        accessory.setDescription(updatedDTO.getDescription());

        if (updatedDTO.getAvailable() != null) {
            accessory.setAvailable(updatedDTO.getAvailable());
        }

        // Update car relationship if needed
        if (updatedDTO.getCarId() != null && !updatedDTO.getCarId().equals(accessory.getCar().getId())) {
            Car newCar = carRepository.findById(updatedDTO.getCarId())
                    .orElseThrow(() -> new ResourceNotFoundException("Car not found with ID: " + updatedDTO.getCarId()));
            accessory.setCar(newCar);
        }
    }
}





