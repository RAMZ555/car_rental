package com.example.rentalrequest.model;

import com.example.rentalrequest.converter.StringEncryptionConverter;
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

    @Convert(converter = StringEncryptionConverter.class)
    @Column(columnDefinition = "TEXT")
    private String customerName;

    @Convert(converter = StringEncryptionConverter.class)
    @Column(columnDefinition = "TEXT")
    private String phoneNumber;

    @Convert(converter = StringEncryptionConverter.class)
    @Column(columnDefinition = "TEXT")
    private String email;

    // NO encryption for business logic fields
    private String carModel; // auto-filled from selected car

    private LocalDateTime pickupDateTime;
    private LocalDateTime dropDateTime;

    private String status; // pending / accepted / rejected
    private String rejectionReason;
}













