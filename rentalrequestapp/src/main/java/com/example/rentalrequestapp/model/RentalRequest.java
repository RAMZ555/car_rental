package com.example.rentalrequest.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String carModel;
    private int rentalDays;

    private String pickupLocation;
    private String dropLocation;
    private LocalDateTime pickupDateTime;
    private LocalDateTime dropDateTime;
}







