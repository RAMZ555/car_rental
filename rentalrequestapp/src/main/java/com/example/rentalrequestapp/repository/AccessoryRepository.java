package com.example.rentalrequestapp.repository;



import com.example.rentalrequestapp.model.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
}

