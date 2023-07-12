/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Linda Deng (9/1/2021)
 */
public class IntDList {

    //First and last nodes of list.
    protected DNode _front, _back;

    //An empty list
    public IntDList() {
        _front = _back = null;
    }

    /** @param values the ints to be placed in the IntDList.*/
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**@return The first value in this list.
     Throws a NullPointerException if the list is empty.*/
    public int getFront() {
        return _front._val;
    }

    /*** @return The last value in this list.
     * Throws a NullPointerException if the list is empty.*/
    public int getBack() {
        return _back._val;
    }

    /*** @return The number of elements in this list.*/
    public int size() {
        if (_back==null && _front==null){
            return 0;
        }
        int res = 0;
        DNode node = _back;
        while (node != null) {
            res++;
            node = node._prev;
        }
        return res;
    }

    /*** @param index index of node to return,
     *          where index = 0 returns the first node,
     *          index = 1 returns the second node,
     *          index = -1 returns the last node,
     *          index = -2 returns the second to last node, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices
     *          and -size <= index <= -1 for negative indices.
     * @return The node at index index*/ // TODO: Implement this method and return correct node
    private DNode getNode(int index) {
        DNode node = _front;
        if (index<0)    {
            node = _back;
            for (int i = 1; i < Math.abs(index) ; i++) {
                node = node._prev;
            }
            return node;
        }
        else {
            for (int i = 0; i < index; i++) {
                node=node._next;
            }
        }
        return node;
    }

    /**
     * @param index index of element to return,
     *          where index = 0 returns the first element,
     *          index = 1 returns the second element,
     *          index = -1 returns the last element,
     *          index = -2 returns the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices
     *          and -size <= index <= -1 for negative indices.
     * @return The integer value at index index
     */
    public int get(int index) {
        return getNode(index)._val; // TODO: Implement this method (Hint: use `getNode`)
    }

    /*** @param d value to be inserted in the front*/ // TODO: Implement this method
    public void insertFront(int d) {
        if (size() == 0){
            _front = _back = new DNode(d);
            return;
        }
        DNode prev = _front;
        _front = new DNode(null, d, _front);
        _front._next = prev;
        prev._prev = _front;
    }

    /*** @param d value to be inserted in the back*/ // TODO: Implement this method
    public void insertBack(int d) {
        if (size() == 0){
            _front = _back = new DNode(d);
            return;
        }
        DNode prev = _back;
        _back = new DNode(_back, d, null);
        _back._prev = prev;
        prev._next = _back;
    }

    /** @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, etc.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices
     *              and -(size+1) <= index <= -1 for negative indices.*/ // TODO: Implement this method
    public void insertAtIndex(int d, int index) {
        int i = index;
        if (i<0){
            i = index+size()+1;
        }
        if (size() == 0){
            _front = _back = new DNode(d);
        }
        else if (i == 0){
            insertFront(d);
        }
        else if(i == size()){
            insertBack(d);
        }
        else{
            DNode nxt = getNode(i);
            DNode prev = getNode(i - 1);
            DNode curr = new DNode(prev, d, nxt);
            nxt._prev = curr;
            prev._next = curr;
        }
    }

    /*** Removes the first item in the IntDList and returns it. Assume `deleteFront` is never called on an empty IntDList.
     *@return the item that was deleted*/  // TODO: Implement this method and return correct value
    public int deleteFront() {
        if (size()==1){
            int del = _front._val;
            _front = _back = null;
            return del;
        }
        DNode del = _front;
        _front._next._prev = null;
        _front = _front._next;
        return del._val;
    }

    /** Removes the last item in the IntDList and returns it. Assume `deleteBack` is never called on an empty IntDList.
     * @return the item that was deleted*/  // TODO: Implement this method and return correct value
    public int deleteBack() {
        if (size()==1){
            int del = _front._val;
            _front = _back = null;
            return del;
        }
        DNode del = _back;
        _back._prev._next = null;
        _back = _back._prev;
        return del._val;
    }

    /**@param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices
     *              and -size <= index <= -1 for negative indices.
     * @return the item that was deleted*/
    public int deleteAtIndex(int index) {
        return 0;//TODO
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */ // TODO: Implement this method to return correct value
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
