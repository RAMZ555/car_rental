## 🚗 Car Rental Request App — Project Structure
# A simple Spring Boot REST API for managing car rental requests, cars, and car accessories.

# 📦 Project Layers:
model → JPA Entity classes

repository → Spring Data JPA interfaces

service → Business logic

controller → REST API endpoints

main → Application bootstrap

# 🧾 Entity Classes

📁 RentalRequest.java

Location: model package

Description:

Represents a car rental request record in the database.

Fields: id, customerName, carModel, rentalDays.

Annotations:

@Entity – JPA mapping

@Data, @NoArgsConstructor, @AllArgsConstructor – Lombok for boilerplate code

# 📁 Car.java
Location: model.carModel package
Description:

Represents a car available for rental.

Fields: id, brand, model, fuel, carType, plateNumber, mainLocation, color, yearOfCar, transmission, passengers, noOfAirBags, description, dailyPrice, weeklyPrice, monthlyPrice, and Set<Accessory>.

Relationships:

@ManyToMany with Accessory

# 📁 Accessory.java
Location: model.accessoryModel package
Description:

Represents optional add-ons for cars.

Fields: id, name, description, and Set<Car>.

Relationships:

@ManyToMany(mappedBy = "accessories") with Car

# 🗃️ Repository Interfaces
📁 RentalRequestRepository.java
Location: repository package

Extends JpaRepository<RentalRequest, Long>

Inherits built-in methods: save(), findAll(), findById(), deleteById(), etc.

# 📁 CarRepository.java
Location: repository.carRepo package

Extends JpaRepository<Car, Long>

# 📁 AccessoryRepository.java
Location: repository.accessoryRepo package

Extends JpaRepository<Accessory, Long>

# ⚙️ Service Classes
📁 RentalRequestService.java
Location: service package
Responsibilities:

Handles all business logic for rental requests.

Methods:

getAllRentals(), getRentalById(), createRental(), updateRental(), deleteRental()

# 📁 CarService.java
Location: service.carService package
Responsibilities:

Business logic for managing cars.

Methods:

getAllCars(), getCarById(), addCar(), updateCar(), deleteCar()

# 📁 AccessoryService.java
Location: service.accessoryService package
Responsibilities:

Handles accessory-related operations.

Methods:

getAllAccessories(), getAccessoryById(), addAccessory(), updateAccessory(), deleteAccessory()

# 🌐 Controller Classes
📁 RentalRequestController.java
Location: controller package
Base Route: /api/rentals

Endpoints:

GET /api/rentals → Get all rental requests

GET /api/rentals/{id} → Get rental by ID

POST /api/rentals → Create new rental

PUT /api/rentals/{id} → Update rental

DELETE /api/rentals/{id} → Delete rental

# 📁 CarController.java
Location: controller.carController package
Base Route: /api/cars

Endpoints:

GET /api/cars → List all cars

GET /api/cars/{id} → Get car by ID

POST /api/cars → Add new car

PUT /api/cars/{id} → Update car

DELETE /api/cars/{id} → Delete car

# 📁 AccessoryController.java
Location: controller.accessoryController package
Base Route: /api/accessories

Endpoints:

GET /api/accessories → List all accessories

GET /api/accessories/{id} → Get accessory by ID

POST /api/accessories → Add new accessory

PUT /api/accessories/{id} → Update accessory

DELETE /api/accessories/{id} → Delete accessory

##📁 RentalRequestAppApplication.java
Location: main package

This is the main class of the application.

Runs the Spring Boot app.

Annotated with @SpringBootApplication.
