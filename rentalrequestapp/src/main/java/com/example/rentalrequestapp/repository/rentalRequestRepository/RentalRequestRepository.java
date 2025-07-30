package com.example.rentalrequestapp.repository.rentalRequestRepository;

import com.example.rentalrequestapp.model.rentalRequestModel.RentalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {
}





