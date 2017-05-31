package finance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GrossIncomeTest {

    private GrossIncome income;

    private final double incomeInYear0 = 100;
    private final double incomeInYear1 = 200;
    private final double incomeInYear2 = 300;

    @Before
    public void setUp(){
        income = new GrossIncome(
                new double[]{
                        incomeInYear0,
                        incomeInYear1,
                        incomeInYear2
                }
        );
    }

    @After
    public void tearDown(){
        income = null;
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructor() throws Exception {
        new GrossIncome(new double[]{-1});
    }

    @Test
    public void getIncomeInYear() throws Exception {
        double expected = incomeInYear0;
        double actual = income.getGrossIncomeInYear(0);
        assertEquals(expected, actual, 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getIncomeInYearOutOfBounds1() throws Exception {
        income.getGrossIncomeInYear(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getIncomeInYearOutOfBounds2() throws Exception {
        income.getGrossIncomeInYear(3);
    }

    @Test
    public void getSpan() throws Exception{
        int expected = 3;
        int actual = income.getSpan();
        assertEquals(expected, actual);
    }


}