/**
 * Scheme-like pairs that can be used to form a list of integers.
 * @author P. N. Hilfinger or unknown TA
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        if (_back == null){
            return 0;
        }

        DNode temp = _front;

        int size = 0;
        // IntDList.DNode x = _back;

        while (temp != null){
            temp = temp._next;
            size += 1;
        }
        return size;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size
     *          for positive indices and -size <= i < 0 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        if (i < 0){
            i = size() + i;
        }

        DNode temp = _front;

        while (i>0){
            temp = temp._next;
            i --;
        }
        return temp._val;
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        _front = new DNode(null, d, _front);
        // DNode old_back = _back;

        // new_back._next = old_back;

        if (_back == null) {
            _back = _front;
        }
        else {
            _front._next._prev = _front;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        _back = new DNode(_back, d, null);
       // DNode old_back = _back;

       // new_back._next = old_back;

        if (_front == null) {
            _front = _back;
        }
        else {
            _back._prev._next = _back;
        }

    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        int result = _back._val;

        _back = _back._prev;
        if (_back != null) {
            _back._next = null;
        }
        return result;   // Your code here

    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        int length = size();
        String lst_string = "[";

        if (length == 0) {
            return "[]";
        }
        for (int i = 0; i<length -1; i++){
            lst_string += Integer.toString(get(i)) + ", ";
        }
        lst_string += Integer.toString(get(-1)) + ']';
        return lst_string;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    protected static class DNode {
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
