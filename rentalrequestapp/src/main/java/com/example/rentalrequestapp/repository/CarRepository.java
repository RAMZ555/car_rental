package com.example.rentalrequestapp.repository;



import com.example.rentalrequestapp.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
