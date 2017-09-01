package ua.org.nalabs.javalessons.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import ua.org.nalabs.javalessons.javafx.model.Person;

import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BirthdayStatisticsAsPieChartController {
    @FXML
    private PieChart pieChart;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    /**
     * Sets the persons to show the statistics for.
     *
     * @param persons
     */
    public void setPersonData(List<Person> persons) {
        // Count the number of people having their birthday in a specific month.
        int[] monthCounter = new int[12];
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Person p : persons) {
            int month = p.getBirthday().getMonthValue() - 1;
            monthCounter[month]++;
        }

        // Create a XYChart.Data object for each month. Add it to the series.
        for (int i = 0; i < monthCounter.length; i++) {
            pieChartData.add(new PieChart.Data(Month.of(i+1) + "", monthCounter[i]));
        }

        pieChart.getData().addAll(pieChartData);
    }
}
