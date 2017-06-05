package gui;

import finance.GraduateIncome;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextField;

public class DebtPieChart extends PieChart {

    private final TextField debtInputField;
    private final IncomeProjectionChart chart;

    DebtPieChart(IncomeProjectionChart chart, TextField debtInputField){

        this.chart = chart;
        this.debtInputField = debtInputField;

        double debt = (Double)(debtInputField
                .getTextFormatter())
                .getValue();

        GraduateIncome income = new GraduateIncome(
                debt,
                chart.getGrossIncome(),
                new double[30] // modify this later
        );


        getData().add(new Data("Starting Debt", debt));
        getData().add(new Data("Total Paid", income.getDebtPaid()));
        getData().add(new Data("Written off", income.getWrittenOff()));

    }

    void updateChart(){

        GraduateIncome income = new GraduateIncome(
                (Double)debtInputField.getTextFormatter().getValue(),
                chart.getGrossIncome(),
                new double[30] // modify for additional repayments
        );

        getData().get(1).setPieValue(income.getDebtPaid());
        getData().get(2).setPieValue(income.getWrittenOff());

    }

    public double getPaid(){
        return getData().get(1).getPieValue();
    }

    public double getWrittenOff(){
        return getData().get(2).getPieValue();
    }
}
