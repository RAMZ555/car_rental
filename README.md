## 🚗 Car Rental Request App — Project Structure

A simple Spring Boot REST API for managing car rental requests.
#

###📁 RentalRequest.java
Location: model package

This is the Entity class.

It represents a car rental request record in the database.

Fields: id, customerName, carModel, rentalDays.

Annotated with @Entity for JPA mapping to a table.

Lombok annotations like @Data, @NoArgsConstructor, and @AllArgsConstructor generate boilerplate code like getters, setters, and constructors.



##📁 RentalRequestRepository.java
Location: repository package

This is a Spring Data JPA interface.

Extends JpaRepository<RentalRequest, Long>.

Automatically provides methods like save(), findAll(), findById(), deleteById(), etc.

No need to implement manually — Spring generates the implementation at runtime.



##📁 RentalRequestService.java
Location: service package

This is the service layer.

Contains business logic for handling rental requests.

Calls the repository to interact with the database.

Methods: getAllRentals(), getRentalById(), createRental(), updateRental(), deleteRental().



##📁 RentalRequestController.java
Location: controller package

This is the REST API controller.

Maps HTTP requests to the service methods.

Routes:

GET /api/rentals → Get all rentals

GET /api/rentals/{id} → Get rental by ID

POST /api/rentals → Create new rental

PUT /api/rentals/{id} → Update rental

DELETE /api/rentals/{id} → Delete rental

Uses @RestController and @RequestMapping.



##📁 RentalRequestAppApplication.java
Location: main package

This is the main class of the application.

Runs the Spring Boot app.

Annotated with @SpringBootApplication.
