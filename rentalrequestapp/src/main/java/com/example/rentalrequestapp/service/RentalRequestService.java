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

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
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
        log.info("Fetching all rental requests");
        return rentalRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RentalRequest getRequestById(Long id) {
        log.info("Fetching rental request with ID: {}", id);
        return rentalRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found with id: " + id));
    }

    public Map<String, Object> createRentalRequest(RentalRequestDTO dto) {
        log.info("Creating rental request for customer: {}", dto.getName());

        validateRentalRequestDTO(dto);

        RentalRequest rental = buildRentalRequestFromDTO(dto);
        RentalRequest savedRequest = rentalRequestRepository.save(rental);

        log.info("Rental request created successfully with ID: {}", savedRequest.getId());

        return Map.of(
                "id", savedRequest.getId(),
                "customerName", savedRequest.getCustomerName(),
                "status", "Pending Admin Approval",
                "rentalDays", savedRequest.getRentalDays(),
                "totalAmount", calculateTotalAmount(savedRequest)
        );
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
        log.info("Fetching rental requests for customer: {}", customerName);
        return rentalRequestRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }

    @Transactional(readOnly = true)
    public List<RentalRequest> getPendingRequests() {
        log.info("Fetching pending rental requests");
        return rentalRequestRepository.findByStatus("PENDING");
    }

    public RentalRequest approveRequest(Long id, Long carId) {
        log.info("Approving rental request ID: {} with car ID: {}", id, carId);

        RentalRequest request = getRequestById(id);
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));

        if (!car.isAvailable()) {
            throw new IllegalStateException("Selected car is not available");
        }

        request.setCarModel(car.getBrand() + " " + car.getModel());
        request.setStatus("APPROVED");
        car.setAvailable(false); // Mark car as unavailable

        carRepository.save(car);
        return rentalRequestRepository.save(request);
    }

    public RentalRequest rejectRequest(Long id, String reason) {
        log.info("Rejecting rental request ID: {} with reason: {}", id, reason);

        RentalRequest request = getRequestById(id);
        request.setStatus("REJECTED");
        request.setRejectionReason(reason);

        return rentalRequestRepository.save(request);
    }

    // Private helper methods
    private void validateRentalRequestDTO(RentalRequestDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
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
    }

    private RentalRequest buildRentalRequestFromDTO(RentalRequestDTO dto) {
        RentalRequest rental = new RentalRequest();
        rental.setCustomerName(dto.getName());
        rental.setPickupLocation(dto.getPickupLocation());
        rental.setDropLocation(dto.getDropLocation());
        rental.setPickupDateTime(dto.getPickupDateTime());
        rental.setDropDateTime(dto.getDropDateTime());
        rental.setRentalDays((int) ChronoUnit.DAYS.between(dto.getPickupDateTime(), dto.getDropDateTime()));
        rental.setStatus("PENDING");
        return rental;
    }

    private void updateRentalRequestFields(RentalRequest existing, RentalRequest updated) {
        existing.setCustomerName(updated.getCustomerName());
        existing.setCarModel(updated.getCarModel());
        existing.setRentalDays(updated.getRentalDays());
        existing.setPickupLocation(updated.getPickupLocation());
        existing.setDropLocation(updated.getDropLocation());
        existing.setPickupDateTime(updated.getPickupDateTime());
        existing.setDropDateTime(updated.getDropDateTime());

        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
    }

    private BigDecimal calculateTotalAmount(RentalRequest request) {
        // This is a placeholder - implement actual calculation based on car pricing
        return BigDecimal.valueOf(request.getRentalDays() * 100); // $100 per day placeholder
    }
}





