module your.module.name {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens main to javafx.graphics, javafx.fxml;
    opens gui to javafx.fxml;
    opens entities to javafx.base;
} 