/** Functions to increment and sum the elements of a
 * WeirdList. */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        Add f = new Add(n);
        return L.map(f);
    }

    /** Return the sum of all the elements in L. */
    static int sum(WeirdList L) {
        Sum s = new Sum();
        L.map(s);
        return s.sum;
    }

    private static class Add implements IntUnaryFunction {
        public int x;
        public Add(int x) {
            this.x = x;
        }
        public int apply(int head) {
            return head+x;
        }
    }

    private static class Sum implements IntUnaryFunction {
        public int sum;
        public int apply(int x){
            sum += x;
            return x;
        }
    }
    /* IMPORTANT: YOU ARE NOT ALLOWED TO USE RECURSION IN ADD AND SUM
     *
     * As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     *
     * HINT: Try checking out the IntUnaryFunction interface.
     *       Can we use it somehow?
     */
}
