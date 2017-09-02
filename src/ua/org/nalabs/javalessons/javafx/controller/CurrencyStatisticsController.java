package ua.org.nalabs.javalessons.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import ua.org.nalabs.javalessons.javafx.model.Person;
import ua.org.nalabs.javalessons.javafx.util.CurrencyUtil;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CurrencyStatisticsController {
    @FXML
    private LineChart<String,Number> lineChart;

    @FXML
    private Button refreshButton;

    @FXML
    private DatePicker datePickerFrom;

    private LocalDate from;
    private LocalDate to;

    @FXML
    private DatePicker datePickerTo;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private AnchorPane graphAnchoPane;

    private Set<Currency> currencies;

    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private XYChart.Series series;



    public void setRefreshButton(Button refreshButton) {
        this.refreshButton = refreshButton;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        ObservableList<String> curr = FXCollections.observableList(CurrencyUtil.getAllCurrencies()) ;
        choiceBox.setItems(curr);
        choiceBox.getSelectionModel().select("USD");
        refreshButton.setDefaultButton(true);

    }

    @FXML
    public void handleOnFromDate() {
            this.from = datePickerFrom.getValue();
        System.out.println(this.from);
    }

    @FXML
    public void handleOnRefreshButton() {

        if((this.from == null) || (this.to == null)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dates are not selected");
            alert.setHeaderText("Dates are not selected");
            alert.setContentText("Choose start and end date");
            alert.showAndWait();
        } else if( !this.from.isBefore(this.to) ) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error values are selected");
            alert.setHeaderText("Error values are selected");
            alert.setContentText("Error values are selected");
            alert.showAndWait();
        } else {
            // check only for before
            // TODO add other checks
            series = new XYChart.Series();
            series.setName(choiceBox.getValue());
            long daysBetween = ChronoUnit.DAYS.between(datePickerFrom.getValue(), datePickerTo.getValue());
            System.out.println("days between " + daysBetween);

            LocalDate datePosition = this.from;
            while (datePosition.isBefore(this.to)) {
            series.getData().add(
                    new XYChart.Data<String, Number>(datePosition.toString(), CurrencyUtil.getCurrencyForDate(choiceBox.getValue(), datePosition) ));
                datePosition = datePosition.plusDays(1);
            }


            lineChart.getData().add(series);

            graphAnchoPane.requestLayout();
        }


    }

    @FXML
    public void handleOnToDate() {

        this.to = datePickerTo.getValue();
        System.out.println(this.to);
    }


        /**
         * Sets the currencies to show the statistics for.
         *
         * @param currencies
         */
    public void setCurrencies(Set<Currency> currencies) {
        this.currencies = currencies;
    }




}
