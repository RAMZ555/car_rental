package com.example.rentalrequest.repository;

import com.example.rentalrequest.model.RentalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {
    List<RentalRequest> findByCustomerNameContainingIgnoreCase(String customerName);
    List<RentalRequest> findByStatus(String status);
    List<RentalRequest> findByCarModelContainingIgnoreCase(String carModel);
}








