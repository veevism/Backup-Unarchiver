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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import se233.unarchiver.Launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField locationField;

    private DirectoryChooser directoryChooser = new DirectoryChooser();

    private String nameee;
    private String passworddd;
    private String locationtiontion;








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
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(false);
            boolean checknull = false;
//               commitArchive(e, inputListView);
               if (Objects.equals(nameField.getText(), "")) {
                    checknull = true;
               }

               if (Objects.equals(passwordField.getText(), "")) {
                   checknull = true;
               }else {
                   zipParameters.setEncryptFiles(true);
                   System.out.println("HEEE");
               }

               if (Objects.equals(locationField.getText(), "")) {
                   checknull = true;
               }

               if (checknull) {
                   throw new NullPointerException("You Missing SOmething");
               }

               nameee = nameField.getText();
               passworddd = passwordField.getText();
               locationtiontion = locationField.getText();


            zipParameters.setEncryptionMethod(EncryptionMethod.AES);

            ObservableList<String> selectedItems = getSelectedItems(inputListView);

            List<File> filesToAdd = new ArrayList<File>();

            for (int i = 0; i < selectedItems.size(); i++) {
                filesToAdd.add(new File(((String) selectedItems.get(i))));
            }

            System.out.println(filesToAdd);

            String sumLocation = (locationtiontion + "/" + nameee + ".zip");

            ZipFile zipFile = new ZipFile(sumLocation, passworddd.toCharArray());
            try {
                zipFile.addFiles(filesToAdd, zipParameters);
                zipFile.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        });

        locationField.setOnMouseClicked(e -> {
            File selectedDirectory = directoryChooser.showDialog(Launcher.stage);
            if (selectedDirectory != null) {
                locationField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        extractbutton.setOnAction(e -> {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(false);
            boolean checknull = false;
//               commitArchive(e, inputListView);
            if (Objects.equals(nameField.getText(), "")) {
                checknull = true;
            }

            if (Objects.equals(passwordField.getText(), "")) {
                checknull = true;
            }

            if (Objects.equals(locationField.getText(), "")) {
                checknull = true;
            }

            if (checknull) {
                throw new NullPointerException("You Missing SOmething");
            }

            nameee = nameField.getText();
            passworddd = passwordField.getText();
            locationtiontion = locationField.getText();

            String sumLocation = locationtiontion + "/" + nameee;

            File theDir = new File(sumLocation);

            if (!theDir.exists()){
                theDir.mkdirs();
            } else {
                System.out.println("This directory already exists");
            }

            String filepathpath = (String) getSelectedItems(inputListView).get(0);
            ZipFile zipFile = new ZipFile(new File(filepathpath));

            try {
                zipFile.extractAll(sumLocation);
            } catch (ZipException ex) {
                throw new RuntimeException(ex);
            }
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

            tranfercontent = selecteditems;

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
