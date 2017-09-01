package ua.org.nalabs.javalessons.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ua.org.nalabs.javalessons.javafx.controller.*;
import ua.org.nalabs.javalessons.javafx.model.Person;
import ua.org.nalabs.javalessons.javafx.model.PersonListWrapper;
import ua.org.nalabs.javalessons.javafx.repository.PersonRepositoryDB;
import ua.org.nalabs.javalessons.javafx.util.CurrencyUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/*

 */
public class Main extends Application {
    private Stage stage;
    private BorderPane rootPane;
    private PersonRepositoryDB personRepositoryDB;


    public static void main(String[] args) {
        launch(args);

    }

    /**
     * Will be automatically called by main method's launch() method
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("Persons");
        this.stage.getIcons().add(new Image("file:resources/images/smile.png"));
        personRepositoryDB = new PersonRepositoryDB();
        initRootPane();
        personOverview();
    }

    private void initRootPane() {
        try {
            FXMLLoader root = new FXMLLoader();
            root.setLocation(Main.class.getResource("view/menubar.fxml"));
            rootPane = (BorderPane) root.load();
            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            MenubarController menubarController = root.getController();
            menubarController.setMain(this);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Try to load last opened person file.
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }

    }

    private void personOverview(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("view/dbbrowser.fxml"));
            AnchorPane personOverview = (AnchorPane) fxmlLoader.load();
            rootPane.setCenter(personOverview);
            DBBrowserController controller = fxmlLoader.getController();
            controller.setMain(this);

        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/personedit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PersoneditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Stage getStage() {
        return stage;
    }

    /**
 * Returns the person file preference, i.e. the file that was last opened.
 * The preference is read from the OS specific registry. If no such
 * preference can be found, null is returned.
 *
 * @return
 */
public File getPersonFilePath() {
    Preferences prefs = Preferences.userNodeForPackage(Main.class);
    String filePath = prefs.get("filePath", null);
    if (filePath != null) {
        return new File(filePath);
    } else {
        return null;
    }
}

/**
 * Sets the file path of the currently loaded file. The path is persisted in
 * the OS specific registry.
 *
 * @param file the file or null to remove the path
 */
public void setPersonFilePath(File file) {
    Preferences prefs = Preferences.userNodeForPackage(Main.class);
    if (file != null) {
        prefs.put("filePath", file.getPath());

        // Update the stage title.
        stage.setTitle("AddressApp - " + file.getName());
    } else {
        prefs.remove("filePath");

        // Update the stage title.
        stage.setTitle("AddressApp");
    }
}

    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     * @param file
     */
    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personRepositoryDB.getPersonObservableList().clear();
            personRepositoryDB.getPersonObservableList().addAll(wrapper.getPersons());

            // Save the file path to the registry.
            setPersonFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personRepositoryDB.getPersonObservableList());

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setPersonFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Opens a dialog to show birthday statistics.
     */
    public void showBirthdayAsHistogramStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BirthdayStatisticsAsHistogram.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            BirthdayStatisticsAsHistogramController controller = loader.getController();
            personRepositoryDB = new PersonRepositoryDB();
            controller.setPersonData(personRepositoryDB.getPersonObservableList());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to show birthday statistics.
     */
    public void showCurrencyStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CurrencyStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("bank.gov.ua Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            CurrencyStatisticsController controller = loader.getController();


            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBirthdayAsPieChartStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BirthdayStatisticsAsPieChart.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            BirthdayStatisticsAsPieChartController controller = loader.getController();
            personRepositoryDB = new PersonRepositoryDB();
            controller.setPersonData(personRepositoryDB.getPersonObservableList());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
