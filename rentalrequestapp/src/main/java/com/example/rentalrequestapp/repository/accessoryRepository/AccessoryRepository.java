package com.example.rentalrequestapp.repository.accessoryRepository;



import com.example.rentalrequestapp.model.accessoryModel.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
}

