package finance;

import java.util.stream.DoubleStream;

import static finance.GraduateDebt.REPAYMENT_THRESHOLD;
import static finance.GraduateDebt.interestRate;

public class GraduateIncome extends TaxedIncome{

    static final double REPAYMENT_RATE = 0.09;

    private final double initialDebt;
    private double[] netIncome;

    public GraduateIncome(double initialDebt, double[] incomeHistory) {
        super(incomeHistory);
        if(incomeHistory.length != GraduateDebt.MAX_AGE){
            throw new IllegalArgumentException(
                    "Income history must be of length "
                    + GraduateDebt.MAX_AGE + "."
            );
        }
        this.initialDebt = initialDebt;
        netIncome = projectNetIncome(new double[GraduateDebt.MAX_AGE]);
    }

    private static double mandatoryRepayment(double preTaxIncome){
        return (preTaxIncome > REPAYMENT_THRESHOLD)
                ? REPAYMENT_RATE * (preTaxIncome - REPAYMENT_THRESHOLD)
                : 0.0;
    }

    private double[] projectNetIncome(double[] additionalRepayment){
        if(additionalRepayment.length != GraduateDebt.MAX_AGE){
            throw new IllegalArgumentException(
                    "Additional repayments must be of length "
                    + GraduateDebt.MAX_AGE + "."
            );
        }
        if(DoubleStream.of(additionalRepayment).anyMatch(d -> d < 0)) {
            throw new UnsupportedOperationException(
                    "Additional repayment may not be negative."
            );
        }

        GraduateDebt debt = new GraduateDebt(initialDebt);
        double[] netIncome = new double[GraduateDebt.MAX_AGE];

        for (int year = 0; year < netIncome.length; year++) {

            double totalRepayment =
                    mandatoryRepayment(getGrossIncomeInYear(year))
                    + additionalRepayment[year];

            netIncome[year] = getTaxedIncome(year)
                    - totalRepayment;

            if (netIncome[year] < 0) {
                throw new UnsupportedOperationException("Cannot repay more than net earnings.");
            }

            debt.makeRepayment(totalRepayment);

            if(debt.isActive()) {
                debt.accumulateInterest(interestRate(getGrossIncomeInYear(year)));
            }
        }

        return netIncome;
    }

    public void setAdditionalRepayments(double[] additionalRepayments){
        netIncome = projectNetIncome(additionalRepayments);
    }

    double getGraduateIncome(int year){
        try {
            return netIncome[year];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(
                    "Income not specified in year " + year + ". "
                            + "Choose year between 0 and " + netIncome.length + "."
            );
        }
    }

    public double[] getGraduateIncome(){
        return get(this::getGraduateIncome);
    }

    double getRepaymentMade(int year){
        return getTaxedIncome(year) - netIncome[year];
    }

}
