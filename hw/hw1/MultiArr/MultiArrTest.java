import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] a = {{1,2,3},
                            {4,5,6}};
        assertEquals(6, MultiArr.maxValue(  a ));
        a[0][0] = 100;
        assertEquals(100, MultiArr.maxValue(  a ));
    }

    @Test
    public void testAllRowSums() {
        assertArrayEquals(new int[] {3, 3, 3}, MultiArr.allRowSums(new int[][]{   {1,1,1}, {1,1,1}, {1,1,1} }  ));
        assertArrayEquals(new int[]{3,3,4}, MultiArr.allRowSums(new int[][]{   {1,1,1}, {1,1,1}, {1,1,2} }  ));
        assertArrayEquals(new int[]{22, 19}, MultiArr.allRowSums(new int[][]{{6,3,7,1,5},{2, 7, 0, 2, 8}}));
    }

    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
