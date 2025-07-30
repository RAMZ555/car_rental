package com.example.rentalrequestapp.repository.carRepository;



import com.example.rentalrequestapp.model.carModel.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
