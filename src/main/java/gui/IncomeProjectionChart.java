package gui;

import finance.GraduateIncome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IncomeProjectionChart extends BarChart<String, Number> {

    private static final int SPAN = 30;

    private static final double MIN_INCOME = 0;
    private static final double MAX_INCOME = 200000;
    private static final double INTERVAL = 20000;

    IncomeProjectionChart(double width, double height){
        super(
                new CategoryAxis(
                        FXCollections.observableList(
                                IntStream.range(0, SPAN)
                                .boxed()
                                .map(String::valueOf)
                                .collect(Collectors.toList())
                        )
                ),
                new NumberAxis(MIN_INCOME, MAX_INCOME, INTERVAL)
        );

        setPrefWidth(width);
        setPrefHeight(height);

        getXAxis().setLabel("Time / Years");
        getYAxis().setLabel("Earnings / GBP");

        setTitle("Projection");

        setVerticalGridLinesVisible(false);
        setCategoryGap(0);
        setBarGap(0);

        getData().add(0, series("Gross Income", new double[SPAN]));
        getData().add(1, series("Post Tax & Repayments", new double[SPAN]));
        //getData().add(2, series("Post Additional Repayments", new double[SPAN]));

        addEventHandler(MouseEvent.MOUSE_RELEASED, (event -> updateChart()));
    }

    private void updateChart(){

        double DEBT = 45000; // TODO: 01/06/2017 make user defined.

        double[] grossIncome = getGrossIncome();
        GraduateIncome income = new GraduateIncome(DEBT, getGrossIncome(), new double[grossIncome.length]);

        setPostTaxAndRepayment(income.getGraduateIncome());
    }

    void setGrossIncome(int year, double earnings){
        getData().get(0).getData().get(year).setYValue(earnings);
    }

    void setGrossIncome(double[] earnings){
        getData().get(0).setData(dataObservableList(earnings));
    }

    void setPostTaxAndRepayment(int year, double earnings){
        getData().get(1).getData().get(year).setYValue(earnings);
    }

    void setPostTaxAndRepayment(double[] earnings){
        getData().get(1).setData(dataObservableList(earnings));
    }

    void setPostAdditionalRepayment(int year, double earnings){
        getData().get(2).getData().get(year).setYValue(earnings);
    }

    void setPostAdditionalRepayment(double[] earnings){
        getData().get(2).setData(dataObservableList(earnings));
    }

    double[] getGrossIncome(){
        return extract(getData().get(0));
    }

    double getGrossIncome(int year){
        return extract(getData().get(0), year);
    }

    double[] getPostTaxAndRepayment(){
        return extract(getData().get(1));
    }

    double getPostTaxAndRepayment(int year){
        return extract(getData().get(1), year);
    }

    double[] getPostAdditionalRepayments(){
        return extract(getData().get(2));
    }

    double getPostAdditionalRepayments(int year){
        return extract(getData().get(2), year);
    }

    void addChartDrawer(ChartDrawer chartDrawer){
        addEventHandler(MouseEvent.MOUSE_DRAGGED, chartDrawer);
    }

    private static double[] extract(Series<String, Number> series){
        return series.getData()
                .stream()
                .map(XYChart.Data::getYValue)
                .mapToDouble(Number::doubleValue)
                .toArray();
    }

    private static double extract(Series<String, Number> series, int year){
        return series.getData().get(year).getYValue().doubleValue();
    }

    private static Series<String, Number> series(String name, double[] initialData){
        return new Series<>(name, dataObservableList(initialData));
    }

    private static ObservableList<Data<String, Number>> dataObservableList(double[] data){
        return FXCollections.observableList(
                IntStream.range(0, SPAN)
                        .mapToObj(year -> datum(year, data[year]))
                        .collect(Collectors.toList())
        );
    }

    private static Data<String, Number> datum(int year, double earnings){
        return new Data<>(String.valueOf(year), earnings);
    }

    static double getMaxIncome(){
        return MAX_INCOME;
    }

    static double getMinIncome(){
        return MIN_INCOME;
    }

    static int getSpan(){
        return SPAN;
    }

    static boolean inBound(double earning){
        return earning >= MIN_INCOME && earning <= MAX_INCOME;
    }
}
