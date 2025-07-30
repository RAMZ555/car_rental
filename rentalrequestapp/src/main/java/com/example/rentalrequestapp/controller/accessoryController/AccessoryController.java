package com.example.rentalrequestapp.controller.accessoryController;



import com.example.rentalrequestapp.model.accessoryModel.Accessory;
import com.example.rentalrequestapp.service.accessoryService.AccessoryService;
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

