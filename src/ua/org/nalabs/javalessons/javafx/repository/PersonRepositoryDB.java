package ua.org.nalabs.javalessons.javafx.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.org.nalabs.javalessons.javafx.controller.DBBrowserController;
import ua.org.nalabs.javalessons.javafx.model.Person;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonRepositoryDB implements PersonRepository {
    private final static Logger LOGGER = Logger.getLogger("PersonRepositoryDB.class");
    private ObservableList<Person> personObservableList;
    private Connection connection;

    public static void main(String[] args) {
        PersonRepositoryDB personRepositoryDB = new PersonRepositoryDB();
        personRepositoryDB.connection = personRepositoryDB.getConnection();
        personRepositoryDB.initDB();
        personRepositoryDB.fillDB();
        personRepositoryDB.getPersonObservableList().forEach(System.out::println);
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
           connection = DriverManager.getConnection("jdbc:sqlite:addressbook.db");
        } catch ( SQLException sqlexception ) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
        return connection;
    }

    public void remove(String firstName, String lastName) {
        String sql = "DELETE FROM persons WHERE FIRSTNAME = ? AND SECONDNAME = ?";
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.execute();
        }catch (SQLException sqlexception) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
    }
    // for demo purposes we store in DATETIME format and LocalDateTime
    public void updateIntoDB(String firstName, String lastName, String street, int postalCode, String city, LocalDateTime birthday, String srcFName, String srcLName) {
        String sql = "UPDATE persons SET FIRSTNAME=?, SECONDNAME=?, STREET=?, POSTCODE=?, CITY=?, BIRTHDAY=? WHERE FIRSTNAME = ? AND SECONDNAME = ?";
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, street);
            statement.setInt(4, postalCode);
            statement.setString(5, city);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            LocalDateTime birthday2 = LocalDateTime.parse(java.sql.Timestamp.valueOf(birthday).toString(), formatter);

//            statement.setDate(6, java.sql.Date.valueOf(birthday) );
//            statement.setTimestamp(6, java.sql.Timestamp.valueOf(birthday));
            statement.setString(6, java.sql.Timestamp.valueOf(birthday2).toString());


            statement.setString(7, srcFName);
            statement.setString(8, srcLName);

            int rs = statement.executeUpdate();
        }catch (SQLException sqlexception) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
    }

    public void initDB() {
        String sql = "CREATE TABLE IF NOT EXISTS persons " +
//        String sql = "CREATE TABLE persons " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FIRSTNAME VARCHAR(128) NOT NULL," +
                "SECONDNAME VARCHAR(128) NOT NULL," +
                "STREET VARCHAR(128) NOT NULL DEFAULT \'RUSKA\'," +
                "CITY VARCHAR(128) NOT NULL DEFAULT \'TERNOPIL\'," +
                "POSTCODE INT NOT NULL DEFAULT 46000," +
                "BIRTHDAY DATETIME" +
                ")";
        try (Connection connection = this.getConnection();
             Statement  statement = connection.createStatement()) {
                statement.execute(sql);
                statement.close();
        } catch (SQLException sqlexception) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
    }

    public void insertIntoDB(Person person) {
        this.insertIntoDB(person.getFirstName(), person.getLastName(), person.getStreet(), person.getPostalCode(), person.getCity(), person.getBirthday());
    }

    public void insertIntoDB(String firstName, String lastName, String street, int postalCode, String city, LocalDateTime birthday) {
        String sql = "INSERT INTO persons(FIRSTNAME, SECONDNAME, STREET, POSTCODE, CITY, BIRTHDAY) VALUES(?,?,?,?,?,?)";
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, street);
            statement.setInt(4, postalCode);
            statement.setString(5, city);
            statement.setTimestamp(6, java.sql.Timestamp.valueOf(birthday));
            statement.setString(6, java.sql.Timestamp.valueOf(birthday).toString());
            statement.execute();
        }catch (SQLException sqlexception) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
    }

    public void fillDB() {
        try(Connection connection = this.getConnection();
            Statement statement = connection.createStatement()){
            statement.execute("INSERT INTO persons(FIRSTNAME, SECONDNAME, STREET, POSTCODE, CITY, BIRTHDAY) " +
                    "VALUES(\'Hans\', \'Muster\', \'Baker St.\', 46018, \'Ternopil\', '1999-02-21 10:00:00.0')");

            statement.execute("INSERT INTO persons(FIRSTNAME, SECONDNAME, STREET, POSTCODE, CITY, BIRTHDAY) " +
                    "VALUES(\'Ruth\', \'Mueller\', \'Ruska Street\', 46000, \'Ternopil\', '1999-02-21 20:00:00.0')");

            statement.execute("INSERT INTO persons(FIRSTNAME, SECONDNAME, STREET, POSTCODE, CITY, BIRTHDAY) " +
                    "VALUES(\'Heinz\', \'Kurz\', \'Ruska Street\', 46000, \'Ternopil\', '1987-05-25 05:20:00.0')");

            statement.execute("INSERT INTO persons(FIRSTNAME, SECONDNAME, STREET, POSTCODE, CITY, BIRTHDAY) " +
                    "VALUES(\'Cornelia\', \'Meier\', \'Ruska Street\', 46000, \'Ternopil\', '1970-01-21 11:00:00.0')");

            statement.execute("INSERT INTO persons(FIRSTNAME, SECONDNAME, STREET, POSTCODE, CITY, BIRTHDAY) " +
                    "VALUES(\'Werner\', \'Meyer\', \'Ruska Street\', 46000, \'Ternopil\', '1960-01-21 16:00:00.0')");

            statement.execute("INSERT INTO persons(FIRSTNAME, SECONDNAME, STREET, POSTCODE, CITY, BIRTHDAY) " +
                    "VALUES(\'Lydia\', \'Kunz\', \'Ruska Street\', 46000, \'Ternopil\', '1978-03-21 14:00:00.0')");


    }catch (SQLException sqlexception) {
        LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
    }
    }

    public ObservableList<Person> getPersonObservableList() {
        personObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM persons";
        try(Connection connection = this.getConnection();
            Statement statement = connection.createStatement();){

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String firstName = resultSet.getString("FIRSTNAME");
                String lastName = resultSet.getString("SECONDNAME");
                String street = resultSet.getString("STREET");
                int postalCode = resultSet.getInt("POSTCODE");
                String city = resultSet.getString("CITY");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                LocalDateTime birthday = LocalDateTime.parse(resultSet.getString("BIRTHDAY"), formatter);
//                LocalDate birthday = resultSet.getDate("BIRTHDAY").toLocalDate();

                Person p = new Person(firstName, lastName, street, postalCode, city, birthday);
                personObservableList.add(p);
            }

        }catch (SQLException sqlexception) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
        return personObservableList;
    }
}
