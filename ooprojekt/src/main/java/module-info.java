module org.ponmaj.ooprojekt {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    requires java.prefs;


    opens org.ponmaj.ooprojekt to javafx.fxml;
    exports org.ponmaj.ooprojekt;
    exports org.ponmaj.ooprojekt.presenter;
    opens org.ponmaj.ooprojekt.presenter to javafx.fxml;
    exports org.ponmaj.ooprojekt.model;
    opens org.ponmaj.ooprojekt.model to javafx.fxml;
}