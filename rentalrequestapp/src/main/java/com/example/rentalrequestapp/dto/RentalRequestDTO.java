package com.example.rentalrequest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalRequestDTO {
    private String name;
    private String pickupLocation;
    private String dropLocation;
    private LocalDateTime pickupDateTime;
    private LocalDateTime dropDateTime;
}

