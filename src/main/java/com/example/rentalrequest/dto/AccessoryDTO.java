package com.example.rentalrequest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessoryDTO {
    private String name;
    private String description;
    private Boolean available;
    private Long carId;
}






