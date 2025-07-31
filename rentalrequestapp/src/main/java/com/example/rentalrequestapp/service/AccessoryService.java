package com.example.rentalrequest.service;



import com.example.rentalrequest.model.Accessory;
import com.example.rentalrequest.repository.AccessoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessoryService {

    @Autowired
    private AccessoryRepository accessoryRepository;

    public Accessory saveAccessory(Accessory accessory) {
        return accessoryRepository.save(accessory);
    }

    public List<Accessory> getAllAccessories() {
        return accessoryRepository.findAll();
    }
}

