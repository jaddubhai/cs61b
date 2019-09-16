package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
 */
class Lists {
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {

        if (L == null ) {
            return null;
        }

        IntList curr = L;
        IntList sub = L;


        while (curr != null) {

            if (curr.tail == null) {
                curr = curr.tail;
                sub.tail = null;
                break;

            } else if (curr.tail.head > curr.head) {
                curr = curr.tail;
                sub = sub.tail;

            } else {
                curr = curr.tail;
                sub.tail = null;
                break;
            }
        }

        IntListList result = new IntListList(L, naturalRuns(curr));
        return result;
    }
    }


