package se233.unarchiver.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se233.unarchiver.Launcher;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML
    private Button s2button; //test button change scene 1 -> scene 2
    @FXML
    private Button archivebutton; //commit to archive button
    @FXML
    private Button extractbutton;
    @FXML
    public ListView<String> inputListView;

    public ObservableList<String> tranfercontent;
    @FXML
    private Label labelresponse;
    @FXML
    private Button searchbutton;
    @FXML
    private Button deletebutton;
    private Alert a = new Alert(Alert.AlertType.NONE);
    private FileChooser fileChooser = new FileChooser();

    //Contain any caller method that associated with scene 1.
    public void initialize ()
    {
        //pop up scene 2.
        //scene 2 is for choose archive formats.

        //when user drag some files over the listview it will change the cursor type.
        inputListView.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasFiles()) {
                e.acceptTransferModes(TransferMode.COPY);
            } else {
                e.consume();
            }
        });

        //users can select multiple file from listview.
        inputListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        //------not in the product.
//        inputListView.getItems().addAll("Fundamentals", "Med Surg", "Pediatrics", "Oncology");
//        inputListView.getItems().addAll("Fundamentals", "Med Surg", "Pediatrics", "Oncology");
//        inputListView.getItems().addAll("Fundamentals", "Med Surg", "Pediatrics", "Oncology");
//        inputListView.getItems().addAll("Fundamentals", "Med Surg", "Pediatrics", "Oncology");
//        inputListView.getItems().addAll("Fundamentals", "Med Surg", "Pediatrics", "Oncology");
//        inputListView.getItems().addAll("Fundamentals", "Med Surg", "Pediatrics", "Oncology");
//        inputListView.getItems().addAll("Fundamentals", "Med Surg", "Pediatrics", "Oncology");
//        inputListView.getItems().addAll("Fundamentals", "Med Surg", "Pediatrics", "Oncology");

        //------not in the product.

        //user can drag file then drop on the listview.
        inputListView.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                String filePath;
                int total_files = db.getFiles().size();
                for (int i = 0; i < total_files; i++) {
                    File file = db.getFiles().get(i);
                    filePath = file.getAbsolutePath();
                    inputListView.getItems().add(filePath);
                }
            }
            e.setDropCompleted(success);
            e.consume();
        });

        inputListView.setOnMouseClicked(e -> {
            try {
                showSelectedItem(e,inputListView,scrollpaneresponse);
            } catch (Exception ignored) {}
        });

        //use label to tell that which files is being selected.
        archivebutton.setOnAction(e ->
        {
           try{
               commitArchive(e, inputListView);
           } catch (IndexOutOfBoundsException er) {
               exceptionmessegesAlert(er, "Need To Select File First");
           } catch (NullPointerException er) {
               exceptionmesseges(er, "Can't Popup Another Scenes");
           } catch (Exception er) {
               exceptionmesseges(er, "Something Went Wrong");
           }
        });

        extractbutton.setOnAction(e -> {

        });

        //delete files that being selected.
        deletebutton.setOnAction(e -> {
            try{
                listviewDeleteItems(inputListView);
            }catch (IndexOutOfBoundsException er) {
                exceptionmessegesAlert(er, "Need To Select File First");
            }catch (Exception er) {
                exceptionmessegesAlert(er, "Something Went Wrong");
            }});

        //search and add files from any directory use file explorer popup.
        searchbutton.setOnAction(e -> {
            try {
                listviewSearchItems(inputListView);
            }catch (Exception ignored) {}
        });

    }

    @FXML
    private void popupScene(String stageTitle,String fxmlName) throws IOException, Exception {
        Parent root = FXMLLoader.load(Launcher.class.getResource(fxmlName)); //"main-view-scene2.fxml"
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();

        primaryStage.setTitle(stageTitle);
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.NONE);
        primaryStage.initOwner(s2button.getScene().getWindow());
        primaryStage.show();
    }

    private void listviewSearchItems (ListView<String> sampleListView) throws Exception {
        fileChooser.setTitle("Search Files");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

        List<File> selectedFile = fileChooser.showOpenMultipleDialog(Launcher.stage);

        for (int i = 0; i < selectedFile.size(); i++) {
            sampleListView.getItems().add(selectedFile.get(i).getAbsolutePath());
        }
    }

    private void listviewDeleteItems(ListView<String> sampleListView) throws Exception{
        ObservableList<Integer> selectedindices;
        selectedindices = getSelectedIndices(sampleListView);

        if (selectedindices.size() == 0 ){
            throw new IndexOutOfBoundsException();
        }
        for (int i = selectedindices.size() - 1; i >= 0 ; i--) {
            sampleListView.getItems().remove((int)selectedindices.get(i));
        }

        scrollpaneresponse.setContent(new Text(""));

    }

    private ObservableList<Integer> getSelectedIndices(ListView<String> sampleListView) {
        ObservableList<Integer> topics;
        return topics = sampleListView.getSelectionModel().getSelectedIndices();
    }

    private ObservableList<String> getSelectedItems(ListView<String> sampleListView) {
        ObservableList<String> topics;
        return topics = sampleListView.getSelectionModel().getSelectedItems();
    }

    @FXML
    private ScrollPane scrollpaneresponse;
    private void commitArchive(ActionEvent e, ListView<String> sampleListView) throws Exception {

        //contain every elements that's user select from listview
        ObservableList<String> selecteditems;
        selecteditems = getSelectedItems(sampleListView);

        if (selecteditems.size() == 0) {
            throw new IndexOutOfBoundsException();
        }

        if (confirmation("Are you sure you want to archive these files?")) {

//            tranfercontent = getSelectedItems(sampleListView);
            tranfercontent = selecteditems;
//            System.out.println(tranfercontent);
//            popupScene("Archive format","main-view-scene2.fxml");

            FileInfo samplefile = new FileInfo(tranfercontent);

            Node node = (Node) e.getSource();

            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

            try {
                Parent root = FXMLLoader.load(Launcher.class.getResource("main-view-scene2.fxml"));
                stage.setUserData(samplefile);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (Exception er){
                System.out.println(er);
            }

//            Parent root = FXMLLoader.load(Launcher.class.getResource("main-view-scene2.fxml"));
//            Scene scene = new Scene(root);
//            Stage primaryStage = new Stage();
//
//            primaryStage.setTitle("Archive Format");
//            primaryStage.setScene(scene);
//            primaryStage.initModality(Modality.NONE);
//            primaryStage.initOwner(s2button.getScene().getWindow());
//            primaryStage.show();
        }
    }

    private void commitExtract(ActionEvent e, ListView<String> sampleListView) {
        //haven't done yet.
        ObservableList<String> selecteditems;
        selecteditems = getSelectedItems(sampleListView);

        if (selecteditems.size() == 0) {
            throw new IndexOutOfBoundsException();
        }


    }

    public void exceptionmessegesAlert(Exception er, String msg) {
        System.out.println("Exception occurs : " + er);
        a.setAlertType(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.show();
    }

    public void exceptionmesseges(Exception er, String msg) {
        System.out.println("Exception occurs : " + er);
        System.out.print(" " + msg);
    }

    public void exceptionmesseges(Exception er) {
        System.out.println("Exception occurs : " + er);
    }

    private void showSelectedItem(MouseEvent event,ListView<String> sampleListView, ScrollPane sampleScrollpane) throws Exception{
        ObservableList<String> selecteditem;
        selecteditem = getSelectedItems(sampleListView);
        String list= "";

        if (selecteditem.size() == 0) {
            System.out.println("Success");
            sampleScrollpane.setContent(new Text("You have selected " + selecteditem.size()  +" Item"));
            throw new IndexOutOfBoundsException();

        }
        for (String m : selecteditem) {
            list += m + ", ";
            list += "\n";
        }
        int pos= list.lastIndexOf(",");
        String selection= list.substring(0, pos);
        sampleScrollpane.setContent(new Text("You have selected " + selecteditem.size()  +" Item : \n"+ selection));
    }

    public boolean confirmation (String msg) {
        a.setAlertType(Alert.AlertType.CONFIRMATION);
        a.setContentText(msg);
        a.showAndWait();

        if (a.getResult() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }

    }

}
