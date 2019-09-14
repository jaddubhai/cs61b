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
        /* *Replace this body with the solution. */
        if (L == null) {
            return null;
        } else {
            IntList start = Helper(L);
            IntListList sub = naturalRuns(start.tail);
            start.tail = null;
            return new IntListList (L, sub);
        }
    }

    private static IntList Helper(IntList L) {
        while (L.tail != null) {
            if (L.head < L.tail.head) {
                L = L.tail;
            }
        }
        return L;
    }
    }


