package ua.org.nalabs.javalessons.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ua.org.nalabs.javalessons.javafx.Main;

public class LoginController {

    private String loginName = "root";
    private String password  = "toor";
    private Main main;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public LoginController() {
    }

    @FXML
    Button loginButton;

    @FXML
    Button cancelButton;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    public void handleLogin() {
            if(loginField.getText().trim().equals(loginName) && passwordField.getText().equals(password)) {
                main.authenticationOK();
            }
    }

    @FXML
    public void handleCancel() {
            System.exit(0);
    }


}
