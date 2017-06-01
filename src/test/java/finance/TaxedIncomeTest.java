package finance;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import static finance.TaxedIncome.*;

public class TaxedIncomeTest {


    private TaxedIncome taxedIncome;

    private static double zeroTaxIncomeYear0;
    private static double lowerTaxIncomeYear1;
    private static double higherTaxIncomeYear2;
    private static double additionalTaxIncomeYear3;

    @BeforeClass
    public static void setUpClass(){
        zeroTaxIncomeYear0 = ZERO_TAX_THRESHOLD / 2d;

        lowerTaxIncomeYear1 = ZERO_TAX_THRESHOLD
                + ((LOWER_TAX_THRESHOLD - ZERO_TAX_THRESHOLD)/2d);

        higherTaxIncomeYear2 = LOWER_TAX_THRESHOLD
                + ((HIGHER_TAX_THRESHOLD - LOWER_TAX_THRESHOLD)/2d);

        additionalTaxIncomeYear3 = HIGHER_TAX_THRESHOLD * 2d;
    }

    @Before
    public void setUp() throws Exception {
        taxedIncome = new TaxedIncome(
                new double[]{
                        zeroTaxIncomeYear0,
                        lowerTaxIncomeYear1,
                        higherTaxIncomeYear2,
                        additionalTaxIncomeYear3
                }
        );
    }

    @After
    public void tearDown() throws Exception {
        taxedIncome = null;
    }

    @Test
    public void getTaxPaidInYear0() throws Exception {
        double expected = 0;
        double actual = taxedIncome.getTaxPaid(0);
        assertEquals("Tax paid below personal allowance:",expected, actual, 0);
    }

    @Test
    public void getTaxedIncomeInYear0() throws Exception {
        double expected = zeroTaxIncomeYear0;
        double actual = taxedIncome.getTaxedIncome(0);
        assertEquals("Taxed income below personal allowance:", expected, actual, 0);
    }

    @Test
    public void getTaxPaidInYear1() throws Exception {
        double expected = (LOWER_TAX_THRESHOLD - lowerTaxIncomeYear1) * LOWER_TAX_RATE;
        double actual = taxedIncome.getTaxPaid(1);
        assertEquals("Taxed paid in lower tax bracket:", expected, actual, 0);
    }

    @Test
    public void getTaxedIncomeInYear1() throws Exception {
        double expected = ZERO_TAX_THRESHOLD
                + ((LOWER_TAX_THRESHOLD - lowerTaxIncomeYear1) * (1 - LOWER_TAX_RATE));
        double actual = taxedIncome.getTaxedIncome(1);
        assertEquals("Taxed income in lower tax bracket:", expected, actual, 0);
    }

    @Test
    public void getTaxPaidInYear2() throws Exception {
        double expected = ((LOWER_TAX_THRESHOLD - ZERO_TAX_THRESHOLD) * LOWER_TAX_RATE)
                + ((higherTaxIncomeYear2 - LOWER_TAX_THRESHOLD) * HIGHER_TAX_RATE);
        double actual = taxedIncome.getTaxPaid(2);
        assertEquals("Tax paid in higher tax bracket:", expected, actual, 0);
    }

    @Test
    public void getTaxedIncomeInYear2() throws Exception {
        double expected = ZERO_TAX_THRESHOLD
                + ((LOWER_TAX_THRESHOLD - ZERO_TAX_THRESHOLD) * (1 - LOWER_TAX_RATE))
                + ((higherTaxIncomeYear2 - LOWER_TAX_THRESHOLD) * (1 - HIGHER_TAX_RATE));
        double actual = taxedIncome.getTaxedIncome(2);
        assertEquals("Taxed income in higher tax bracket:", expected, actual, 0);
    }

    @Test
    public void getTaxedPaidInYear3() throws Exception {
        double expected = ((LOWER_TAX_THRESHOLD - ZERO_TAX_THRESHOLD) * LOWER_TAX_RATE)
                + ((HIGHER_TAX_THRESHOLD - LOWER_TAX_THRESHOLD) * HIGHER_TAX_RATE)
                + ((additionalTaxIncomeYear3 - HIGHER_TAX_THRESHOLD) * ADDITIONAL_TAX_RATE);
        double actual = taxedIncome.getTaxPaid(3);
        assertEquals("Tax paid in additional tax bracket:", expected, actual, 0);
    }

    @Test
    public void getTaxedIncomeInYear3() throws Exception {
        double expected = ZERO_TAX_THRESHOLD
                + ((LOWER_TAX_THRESHOLD - ZERO_TAX_THRESHOLD) * (1 - LOWER_TAX_RATE))
                + ((HIGHER_TAX_THRESHOLD - LOWER_TAX_THRESHOLD) * (1 - HIGHER_TAX_RATE))
                + ((additionalTaxIncomeYear3 - HIGHER_TAX_THRESHOLD)* (1 - ADDITIONAL_TAX_RATE));
        double actual = taxedIncome.getTaxedIncome(3);
        assertEquals("Taxed income in additional tax bracket:", expected, actual, 0);

    }

    // TODO: 01/06/2017 add test cases for double[] getTaxedIncome()
}