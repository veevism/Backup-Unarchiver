module se233.unarchiver {
    requires javafx.controls;
    requires javafx.fxml;
    requires zip4j;


    opens se233.unarchiver to javafx.fxml;
    opens se233.unarchiver.controller to javafx.fxml;
    exports se233.unarchiver;
    exports se233.unarchiver.controller;
}