package ua.org.nalabs.javalessons.javafx.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ua.org.nalabs.javalessons.javafx.model.Person;
import ua.org.nalabs.javalessons.javafx.controller.DBBrowserController;
import ua.org.nalabs.javalessons.javafx.repository.PersonRepositoryDB;
import ua.org.nalabs.javalessons.javafx.Main;
import ua.org.nalabs.javalessons.javafx.util.DateUtil;

/*

    Всі поля і методи, до яких fxml файлу потрібен доступ, повинні бути позначені анотацією @FXML.
    Правда, це вірно тільки для полів і методів з модифікатором private,
    але краще залишити їх такими та позначати анотацією, ніж робити публічними!

    Метод initialize() автоматично викликається після завантаження fxml файлу.
    На цей момент всі FXML поля повинні бути ініціалізовані;

    Метод setCellValueFactory(...) використовується для визначення того,
    яке поле всередині класу Person буде використовуватися для конкретного стовпця в таблиці.
    Стрілка -> означає, що ми використали лямбда-вираз із Java 8.
    (Іншим варіантом зробити це - використати PropertyValueFactory, але цей спосіб порушує безпеку типів).

    В нашому випадку використовуються лише значення StringProperty.
    Якщо ви хочете використати IntegerProperty або DoubleProperty,
    в методі setCellValueFactory(...) має бути додатковий виклик asObject():

    myIntegerColumn.setCellValueFactory(cellData ->
      cellData.getValue().myIntegerProperty().asObject());

        Це необхідно через погане архітектурне рішення JavaFX

 */

public class DBBrowserController {
    @FXML
    private TableView<Person> personTable;

    public TableView<Person> getPersonTableView() {
        return this.personTable;
    }

    public void setPersonTable(TableView<Person> personTable) {
        this.personTable = personTable;
    }

    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    private PersonRepositoryDB repository;

    // Reference to the main application.
    private Main main;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public DBBrowserController() {
        repository = new PersonRepositoryDB();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

    // Для отримання інформації про те, що користувач вибрав запис у таблиці, нам необхідно прослуховувати зміни.
    // Для цього в JavaFX існує інтерфейс ChangeListener з одним методом changed(...).
    // Цей метод має три параметри: observable, oldValue і newValue.
    // Ми будемо створювати інтерфейс ChangeListener користуючись лямбда-виразами з Java 8.
    // Давайте додамо кілька рядків коду до методу initialize() класу PersonOverviewController.


        // Clear person details.
        // Якщо ми передаємо null в параметр методу showPersonDetails(...), то будуть стерті всі значення міток.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        // У рядку personTable.getSelectionModel... ми отримуємо selectedItemProperty таблиці і додаємо до нього слухача.
        // Коли користувач вибирає запис у таблиці, виконується наш лямбда-вираз.
        // Ми беремо тільки що обраний запис і передаємо його в метод showPersonDetails(...).
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;

        personTable.setItems(repository.getPersonObservableList());
    }

    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param person the person or null
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            // Fill the labels with info from the person object.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));

        } else {
            // Person is null, remove all the text.
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = main.showPersonEditDialog(tempPerson);
        if (okClicked) {
//            repository.getPersonObservableList().add(tempPerson);
            personTable.getItems().add(tempPerson);
            repository.insertIntoDB(tempPerson);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();

        if (selectedPerson != null) {
            String pfirstName = selectedPerson.getFirstName();
            String psecondName = selectedPerson.getLastName();
            boolean okClicked = main.showPersonEditDialog(selectedPerson);
            if (okClicked) {



                showPersonDetails(selectedPerson);
                repository.updateIntoDB(
                        selectedPerson.getFirstName(),
                        selectedPerson.getLastName(),
                        selectedPerson.getStreet(),
                        selectedPerson.getPostalCode(),
                        selectedPerson.getCity(),
                        selectedPerson.getBirthday(),
                        pfirstName,
                        psecondName
                );
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        // if selected nothing or DB is empty
        if(selectedIndex >= 0) {
            // delete from View
            personTable.getItems().remove(selectedIndex);
            // delete from DB
            Person person = personTable.getSelectionModel().getSelectedItem();
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            repository.remove(firstName, lastName);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getStage());
            alert.setTitle("No one item is selected! (title text)");
            alert.setHeaderText("No Person is selected! (header text)");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }




}
