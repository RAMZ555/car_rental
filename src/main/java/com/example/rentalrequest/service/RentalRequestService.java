package com.example.rentalrequest.service;

import com.example.rentalrequest.dto.RentalRequestDTO;
import com.example.rentalrequest.model.RentalRequest;
import com.example.rentalrequest.model.Car;
import com.example.rentalrequest.repository.RentalRequestRepository;
import com.example.rentalrequest.repository.CarRepository;
import com.example.rentalrequest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;
    private final CarRepository carRepository;

    @Transactional(readOnly = true)
    public List<RentalRequest> getAllRequests() {
        log.debug("Fetching all rental requests");
        return rentalRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RentalRequest getRequestById(Long id) {
        log.debug("Fetching rental request with ID: {}", id);
        return rentalRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found with id: " + id));
    }

    public Map<String, Object> createRentalRequest(RentalRequestDTO dto) {
        log.info("Creating rental request for customer: {}", dto.getCustomerName());

        validateRentalRequestDTO(dto);

        RentalRequest rental = buildRentalRequestFromDTO(dto);
        RentalRequest savedRequest = rentalRequestRepository.save(rental);

        log.info("Rental request created successfully with ID: {}", savedRequest.getId());

        int rentalDays = calculateRentalDays(dto.getPickupDateTime(), dto.getDropDateTime());

        // Use HashMap for null safety
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedRequest.getId());
        response.put("customerName", savedRequest.getCustomerName());
        response.put("status", "Pending Admin Approval");
        response.put("rentalDays", rentalDays);
        response.put("totalAmount", calculateTotalAmount(savedRequest));

        return response;
    }

    public RentalRequest updateRentalRequest(Long id, RentalRequest updatedRequest) {
        log.info("Updating rental request with ID: {}", id);

        RentalRequest existingRequest = getRequestById(id);
        updateRentalRequestFields(existingRequest, updatedRequest);

        RentalRequest savedRequest = rentalRequestRepository.save(existingRequest);
        log.info("Rental request updated successfully with ID: {}", savedRequest.getId());

        return savedRequest;
    }

    public void deleteRentalRequest(Long id) {
        log.info("Deleting rental request with ID: {}", id);

        if (!rentalRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rental request not found with id: " + id);
        }

        rentalRequestRepository.deleteById(id);
        log.info("Rental request deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<RentalRequest> getRequestsByCustomerName(String customerName) {
        log.debug("Fetching rental requests for customer: {}", customerName);
        return rentalRequestRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }

    @Transactional(readOnly = true)
    public List<RentalRequest> getPendingRequests() {
        log.debug("Fetching pending rental requests");
        return rentalRequestRepository.findByStatus("PENDING");
    }

    public RentalRequest approveRequest(Long id, Long carId) {
        log.info("Approving rental request ID: {} with car ID: {}", id, carId);

        RentalRequest request = getRequestById(id);
        Car car = validateAndGetAvailableCar(carId);

        // Preserve all original customer data - only update status and car assignment
        log.debug("Preserving original data for customer: {}", request.getCustomerName());

        // Only update carModel if it's missing and car data is available
        if ((request.getCarModel() == null || request.getCarModel().trim().isEmpty())
                && car.getBrand() != null && car.getModel() != null) {
            String carModel = (car.getBrand() + " " + car.getModel()).trim();
            if (!carModel.isEmpty()) {
                request.setCarModel(carModel);
            }
        }

        // Update status
        request.setStatus("APPROVED");

        // Mark car as unavailable
        car.setAvailable(false);
        carRepository.save(car);

        RentalRequest savedRequest = rentalRequestRepository.save(request);
        log.info("Rental request {} approved successfully for customer: {}",
                id, savedRequest.getCustomerName());

        return savedRequest;
    }

    public RentalRequest rejectRequest(Long id, String reason) {
        log.info("Rejecting rental request ID: {} with reason: {}", id, reason);

        RentalRequest request = getRequestById(id);

        // Preserve all original data - only update status and rejection details
        request.setStatus("REJECTED");
        request.setRejectionReason(reason);

        RentalRequest savedRequest = rentalRequestRepository.save(request);
        log.info("Rental request {} rejected for customer: {}", id, savedRequest.getCustomerName());

        return savedRequest;
    }

    // --------------------- Private helpers ---------------------

    private void validateRentalRequestDTO(RentalRequestDTO dto) {
        if (dto.getCustomerName() == null || dto.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }

        if (dto.getPhoneNumber() == null || dto.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        // Enhanced email validation
        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (dto.getPickupDateTime() == null) {
            throw new IllegalArgumentException("Pickup date and time is required");
        }
        if (dto.getDropDateTime() == null) {
            throw new IllegalArgumentException("Drop date and time is required");
        }
        if (dto.getPickupDateTime().isAfter(dto.getDropDateTime())) {
            throw new IllegalArgumentException("Pickup date cannot be after drop date");
        }

        // Validate pickup is not in the past
        if (dto.getPickupDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Pickup date cannot be in the past");
        }
    }

    private Car validateAndGetAvailableCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));

        if (!car.isAvailable()) {
            throw new IllegalStateException("Selected car is not available");
        }

        return car;
    }

    private RentalRequest buildRentalRequestFromDTO(RentalRequestDTO dto) {
        RentalRequest rental = new RentalRequest();
        rental.setCustomerName(dto.getCustomerName().trim());
        rental.setPhoneNumber(dto.getPhoneNumber().trim());
        rental.setEmail(dto.getEmail().trim().toLowerCase()); // Normalize email
        rental.setCarModel(dto.getCarModel() != null ? dto.getCarModel().trim() : null);
        rental.setPickupDateTime(dto.getPickupDateTime());
        rental.setDropDateTime(dto.getDropDateTime());
        rental.setStatus("PENDING");
        return rental;
    }

    private void updateRentalRequestFields(RentalRequest existing, RentalRequest updated) {
        if (updated.getCustomerName() != null) {
            existing.setCustomerName(updated.getCustomerName().trim());
        }
        if (updated.getPhoneNumber() != null) {
            existing.setPhoneNumber(updated.getPhoneNumber().trim());
        }
        if (updated.getEmail() != null) {
            existing.setEmail(updated.getEmail().trim().toLowerCase());
        }
        if (updated.getCarModel() != null) {
            existing.setCarModel(updated.getCarModel().trim());
        }
        if (updated.getPickupDateTime() != null) {
            existing.setPickupDateTime(updated.getPickupDateTime());
        }
        if (updated.getDropDateTime() != null) {
            existing.setDropDateTime(updated.getDropDateTime());
        }
        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
        if (updated.getRejectionReason() != null) {
            existing.setRejectionReason(updated.getRejectionReason());
        }
    }

    private int calculateRentalDays(LocalDateTime pickup, LocalDateTime drop) {
        if (pickup == null || drop == null) return 0;
        int days = (int) ChronoUnit.DAYS.between(pickup, drop);
        return Math.max(days, 1); // Minimum 1 day
    }

    private BigDecimal calculateTotalAmount(RentalRequest request) {
        if (request.getPickupDateTime() == null || request.getDropDateTime() == null) {
            return BigDecimal.ZERO;
        }

        int days = calculateRentalDays(request.getPickupDateTime(), request.getDropDateTime());

        // TODO: Implement dynamic pricing based on car type/model
        BigDecimal dailyRate = BigDecimal.valueOf(100);
        return BigDecimal.valueOf(days).multiply(dailyRate);
    }
}






