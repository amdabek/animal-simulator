module org.pon1645.ooprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.pon1645.ooprojekt to javafx.fxml;
    exports org.pon1645.ooprojekt;
}