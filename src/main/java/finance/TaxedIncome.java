package finance;

class TaxedIncome extends GrossIncome {

    static final double ZERO_TAX_THRESHOLD = 11000d;
    static final double LOWER_TAX_THRESHOLD = 43000d;
    static final double HIGHER_TAX_THRESHOLD = 150000d;

    static final double LOWER_TAX_RATE = 0.2;
    static final double HIGHER_TAX_RATE = 0.4;
    static final double ADDITIONAL_TAX_RATE = 0.45;

    TaxedIncome(double[] preTaxIncome){
        super(preTaxIncome);
    }

    double getTaxPaid(int year){
        return taxDeduction(getGrossIncome(year));
    }

    double getTaxedIncome(int year){
        double preTax = getGrossIncome(year);
        return preTax - taxDeduction(preTax);
    }

    double[] getTaxedIncome(){
        return get(this::getTaxedIncome);
    }

    private static double taxDeduction(double preTaxIncome){
        return (preTaxIncome < ZERO_TAX_THRESHOLD)

                // NO TAX
                ? 0.0
                // ------

                : (preTaxIncome < LOWER_TAX_THRESHOLD)

                // 20p BRACKET----------------------------------------
                ? LOWER_TAX_RATE * (preTaxIncome - ZERO_TAX_THRESHOLD)
                // ---------------------------------------------------

                : (preTaxIncome < HIGHER_TAX_THRESHOLD)

                // 40p BRACKET-----------------------------------------------
                ? (HIGHER_TAX_RATE * (preTaxIncome - LOWER_TAX_THRESHOLD))
                + (LOWER_TAX_RATE * (LOWER_TAX_THRESHOLD - ZERO_TAX_THRESHOLD))
                // ----------------------------------------------------------

                // 45p BRACKET-----------------------------------------------
                : (ADDITIONAL_TAX_RATE * (preTaxIncome - HIGHER_TAX_THRESHOLD))
                + (HIGHER_TAX_RATE * (HIGHER_TAX_THRESHOLD - LOWER_TAX_THRESHOLD))
                + (LOWER_TAX_RATE * (LOWER_TAX_THRESHOLD - ZERO_TAX_THRESHOLD));
                // ----------------------------------------------------------
    }
}
