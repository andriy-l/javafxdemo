package ua.org.nalabs.javalessons.javafx.model;

import javafx.beans.property.*;
import ua.org.nalabs.javalessons.javafx.util.LocalDateTimeAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Person implements Serializable {

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty street;
    private final IntegerProperty postalCode;
    private final StringProperty city;
    private final ObjectProperty<LocalDateTime> birthday;

    public Person() {
        this(new SimpleStringProperty("Прізвище"), new SimpleStringProperty("Ім\'я"), new SimpleStringProperty("Ruska st."), new SimpleIntegerProperty(0), new SimpleStringProperty("T"),  new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now()));
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName=" + firstName +
                ", lastName=" + lastName +
                ", street=" + street +
                ", postalCode=" + postalCode +
                ", city=" + city +
                ", birthday=" + birthday +
                '}';
    }

    public Person(String firstName, String lastName, String street, int postalCode, String city, LocalDateTime birthday) {
        this.firstName =  new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.street = new SimpleStringProperty(street);
        this.postalCode = new SimpleIntegerProperty(postalCode);
        this.city = new SimpleStringProperty(city);
        this.birthday = new SimpleObjectProperty<LocalDateTime>(birthday);
    }


    public Person(StringProperty firstName, StringProperty lastName, StringProperty street, IntegerProperty postalCode, StringProperty city, ObjectProperty<LocalDateTime> birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public int getPostalCode() {
        return postalCode.get();
    }

    public IntegerProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public LocalDateTime getBirthday() {
        return birthday.get();
    }

    public ObjectProperty<LocalDateTime> birthdayProperty() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday.set(birthday);
    }
}
