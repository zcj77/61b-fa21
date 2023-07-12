package arrays;

import lists.IntList;
import lists.IntListList;
//import lists.Lists;
import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    @Test
    public void catenateTest(){
        assertArrayEquals(new int[]{1}, Arrays.catenate(new int[]{1}, null));
        assertArrayEquals(new int[]{1, 2}, Arrays.catenate(new int[]{1}, new int[]{2}));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, Arrays.catenate(new int[]{1, 2}, new int[]{3, 4, 5}));
    }


    @Test
    public void removeTest(){
        assertArrayEquals(null, Arrays.remove(new int[]{1}, 1, 2));
        assertArrayEquals(new int[]{1, 2}, Arrays.remove(new int[]{1, 2}, 1, 0));
        assertArrayEquals(new int[]{1}, Arrays.remove(new int[]{1, 2}, 1, 1));
        assertArrayEquals(new int[]{1, 4}, Arrays.remove(new int[]{1, 2, 3, 4}, 1, 2));
        assertArrayEquals(new int[]{2, 3, 4}, Arrays.remove(new int[]{1, 2, 3, 4}, 0, 1));
    }


    @Test
    public void naturalsTest(){
        assertArrayEquals(new int[][]{{1}}, Arrays.naturalRuns(new int[]{1}));
        assertArrayEquals(new int[][]{{1, 2}}, Arrays.naturalRuns(new int[]{1, 2}));
        assertArrayEquals(new int[][]{{1, 3, 7}, {5}, {4, 11}},
                Arrays.naturalRuns(new int[]{1, 3, 7, 5, 4, 11}));
        int[][] expected = {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        assertArrayEquals("Hahahaha try again!", expected,
                Arrays.naturalRuns(new int[]{1, 3, 7, 5, 4, 6, 9, 10, 10, 11}));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
