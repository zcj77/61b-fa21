import org.junit.Test;
import static org.junit.Assert.*;

/** Tests of BitExercise
 *  @author Zoe Plaxco
 */
public class BitExerciseTest {

    @Test
    public void testLastBit() {
        int four = BitExercise.lastBit(100);
        assertEquals(4, four);
        assertEquals(1, BitExercise.lastBit(0001) );
        assertEquals(1, BitExercise.lastBit(1001) );
        assertEquals(2, BitExercise.lastBit(1010) );
        assertEquals(1, BitExercise.lastBit(0111) );
    }

    @Test
    public void testPowerOfTwo() {
        boolean powOfTwo = BitExercise.powerOfTwo(32);
        assertTrue(powOfTwo);
        boolean notPower = BitExercise.powerOfTwo(7);
        assertFalse(notPower);
    }

    @Test
    public void testAbsolute() {
        int hundred = BitExercise.absolute(100);
        assertEquals(100, hundred);
        int negative = BitExercise.absolute(-150);
        assertEquals(150, negative);
        int zero = BitExercise.absolute(0);
        assertEquals(0,zero);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(BitExerciseTest.class));
    }
}

