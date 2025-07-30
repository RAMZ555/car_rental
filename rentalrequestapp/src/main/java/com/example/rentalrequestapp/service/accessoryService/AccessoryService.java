package com.example.rentalrequestapp.service.accessoryService;



import com.example.rentalrequestapp.model.accessoryModel.Accessory;
import com.example.rentalrequestapp.repository.accessoryRepository.AccessoryRepository;
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

