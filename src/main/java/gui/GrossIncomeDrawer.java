package gui;

import javafx.scene.input.MouseEvent;

class GrossIncomeDrawer extends ChartDrawer {

    GrossIncomeDrawer(IncomeProjectionChart chart){
        super(chart);
    }

    @Override
    public void handle(MouseEvent event){
        try {
            double[] values = chartValues(event);
            chart.setGrossIncome((int) values[0], values[1]);
        } catch (UnsupportedOperationException ex){
            return;
        }
    }
}
