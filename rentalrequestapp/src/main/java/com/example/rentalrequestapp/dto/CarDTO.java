package com.example.rentalrequest.dto;

import lombok.Data;
import java.util.Set;

@Data
public class CarDTO {
    private Long id;
    private String brand;
    private String model;
    private String fuel;
    private String carType;
    private String plateNumber;
    private String mainLocation;
    private String color;
    private Integer year;
    private String transmission;
    private Integer passengers;
    private Integer noOfAirBags;
    private String description;
    private double dailyPrice;
    private double weeklyPrice;
    private double monthlyPrice;
    private boolean available;
    private Set<String> accessories; // Accessory names only
}


