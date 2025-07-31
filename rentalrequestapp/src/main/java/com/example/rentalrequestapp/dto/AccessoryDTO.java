package com.example.rentalrequest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessoryDTO {
    private String name;
    private String description;
    private Long carId;
    private Boolean available;
}



