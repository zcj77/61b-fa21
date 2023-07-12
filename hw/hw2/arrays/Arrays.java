package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful in testing your answers. */
/** HW #2 */

import lists.IntList;
import lists.IntListList;

import java.util.ArrayList;

/** Array utilities.  @author*/
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        if (A==null){
            return B;
        }
        else if (B==null){
            return A;
        }
        int[] res = new int[A.length+B.length];
        System.arraycopy(A, 0, res,0, A.length);
        System.arraycopy(B, 0, res, A.length, B.length);
        return res;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A, beginning with item #START.
     *  If the start + len is out of bounds for our array, you can return null.
     *  Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     *  result should be [0, 3]. */
    static int[] remove(int[] A, int start, int len) {
        if (start+len>A.length){
            return null;
        }
        return catenate(Utils.subarray(A, 0, start), Utils.subarray(A, len+start, A.length-len-start));
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into maximal ascending lists, w/o reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then returns {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        int a_len = A.length;
        if (A.length==0){
            return new int[][]{{}};
        }

        if(A.length==1 || A.length==2 && A[0]<A[1]) {
            return new int[][]{A};
        }

        int res_len = 1;
        for (int i = 0; i<A.length-2; i++) {
            if (A[i] >= A[i+1]) {
                res_len++;
            }
        }

        int[][] res = new int[res_len][A.length];
        for (int res_ndx = 0; res_ndx < res_len; res_ndx++) {
            int len = 1;
            for (int i = 0; i < len; i++) {
                if(A.length==1 || A.length==2 && A[0]<A[1]){
                    res[res_ndx] = A;
                    return res;
                }
                if (A[i] < A[len]) {
                    len++;
                }
                res[res_ndx][i] = A[i];
            }
            res[res_ndx] = remove(res[res_ndx], len, a_len-len);
            A = remove(A, 0, len);
        }
        return res;
    }
}
