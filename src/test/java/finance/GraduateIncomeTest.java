package finance;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static finance.GraduateDebt.MAX_AGE;
import static finance.GraduateDebt.REPAYMENT_THRESHOLD;
import static finance.GraduateIncome.REPAYMENT_RATE;
import static finance.TaxedIncome.*;
import static org.junit.Assert.assertEquals;

public class GraduateIncomeTest {

    private GraduateIncome income;

    private static double[] preTax;
    private static final double debt = 45000;

    @BeforeClass
    public static void setUpClass() throws Exception {
        preTax = new Random(0)
                        .doubles(MAX_AGE, 0, 200000)
                        .toArray();
    }

    @Before
    public void setUp() throws Exception {
        income = new GraduateIncome(debt, preTax);
    }

    @After
    public void tearDown() throws Exception {
        income = null;
    }

    private static double taxDeduction(double preTax){
        return (preTax < ZERO_TAX_THRESHOLD)
                ? 0.0
                : (preTax < LOWER_TAX_THRESHOLD)
                ? (preTax - ZERO_TAX_THRESHOLD) * LOWER_TAX_RATE
                : (preTax < HIGHER_TAX_THRESHOLD)
                ? ((preTax - LOWER_TAX_THRESHOLD) * HIGHER_TAX_RATE)
                + ((LOWER_TAX_THRESHOLD - ZERO_TAX_THRESHOLD) * LOWER_TAX_RATE)
                : ((preTax - HIGHER_TAX_THRESHOLD) * ADDITIONAL_TAX_RATE)
                + ((HIGHER_TAX_THRESHOLD - LOWER_TAX_THRESHOLD) * HIGHER_TAX_RATE)
                + ((LOWER_TAX_THRESHOLD - ZERO_TAX_THRESHOLD) * LOWER_TAX_RATE);
    }

    private static double debtRepayment(double preTax){
        return (preTax > REPAYMENT_THRESHOLD)
                ? (preTax - REPAYMENT_THRESHOLD) * REPAYMENT_RATE
                : 0.0;
    }

    private static double mandatoryDeductions(double preTax){
        return taxDeduction(preTax) + debtRepayment(preTax);
    }

    @Test
    public void getRepaymentMadeInYear() throws Exception {
        for(int year = 0; year < preTax.length; year++) {
            double expected = debtRepayment(preTax[year]);
            double actual = income.getRepaymentMadeInYear(year);
            assertEquals("Repayment made in year " + year + ":", expected, actual, 1E-11);
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void getRepaymentMadeInYear_negative() throws Exception {
        income.getRepaymentMadeInYear(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getRepaymentInYear_past() throws Exception {
        income.getRepaymentMadeInYear(MAX_AGE + 1);
    }

    @Test
    public void  getGraduateIncomeInYear() throws Exception {
        for (int year = 0; year < preTax.length; year++) {
            double expected = preTax[year] - mandatoryDeductions(preTax[year]);
            double actual = income.getGraduateIncomeInYear(year);
            assertEquals(
                    "Net graduate income in year " + year + ":",
                    expected,
                    actual,
                    1E-10
            );
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void getGraduateIncomeInYear_negative() throws Exception {
        income.getGraduateIncomeInYear(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getGraduateIncomeInYear_past(){
        income.getGraduateIncomeInYear(MAX_AGE + 1);
    }

    @Test // FIXME: 01/06/2017 revisit this.
    public void setAdditionalRepayments() throws Exception {

        Random random = new Random();

        double[] additionalRepayment = new double[preTax.length];

        for (int year = 0; year < preTax.length; year++){
            additionalRepayment[year] =
                    (preTax[year] - mandatoryDeductions(preTax[year]))
                    * random.nextDouble();
        }

        income.setAdditionalRepayments(additionalRepayment);

        for (int year = 0; year < preTax.length - 2; year++){
            double actual = income.getGraduateIncomeInYear(year);
            double expected = preTax[year]
                    - mandatoryDeductions(preTax[year])
                    - additionalRepayment[year];

            assertEquals(
                    "Post additional repayments in year " + year + ": ",
                    actual,
                    expected,
                    1E-10
            );
        }
    }

    @Test (expected = UnsupportedOperationException.class)
    public void setAdditionalRepayments_overpay(){
        double[] repayments = new double[preTax.length];
        repayments[0] = preTax[0]; // cannot repay pre-tax amount from post-tax earnings
        income.setAdditionalRepayments(repayments);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void setAdditionalRepayments_negative(){
        double[] repayments = new double[preTax.length];
        repayments[0] = -100d; // cannot make negative repayment
        income.setAdditionalRepayments(repayments);
    }

    @Test (expected = IllegalArgumentException.class)
    public void setAdditionalRepayments_excess() {
        income.setAdditionalRepayments(new double[MAX_AGE + 1]);
    }

    @Test (expected = IllegalArgumentException.class)
    public void setAdditionalRepayments_insufficient() {
        income.setAdditionalRepayments(new double[MAX_AGE - 1]);
    }
}