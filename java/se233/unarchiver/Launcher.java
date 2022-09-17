package se233.unarchiver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("main-view-scene1.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        try{
            String css = this.getClass().getResource("style/style.css").toExternalForm();
            scene.getStylesheets().add(css);
        }catch (Exception er) {
            System.out.println("Css files not found");
        }

        try{
            Image icon = new Image(this.getClass().getResource("assets/icon.jpeg").toExternalForm());
            this.stage.getIcons().add(icon);
        }catch (Exception er) {
            System.out.println("Icon files not found");
        }

        this.stage.setTitle("Unarchiver by Zomsan Tech.");
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}