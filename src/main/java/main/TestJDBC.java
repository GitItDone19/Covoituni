package main;

import entities.Car;
import entities.Marque;
import entities.Model;
import services.CarService;
import services.MarqueService;
import services.ModelService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TestJDBC {

    public static void main(String[] args) {
        CarService carService = new CarService();
        MarqueService marqueService = new MarqueService();
        ModelService modelService = new ModelService();

        try {
            // Step 1: Insert a new Marque and get its ID
            Marque marque = new Marque("/images/toyota.png", "Toyota");
            marque.setId(marqueService.create(marque));
            System.out.println("Marque added successfully: " + marque);

            // Step 2: Insert a new Model and get its ID
            Model model = new Model("Corolla");
            model.setId(modelService.create(model));
            System.out.println("Model added successfully: " + model);

            // Step 3: Insert the first Car using the created Marque and Model
            Car car1 = new Car("123ABC", marque.getId(), model.getId(), "Sedan", new Date(), "Red");
            car1.setId(carService.create(car1));
            System.out.println("Car 1 added successfully!");

            // Step 4: Retrieve and display all cars after first insertion
            System.out.println("\nList of cars after adding Car 1:");
            System.out.println(carService.readAll());

            // Step 5: Insert the second Car
            Model model2 = new Model("Camry");
            model2.setId(modelService.create(model2));
            Car car2 = new Car("456XYZ", marque.getId(), model2.getId(), "Sedan", new Date(), "Blue");
            car2.setId(carService.create(car2));
            System.out.println("\nCar 2 added successfully!");

            // Step 6: Retrieve and display all cars after adding Car 2
            System.out.println("\nList of cars after adding Car 2:");
            System.out.println(carService.readAll());

            // Step 7: Modify Car 2 and update in the database
            car2.setCouleur("White");
            carService.update(car2);
            System.out.println("\nCar 2 updated successfully!");

            // Step 8: Retrieve and display all cars after modification
            System.out.println("\nList of cars after modifying Car 2:");
            System.out.println(carService.readAll());

            // Step 9: Insert the third Car
            Model model3 = new Model("Yaris");
            model3.setId(modelService.create(model3));
            Car car3 = new Car("789LMN", marque.getId(), model3.getId(), "Hatchback", new Date(), "Black");
            car3.setId(carService.create(car3));
            System.out.println("\nCar 3 added successfully!");

            // Step 10: Retrieve and display all cars after adding Car 3
            System.out.println("\nList of cars after adding Car 3:");
            System.out.println(carService.readAll());

            // Step 11: Delete Car 3
            carService.delete(car3);
            System.out.println("\nCar 3 deleted successfully!");

            // Step 12: Retrieve and display all cars after deletion
            System.out.println("\nList of cars after deleting Car 3:");
            System.out.println(carService.readAll());

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }
}
