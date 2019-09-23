/** A WeirdList holds a sequence of integers.
 * @author your name here
 */
public class WeirdList {
    private int _head;
    private WeirdList _tail;

    /** The empty sequence of integers. */
    public static final WeirdList EMPTY = new nullWeirdList(0, null);

    /** A new WeirdList whose head is HEAD and tail is TAIL. */
    public WeirdList(int head, WeirdList tail) {
        this._head = head;
        this._tail = tail;
    }

    /**
     * Returns the number of elements in the sequence that starts with THIS.
     */
    public int length() {
        return _tail.length() + 1;
    }

    /**
     * Return a string containing my contents as a sequence of numerals each preceded by a blank.
     * Thus, if my list contains 5, 4, and 2, this returns " 5 4 2".
     */
    @Override
    public String toString() {
        return " " + _head + _tail.toString();
    }

    /**
     * Part 3b: Apply FUNC.apply to every element of THIS WeirdList in sequence, and return a
     * WeirdList of the resulting values.
     */
    public WeirdList map(IntUnaryFunction func) {
        return new WeirdList(func.apply(this._head), this._tail.map(func));
    }

    private static class nullWeirdList extends WeirdList {
        public nullWeirdList(int head, WeirdList tail) {
            super(head, tail);
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public int length() {
            return 0;
        }

    }

}
