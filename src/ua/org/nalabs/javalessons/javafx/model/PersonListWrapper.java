package ua.org.nalabs.javalessons.javafx.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Helper class to wrap a list of persons. This is used for saving the
 * list of persons to XML.
 */

// @XmlRootElement визначає ім'я кореневого елемента.
@XmlRootElement
public class PersonListWrapper {
     private List<Person> persons;

    // @XmlElement визначає ім'я елемента, яке нам вказувати не обов'язково.
    @XmlElement
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
