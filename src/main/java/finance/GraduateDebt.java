package finance;

class GraduateDebt extends Debt {

    static final double REPAYMENT_THRESHOLD = 21000d;
    static final double MAX_INTEREST_THRESHOLD = 41000d;

    static final double MAX_INTEREST_RATE = 0.03;

    static final double RPI = 0.02;

    static final int MAX_AGE = 30;

    private int year = 0;

    GraduateDebt(double initialDebt){
        super(initialDebt);
    }

    int getYear(){
        return year;
    }

    boolean isActive(){
        return getDebt() > 0 && year < MAX_AGE;
    }

    @Override
    void accumulateInterest(double interest){
        if (this.isActive()) {
            super.accumulateInterest(interest);
            ++year;
        } else {
            throw new UnsupportedOperationException(
                    "Cannot accumulate interest on inactive debt."
            );
        }
    }

    static double interestRate(double preTaxIncome){
        return (preTaxIncome > MAX_INTEREST_THRESHOLD)
                ? RPI + MAX_INTEREST_RATE
                : (preTaxIncome > REPAYMENT_THRESHOLD)
                ? RPI + (MAX_INTEREST_RATE  * (preTaxIncome - REPAYMENT_THRESHOLD)
                / (MAX_INTEREST_THRESHOLD - REPAYMENT_THRESHOLD))
                : RPI;
    }

}
