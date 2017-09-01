package ua.org.nalabs.javalessons.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import ua.org.nalabs.javalessons.javafx.Main;
import ua.org.nalabs.javalessons.javafx.repository.PersonRepositoryDB;

import java.io.File;

public class MenubarController {

    private PersonRepositoryDB repository;

    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    MenuItem fillDBMenuItem;

    @FXML
    private void handleNew() {
        repository = new PersonRepositoryDB();
        repository.getPersonObservableList().clear();
        main.setPersonFilePath(null);
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showOpenDialog(main.getStage());

        if(file != null) {
            main.loadPersonDataFromFile(file);
        }
    }

    @FXML
    private void handleSave() {
        File personFile = main.getPersonFilePath();
        if (personFile != null) {
            main.savePersonDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.savePersonDataToFile(file);
        }
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Person");
        alert.setHeaderText("About");
        alert.setContentText("Author: Andriy Lutskiv based on sample of Marco");

        alert.showAndWait();
    }


    @FXML
    private void fillDB() {
        PersonRepositoryDB repository = new PersonRepositoryDB();
        repository.initDB();
        repository.fillDB();
    }



    @FXML
    private void exitProgram() {
        System.exit(0);
    }

    @FXML
    private void handleShowBirthdayAsHistogramStatistics() {
        main.showBirthdayAsHistogramStatistics();
    }

    @FXML
    private void handleShowBirthdayStatisticsAsPieChart() {
        main.showBirthdayAsPieChartStatistics();
    }

    @FXML
    public void handleShowCurrenceStatistics() {
        main.showCurrencyStatistics();
    }
}
