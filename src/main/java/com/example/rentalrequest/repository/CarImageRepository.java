package com.example.rentalrequest.repository;

import com.example.rentalrequest.model.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarImageRepository extends JpaRepository<CarImage, Long> { }
