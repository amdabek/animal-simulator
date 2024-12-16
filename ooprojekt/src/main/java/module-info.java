module org.pon1645.ooprojekt {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.pon1645.ooprojekt to javafx.fxml;
    exports org.pon1645.ooprojekt;
}