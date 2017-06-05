package finance;

import java.util.stream.DoubleStream;

import static finance.GraduateDebt.REPAYMENT_THRESHOLD;
import static finance.GraduateDebt.interestRate;

public class GraduateIncome extends TaxedIncome{

    static final double REPAYMENT_RATE = 0.09;

    private final double initialDebt;
    private double[] netIncome;

    private GraduateDebt debt;
    private int yearRepaid = 30;

    public GraduateIncome(
            double initialDebt,
            double[] incomeHistory,
            double[] additionalRepayments
    ) {
        super(incomeHistory);
        if(incomeHistory.length != GraduateDebt.MAX_AGE){
            throw new IllegalArgumentException(
                    "Income history must be of length "
                    + GraduateDebt.MAX_AGE + "."
            );
        }
        this.initialDebt = initialDebt;
        netIncome = projectNetIncome(additionalRepayments);
    }

    private static double mandatoryRepayment(double preTaxIncome){
        return (preTaxIncome > REPAYMENT_THRESHOLD)
                ? REPAYMENT_RATE * (preTaxIncome - REPAYMENT_THRESHOLD)
                : 0.0;
    }

    // FIXME: 01/06/2017 bug in debt interest. used  pretax instead of interest(pretax)
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

        debt = new GraduateDebt(initialDebt);
        double[] netIncome = getTaxedIncome();


        boolean debtRepaid = false;

        for (int year = 0; year < netIncome.length; year++) {

            double grossIncome = getGrossIncome(year);
            double repayment = mandatoryRepayment(grossIncome);

            if(debt.isActive()){
                debt.accumulateInterest(interestRate(grossIncome));
                netIncome[year] -= debt.makeRepayment(repayment);

                if(debt.getDebt() > 0){
                    netIncome[year] -= debt.makeRepayment(additionalRepayment[year]);
                } else if (debtRepaid) {
                    debtRepaid = true;
                    yearRepaid = year;
                }
            }

            if (netIncome[year] < 0) {
                throw new UnsupportedOperationException("Cannot repay more than net earnings.");
            }
        }
        return netIncome;
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

    public double getRepaymentMade(int year){
        return getTaxedIncome(year) - netIncome[year];
    }

    public int getYearRepaid(){
        return yearRepaid;
    }

    public double getDebtPaid(){
        return debt.getPaid();
    }

    public double getWrittenOff(){
        return debt.getDebt();
    }

}
