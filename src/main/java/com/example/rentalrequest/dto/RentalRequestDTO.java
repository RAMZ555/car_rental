package com.example.rentalrequest.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RentalRequestDTO {
    private String customerName;
    private String phoneNumber;
    private String email;
    private String carModel; // auto-filled from selected car in frontend
    private LocalDateTime pickupDateTime;
    private LocalDateTime dropDateTime;
}






