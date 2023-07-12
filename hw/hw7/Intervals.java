import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/** HW #7, Sorting ranges.
 *  @author
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        intervals.sort(Comparator.comparingInt(arr -> arr[0]));

        int res = 0;
        int max = Integer.MAX_VALUE, min = Integer.MIN_VALUE;

        for (int i = 0; i < intervals.size(); i++) {
            int x = intervals.get(i)[0];
            int y = intervals.get(i)[1];

            if (x > min) {
                res += min - max;
                max = x; min = y;
            } else if (x <= min) {
                if (y > min) {
                    min = y;
                }
            }
        }
        res += min - max;
        return res - 1;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
