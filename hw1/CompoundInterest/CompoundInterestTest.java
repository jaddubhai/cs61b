import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(CompoundInterest.numYears(2021), 2);
    }

    @Test
    public void testFutureValue()
    {
        double future = CompoundInterest.futureValue(10, 12, 2021);
        double future2 = CompoundInterest.futureValue(10, -12, 2021);
        double tolerance = 0.01;
        assertEquals(true, (future >= 12.544 - tolerance) && (future <= 12.544 + tolerance));
        assertEquals(true, (future2 >= 7.744 - tolerance) && (future2 <= 7.744 + tolerance));
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        double inflation1 = CompoundInterest.futureValueReal(10, 12, 2021, 3);
        double deflation1 = CompoundInterest.futureValueReal(10, 12, 2021, -3);

        assertEquals(true, (inflation1 >= 11.8026496 - tolerance) && (inflation1 <= 11.8026496 + tolerance));
        assertEquals(true, (deflation1>= 13.3079296 - tolerance) && (deflation1 <= 13.3079296 + tolerance));

    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        double savings1 = CompoundInterest.totalSavings(5000, 2021, 10);
        assertEquals(true, (savings1 >= 16550 - tolerance) && (savings1 <= 16550 + tolerance));


    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        double savings2 = CompoundInterest.totalSavingsReal(5000, 2021, 10, 3);
        assertEquals(true, (savings2 >= 15571.895 - tolerance) && (savings2 <= 15571.895 + tolerance));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
