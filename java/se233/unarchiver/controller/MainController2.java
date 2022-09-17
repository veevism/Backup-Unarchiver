package se233.unarchiver.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;

public class MainController2 {
    @FXML
    private Button s1button;


    @FXML
    private void initialize() {
        s1button.setOnAction(e -> {
            Node node = (Node) e.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            FileInfo fileInfo = (FileInfo) stage.getUserData();

            ObservableList<String> path = fileInfo.getTranfercontent();

            System.out.println(path);

            System.out.println("Hello");


        });
    }
}
