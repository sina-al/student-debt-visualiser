package gui;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.chart.Axis;
import javafx.scene.input.MouseEvent;

import static gui.IncomeProjectionChart.inBound;


abstract class ChartDrawer implements EventHandler<MouseEvent> {

    protected final IncomeProjectionChart chart;
    private final Axis<Number> yAxis;
    private final Axis<String> xAxis;

    ChartDrawer(IncomeProjectionChart chart) {
        this.chart = chart;
        xAxis = chart.getXAxis();
        yAxis = chart.getYAxis();
    }

    double[] chartValues(MouseEvent event) throws UnsupportedOperationException {

        Point2D values = new Point2D(event.getSceneX(), event.getSceneY());

        Number y = yAxis.getValueForDisplay(yAxis.sceneToLocal(values).getY());
        String x = xAxis.getValueForDisplay(xAxis.sceneToLocal(values).getX());

        if (inBound(y.doubleValue()) && x != null) {
            return new double[]{Double.valueOf(x), y.doubleValue()};
        } else {
            throw new UnsupportedOperationException("Mouse not on chart quadrant.");
        }
    }


}