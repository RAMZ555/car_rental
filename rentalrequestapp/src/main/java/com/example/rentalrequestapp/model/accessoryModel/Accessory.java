package com.example.rentalrequestapp.model.accessoryModel;



import com.example.rentalrequestapp.model.carModel.Car;
import jakarta.persistence.*;
        import lombok.*;

        import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accessory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToMany(mappedBy = "accessories")
    private Set<Car> cars = new HashSet<>();
}
