package ua.org.nalabs.javalessons.javafx.repository;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.org.nalabs.javalessons.javafx.model.Person;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonRepositoryCollection implements PersonRepository {
    private ObservableList<Person> personObservableList;

    public PersonRepositoryCollection() {
        this.personObservableList = FXCollections.observableArrayList();
        this.personObservableList.addAll(
        new Person("Hans", "Muster", "Baker St.", 46018, "Ternopil", LocalDateTime.of(1999, 2, 21, 10, 00, 20) ) ,
        new Person("Ruth", "Mueller", "Ruska Street", 46000, "Ternopil", LocalDateTime.of(1980, 2, 21, 10, 00, 20)),
        new Person("Heinz", "Kurz", "Ruska Street", 46000, "Ternopil", LocalDateTime.of(1990, 2, 21, 10, 00, 20)),
        new Person("Cornelia", "Meier", "Ruska Street", 46000, "Ternopil", LocalDateTime.of(1970, 2, 21, 10, 00, 20)),
        new Person("Werner", "Meyer", "Ruska Street", 46000, "Ternopil", LocalDateTime.of(1960, 2, 21, 5, 30, 20)),
        new Person("Lydia", "Kunz", "Ruska Street", 46000, "Ternopil", LocalDateTime.of(1991, 2, 21, 17, 40, 20)),
        new Person("Anna", "Best", "Ruska Street", 46000, "Ternopil", LocalDateTime.of(1995, 2, 21, 12, 30, 20)),
        new Person("Stefan", "Meier", "Ruska Street", 46000, "Ternopil", LocalDateTime.of(1996, 2, 21, 14, 05, 20)),
        new Person("Martin", "Mueller", "Ruska Street", 46000, "Ternopil", LocalDateTime.of(1997, 2, 21, 12, 20, 20))
        );
    }

    public ObservableList<Person> getPersonObservableList() {
        return personObservableList;
    }
}
