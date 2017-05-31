package finance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;
import static finance.GraduateDebt.*;

public class GraduateDebtTest {

    private GraduateDebt graduateDebt;
    private final double initialDebt = 45000;

    @Before
    public void setUp() throws Exception {
        graduateDebt = new GraduateDebt(initialDebt);
    }

    @After
    public void tearDown() throws Exception {
        graduateDebt = null;
    }

    @Test
    public void isActive_start() throws Exception {
        assertTrue(graduateDebt.isActive());
    }

    @Test
    public void isActive_debtPaid() throws Exception {
        graduateDebt.makeRepayment(initialDebt);
        assertFalse("Debt inactive by repayment:", graduateDebt.isActive());
    }

    @Test
    public void isActive_debtWrittenOff() throws Exception {
        for(int year = 0; year < GraduateDebt.MAX_AGE; year++){
            graduateDebt.accumulateInterest(0);
        }
        assertFalse("Debt inactive by write off:", graduateDebt.isActive());
    }

    @Test
    public void getYear_start() throws Exception {
        assertEquals("Year at start:", 0, graduateDebt.getYear());
    }

    @Test
    public void getYear_belowMaxAge() throws Exception {
        int year = new Random().nextInt(MAX_AGE - 1);
        for (int i = 0; i < year; i++){
            graduateDebt.accumulateInterest(0);
        }
        int expected = year;
        int actual = graduateDebt.getYear();
        assertEquals("Year of debt below max age:", expected, actual);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void accumulateInterest_aboveMaxAge() throws Exception {
        for (int i = 0; i < MAX_AGE + 1; i++){
            graduateDebt.accumulateInterest(0);
        }
    }

    @Test (expected = UnsupportedOperationException.class)
    public void  accumulateInterest_negative() throws Exception {
        graduateDebt.accumulateInterest(-1);
    }

    @Test
    public void interestRate_belowRepayment() throws Exception {
        double expected = RPI;
        double actual = interestRate(REPAYMENT_THRESHOLD - 100);
        assertEquals("Min interest rate:", expected, actual, 0);
    }

    @Test
    public void interestRate_maxInterest() throws Exception {
        double maxInterestIncome = MAX_INTEREST_THRESHOLD * 1.5;
        double expectedRate = RPI + MAX_INTEREST_RATE;
        double actualRate = interestRate(maxInterestIncome);
        assertEquals("Max interest rate:", expectedRate, actualRate, 0);
    }

    @Test
    public void interestRate_sliding() throws Exception {
        double slidingInterestIncome = REPAYMENT_THRESHOLD
                + ((MAX_INTEREST_THRESHOLD - REPAYMENT_THRESHOLD) / 2d);

        double expectedRate = RPI + (MAX_INTEREST_RATE *
                ((slidingInterestIncome - REPAYMENT_THRESHOLD)
                / (MAX_INTEREST_THRESHOLD - REPAYMENT_THRESHOLD)));

        double actualRate = interestRate(slidingInterestIncome);
        assertEquals("Sliding interest rate:", expectedRate, actualRate, 0);
    }
}