package ua.org.nalabs.javalessons.javafx.model;

import java.io.Serializable;
import java.util.Arrays;

public class User implements Serializable{
    private String userName;
    private char[] password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        return Arrays.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password=" + Arrays.toString(password) +
                '}';
    }

    public User() {
    }

    public User(String userName, char[] password) {

        this.userName = userName;
        this.password = password;
    }
}
