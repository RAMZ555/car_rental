package com.example.rentalrequest.repository;



import com.example.rentalrequest.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}



