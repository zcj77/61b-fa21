package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

import java.util.ArrayList;
import java.util.Arrays;

/** List problem.  @Jacky Zhao  */
class Lists {
    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs": a maximal strictly ascending sublists,
     *  in the same order as the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *                                      ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        if (L==null){
            return null;
        }
        else if (L.tail == null || L.tail.tail == null && L.head<L.tail.head){
            return IntListList.list(L);
        }

        IntList copy = L;
        int count = 1;
        for (;copy.head<copy.tail.head; copy=copy.tail) {
            count++;
        }

        int[] front = new int[count];
        for (int i = 0; i < front.length ; i++) {
            front[i] = L.head;
            L = L.tail;
        }
        L=copy;

        return new IntListList(IntList.list(front), naturalRuns(L.tail));
    }
}
