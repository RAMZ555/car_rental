package com.example.rentalrequest.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private String fuel;
    private String carType;
    private String plateNumber;
    private String mainLocation;
    private String color;
    private String transmission;
    private Integer year;
    private Integer passengers;
    private Integer noOfAirBags;
    private String description;
    private BigDecimal dailyPrice;
    private BigDecimal weeklyPrice;
    private BigDecimal monthlyPrice;
    private boolean available;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Accessory> accessories = new HashSet<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CarImage> images = new HashSet<>();

}












