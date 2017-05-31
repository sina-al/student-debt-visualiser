package finance;

import java.util.Arrays;
import java.util.stream.DoubleStream;

class GrossIncome {

    private double[] incomeHistory;

    GrossIncome(double[] incomeHistory){
        if(DoubleStream.of(incomeHistory).anyMatch(d -> d < 0)){
            throw new IllegalArgumentException("Cannot have negative income.");
        }
        this.incomeHistory = Arrays.copyOf(incomeHistory, incomeHistory.length);
    }

    double getGrossIncomeInYear(int year) {
        try {
            return incomeHistory[year];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(
                    "Income not specified in year " + year + ". "
                            + "Choose year between 0 and " + incomeHistory.length + "."
            );
        }
    }

    int getSpan(){
        return incomeHistory.length;
    }
}
