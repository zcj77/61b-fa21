package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** @author Jacky Zhao  */

public class ListsTest {
    @Test
    public void ListsTest(){
        assertEquals( IntListList.list(new int[][] {{1}}), Lists.naturalRuns(IntList.list(1)));
        assertEquals( IntListList.list(new int[][] {{1, 2}}), Lists.naturalRuns(IntList.list(1, 2)));
        assertEquals( IntListList.list(new int[][] {{3}, {2}}), Lists.naturalRuns(IntList.list(3, 2)));
        IntList L = IntList.list(1, 3, 7, 5, 4, 11);
        assertTrue("Wrong contents", L.equals(IntList.list(1, 3, 7, 5, 4, 11)));
        assertEquals("Hahahaha try again!", IntListList.list(new int[][] {{1, 3, 7}, {5}, {4, 11}}), Lists.naturalRuns(L));

        L = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        IntList f = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        IntList s = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        IntList t = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);

        IntListList exp = IntListList.list(f, s, t);
        assertTrue("Wrong contents", L.equals(IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11)));
        int[][] expected = {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        assertEquals("Hahahaha try again!", exp, Lists.naturalRuns(L));
    }

    // It might initially seem daunting to try to set up IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just few lines of code!
    // Make note of the IntListList.list method that takes as input a 2D array.
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
