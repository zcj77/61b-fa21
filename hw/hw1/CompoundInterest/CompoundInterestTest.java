import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.
        assertEquals(0, 0); */
        assertEquals(0, CompoundInterest.numYears(2021));
        assertEquals(100, CompoundInterest.numYears(2121));
        assertEquals(1, CompoundInterest.numYears(2022));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2023), tolerance);
        assertEquals(14.641, CompoundInterest.futureValue(10, 21, 2023), tolerance);
        assertEquals(2.5, CompoundInterest.futureValue(10, -50, 2023), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(11.8026496, CompoundInterest.futureValueReal(10, 12, 2023, 3), tolerance);
        assertEquals(642420.7944470324, CompoundInterest.futureValueReal(100000, 8, 2061, 3), tolerance);
        assertEquals(954562.8102329134,  CompoundInterest.futureValueReal(1000000, 8, 2020, 3), tolerance);
    }

    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550,  CompoundInterest.totalSavings(5000, 2023, 10 ), tolerance);
        assertEquals(1340.579,  CompoundInterest.totalSavings(100, 2026, 32 ), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 1;
        assertEquals(14936,  CompoundInterest.totalSavingsReal(5000, 2023, 10, 5 ), tolerance);
        assertEquals(15572,  CompoundInterest.totalSavingsReal(5000, 2023, 10, 3 ), tolerance);
        assertEquals(14936,  CompoundInterest.totalSavingsReal(5000, 2023, 10, 5 ), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
