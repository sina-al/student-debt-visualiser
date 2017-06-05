package finance;

import org.junit.*;

import java.util.Random;

import static finance.GraduateDebt.MAX_AGE;
import static finance.GraduateDebt.REPAYMENT_THRESHOLD;
import static finance.GraduateIncome.REPAYMENT_RATE;
import static org.junit.Assert.*;


// TODO: 05/06/2017 needs complete refactor
public class GraduateIncomeTest {

    private static double[] incomeHistory;
    private static double[] additionalRepayments;
    private static final double startingDebt = 45000;

    private GraduateIncome graduateIncome;

    @BeforeClass
    public static void setUpClass() throws  Exception {
        incomeHistory = new double[]{
                18000,18000,18000,18000,18000,
                21000,21000,21000,21000,21000,
                30000,30000,30000,30000,30000,
                38000,38000,38000,38000,38000,
                47000,47000,47000,47000,47000,
                80000,80000,
                120000,120000,
                200000
        };

        additionalRepayments = new double[incomeHistory.length];

        Random random = new Random();
        for (int year = 0; year < additionalRepayments.length; year++) {
            additionalRepayments[year] = random.nextDouble() * 0.5 * incomeHistory[year];
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        additionalRepayments = null;
        incomeHistory = null;
    }

    @Before
    public void setUp() throws Exception {
        graduateIncome = new GraduateIncome(startingDebt, incomeHistory, additionalRepayments);
    }

    @After
    public void tearDown() throws Exception {
        graduateIncome = null;
    }

    private static double tax(double grossIncome){
        // FIXME: 05/06/2017  decouple from TaxedIncome::getTaxPaid()
        double[] preTax = new double[MAX_AGE];
        preTax[0] = grossIncome;
        return new TaxedIncome(preTax).getTaxPaid(0);
    }

    private static double mandatoryRepayment(double grossIncome){
        if(grossIncome < REPAYMENT_THRESHOLD){
            return (grossIncome - REPAYMENT_THRESHOLD) * REPAYMENT_RATE;
        } else {
            return 0.0;
        }
    }

    private static double graduateIncomeInYear(int year) {
        double debt = startingDebt;
        double totalRepayment = 0d;

        double mandatoryRepayment = mandatoryRepayment(incomeHistory[year]);

        // check for and make appropriate debt repayment
        for (int yr = 0; yr < year; yr++) {

            if (debt > 0) {
                debt -= mandatoryRepayment;
                totalRepayment = mandatoryRepayment;
                if (debt > 0) {
                    debt -= additionalRepayments[year];
                    totalRepayment += additionalRepayments[year];
                    if (debt < 0) {
                        totalRepayment += debt;
                    }
                } else {
                    totalRepayment += debt;
                }
            } else {
                totalRepayment = 0d;
            }
        }
        return incomeHistory[year] - tax(incomeHistory[year]) - totalRepayment;
    }

    @Test
    public void getGraduateIncome() throws Exception {
        for(int year = 0; year < MAX_AGE; year++) {
            double actual = graduateIncome.getGraduateIncome(0);
            double expected = graduateIncomeInYear(0);
            assertEquals(
                    "Graduate income in year " + year + ": ",
                    expected,
                    actual,
                    0
            );
        }
    }

    @Test
    public void getRepaymentMade() throws Exception {

    }

}