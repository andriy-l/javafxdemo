package ua.org.nalabs.javalessons.javafx.repository;

import javafx.collections.ObservableList;
import ua.org.nalabs.javalessons.javafx.model.Person;

public interface PersonRepository {
    ObservableList<Person> getPersonObservableList();
}
