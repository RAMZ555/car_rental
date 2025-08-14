package com.example.rentalrequest.repository;

import com.example.rentalrequest.model.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
    List<Accessory> findByCarId(Long carId);
    List<Accessory> findByAvailableTrue();
    List<Accessory> findByNameContainingIgnoreCase(String name);
}









