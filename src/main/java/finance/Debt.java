package finance;

class Debt {

    private double paid;
    private double debt;

    Debt(double initialDebt){
        if (initialDebt < 0) {
            throw new IllegalArgumentException("Cannot have negative debt.");
        }
        debt = initialDebt;
    }

    double getPaid(){
        return paid;
    }

    double getDebt(){
        return debt;
    }

    void accumulateInterest(double interestRate){
        if(interestRate < 0){
            throw new UnsupportedOperationException("Cannot accumulate negative interest.");
        }
        debt *= (1.0 + interestRate);
    }

    double makeRepayment(double repayment){
        if (repayment < 0){
            throw new IllegalArgumentException("Cannot make negative repayment.");
        }
        if (debt > repayment){
            debt -= repayment;
            paid += repayment;
        } else {
            repayment = debt;
            paid += repayment;
            debt = 0.0;
        }
        return repayment;
    }

}
