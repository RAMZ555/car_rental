package com.example.rentalrequestapp.controller;



import com.example.rentalrequestapp.model.Accessory;
import com.example.rentalrequestapp.service.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accessories")
public class AccessoryController {

    @Autowired
    private AccessoryService accessoryService;

    @PostMapping
    public Accessory addAccessory(@RequestBody Accessory accessory) {
        return accessoryService.saveAccessory(accessory);
    }

    @GetMapping
    public List<Accessory> getAllAccessories() {
        return accessoryService.getAllAccessories();
    }
}

