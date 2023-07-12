import org.checkerframework.checker.units.qual.A;

import java.util.Arrays;

import static java.lang.System.arraycopy;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /** Java's Sorting Algorithm. Java uses Quicksort for ints. */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array == null || k == 0 || k == 1) {
                return;
            }
            for (int i = 1; i < k; i++) {
                int j, x = array[i];
                for (j = i - 1; j >= 0; j--) {
                    if (array[j] <= x) {
                        break;
                    }
                    array[j + 1] = array[j];
                }
                array[j + 1] = x;
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array == null || k == 0 || k == 1) {
                return;
            }
            for (int i = 0; i < k; i++) {
                int curr = array[i], ndx = i;
                for (int j = i + 1; j < k; j++) {
                    if (array[j] < curr) {
                        curr = array[j];
                        ndx = j;
                    }
                }
                array[ndx] = array[i];
                array[i] = curr;
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array == null || k == 0 || k == 1) {
                return;
            }
            sort(array, 0, k);
        }

        public void sort(int[] A, int ndx, int k) {
            if (ndx == k - 1) {
                return;
            }

            int medium = (ndx + k) / 2;
            sort(A, ndx, medium);
            sort(A, medium, k);

            for (int i = medium; i < k; i++) {
                int j, curr = A[i];
                for (j = i - 1; j >= ndx; j--) {
                    if (A[j] <= curr) {
                        break;
                    }
                    A[j + 1] = A[j];
                }
                A[j + 1] = curr;
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array == null || k == 0 || k == 1) {
                return;
            }

            int max = array[0];
            for (int i = 1; i < k; i++) {
                max = Math.max(max, array[i]);
            }

            int[] count = new int[max + 1];
            for (int i = 0; i < k; i++) {
                count[array[i]]++;
            }

            int ndx = 0;
            int[] output = new int[k];
            for (int i = 0; i < max + 1; i++) {
                for(; count[i] != 0; ndx++, count[i]--) {
                    output[ndx] = i;
                }
            }
            arraycopy(output, 0, array, 0 , k);
        }

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array == null || k == 0 || k == 1) {
                return;
            }
            heapify(array, k);
        }

        public void heapify(int[] a, int l) {
            if (a == null || a.length == 0) {
                return;
            }
            int N = a.length;
            for (int k = N - 1; k >= 0 ; k--) {
                for (int p = k, c = 0; 2 * p + 1 < N ; p = c) {
                    if (a[2 * p + 1] < a[p]) {
                        swap(a, p, 2 * p + 1);
                        heapify(a, p);
                    }
                    a.toString();
                    if (p == 0) {
                        break;
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array == null || k == 0 || k == 1) {
                return;
            }
            sort(array, 0, k);
        }

        public void sort(int[] array, int ndx, int k) {
            if (ndx == k) {
                return;
            }

            int pivotloc = ndx;
            for (int i = ndx + 1; i < k; i++) {
                if (array[i] < array[pivotloc]) {
                    for (int j = i; j > pivotloc; j--) {
                        swap(array, j, j-1);
                    }
                    pivotloc++;
                }
            }

            sort(array, ndx, pivotloc);
            sort(array, pivotloc + 1, k);
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            if (a == null || k == 0 || k == 1) {
                return;
            }

            int max = a[0];
            for (int i = 1; i < k; i++) {
                max = Math.max(max, a[i]);
            }

            int x = 2, num = 0;
            while(max > 0) {
                num++;
                max >>= x;
            }

            for (int i = 0; i < num; i++) {
                sort(a, k, x, i);
            }
        }

        private void sort(int[] a, int k, int x, int c) {
            int MASK = (1 << (c + 1) * x) - 1;
            int[] counts = new int[(int) Math.pow(2, x)];
            int[] output = new int[k];
            for (int i = 0; i < k; i++) {
                int ndx = (a[i] & MASK) >> (x * c);
                counts[ndx]++;
            }
            for (int i = 0; i < counts.length - 1; i++) {
                counts[i + 1] += counts[i];
            }
            for (int i = k - 1; i >= 0; i--) {
                int ndx = (a[i] & MASK) >> (x * c);
                output[counts[ndx]-- -1] = a[i];
            }
            arraycopy(output, 0, a, 0, k);
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            if (a == null || k == 0 || k == 1) {
                return;
            }

            int max = a[0];
            for (int i = 1; i < k; i++) {
                max = Math.max(max, a[i]);
            }

            int x = 2, num = 0;
            while(max > 0) {
                num++;
                max >>= x;
            }

            sort(a, 0, k, x, num--);
        }


        private void sort(int[] a, int j, int k, int x, int num) {
            if (num < 0 || j == k || j == k - 1) {
                return;
            }

            int MASK = (1 << (num + 1) * x) - 1;
            int[] counts = new int[(int) Math.pow(2, x)];
            int[] output = new int[k - j];
            for (int i = j; i < k; i++) {
                int h = (a[i] & MASK) >> (x * num);
                counts[h]++;
            }

            for (int i = 0; i < counts.length-1; i++) {
                counts[i + 1] += counts[i];
            }

            for (int i = k - 1; i >= j; i--) {
                int h = (a[i] & MASK) >> (x * num);
                output[counts[h]-- -1] = a[i];
            }
            arraycopy(output, 0, a, j, k-j);

            for (int i = 0; i < counts.length; i++) {
                if (i == counts.length - 1) {
                    sort(a, counts[i] + j, k, x, num -1);
                } else {
                    sort(a, counts[i] + j, counts[i + 1] + j, x, num -1);
                }
            }
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
