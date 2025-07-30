package com.example.rentalrequestapp.service.rentalRequestService;

import com.example.rentalrequestapp.model.rentalRequestModel.RentalRequest;
import com.example.rentalrequestapp.repository.rentalRequestRepository.RentalRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalRequestService {

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    public List<RentalRequest> getAllRequests() {
        return rentalRequestRepository.findAll();
    }

    public RentalRequest saveRequest(RentalRequest request) {
        return rentalRequestRepository.save(request);
    }

    public void deleteRequestById(Long id) {
        rentalRequestRepository.deleteById(id);
    }

    public RentalRequest getRequestById(Long id) {
        return rentalRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental request not found with id: " + id));
    }
}



