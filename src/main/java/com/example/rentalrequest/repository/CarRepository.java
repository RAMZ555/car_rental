package com.example.rentalrequest.repository;

import com.example.rentalrequest.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailableTrue();
    List<Car> findByMainLocationContainingIgnoreCase(String location);
    List<Car> findByBrandContainingIgnoreCase(String brand);
    List<Car> findByCarTypeContainingIgnoreCase(String carType);
}
