package com.example.rentalrequestapp.model.carModel;

import com.example.rentalrequestapp.model.accessoryModel.Accessory;

import jakarta.persistence.*;
        import lombok.*;

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
    private String yearOfCar;
    private String transmission;
    private int passengers;
    private int noOfAirBags;
    private String description;

    private double dailyPrice;
    private double weeklyPrice;
    private double monthlyPrice;

    @ManyToMany
    @JoinTable(
            name = "car_accessory",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "accessory_id")
    )
    private Set<Accessory> accessories = new HashSet<>();
}

