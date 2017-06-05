package gui;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;

public class DebtVisualiser extends HBox {

    private static final double DEFAULT_DEBT = 41000;

    public DebtVisualiser(){

        TextField totalPaid = new TextField();
        totalPaid.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        totalPaid.setText(String.valueOf(0));
        totalPaid.setEditable(false);

        TextField debtInputField = new TextField();
        debtInputField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        debtInputField.setText(String.valueOf(DEFAULT_DEBT));

        IncomeProjectionChart barChart = new IncomeProjectionChart(600, 400);
        addEventHandler(MouseEvent.MOUSE_DRAGGED, new GrossIncomeDrawer(barChart));

        DebtPieChart pieChart = new DebtPieChart(barChart, debtInputField);

        addEventHandler(MouseEvent.MOUSE_RELEASED, event ->
            totalPaid.setText(String.valueOf(pieChart.getPaid())
        ));

        addEventHandler(MouseEvent.MOUSE_RELEASED, event -> pieChart.updateChart());

        getChildren().add(barChart);
        getChildren().add(new VBox(pieChart, numericBox(debtInputField, totalPaid)));
    }

    private static HBox numericBox(TextField debtInput, TextField paidOutput){
        return new HBox(
                new Text("Starting Debt: "),
                debtInput,
                new Text("Total Paid: "),
                paidOutput
        );
    }
}
