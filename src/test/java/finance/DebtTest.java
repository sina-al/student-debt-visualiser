package finance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DebtTest {

    private Debt debt;

    private final double initialDebt = 41000;

    @Before
    public void setUp() throws Exception {
        debt = new Debt(initialDebt);
    }

    @After
    public void tearDown() throws Exception {
        debt = null;
    }

    @Test
    public void getPaid() throws Exception {
        double expected = 0d;
        double actual = debt.getPaid();
        assertEquals("No debt paid:", expected, actual, 0);
    }

    @Test
    public void getDebt() throws Exception {
        double expected = initialDebt;
        double actual = debt.getDebt();
        assertEquals("Initial debt:", expected, actual, 0);
    }

    @Test
    public void accumulateInterest() throws Exception {
        double interest = 0.03;
        debt.accumulateInterest(interest);
        double expected = initialDebt * (1 + interest);
        double actual = debt.getDebt();
        assertEquals("Interest accumulated:", expected, actual, 0);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void accumulateInterest_negative() throws Exception {
        debt.accumulateInterest(-0.03);
    }

    @Test
    public void makeRepayment_remaining() throws Exception {
        double repayment = 0.3 * initialDebt;
        debt.makeRepayment(repayment);
        double expected = initialDebt - repayment;
        double actual = debt.getDebt();
        assertEquals("Debt remaining after repayment:", expected, actual, 0);
    }

    @Test
    public void makeRepayment_paid() throws Exception {
        double expectedRepayment = 0.3 * initialDebt;
        debt.makeRepayment(expectedRepayment);
        double actual = debt.getPaid();
        assertEquals("Debt paid after repayment:", expectedRepayment , actual, 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void makeRepayment_negative() throws  Exception {
        debt.makeRepayment(-100);
    }

    @Test
    public void makeRepayment_excessRepayment_paid() throws Exception {
        double excessRepayment = initialDebt * 1.5;
        debt.makeRepayment(excessRepayment);
        double expectedPaid = initialDebt;
        double actualPaid = debt.getPaid();
        assertEquals("Debt paid after excess repayment:", expectedPaid, actualPaid, 0);
    }

    @Test
    public void makeRepayment_excessRepayment_remaining() throws Exception {
        double excessRepayment = initialDebt * 1.5;
        debt.makeRepayment(excessRepayment);
        double expectedRemaining = 0;
        double actualRemaining = debt.getDebt();
        assertEquals(
                "Debt remaining after excess repayment:",
                expectedRemaining,
                actualRemaining,
                0
        );
    }


}