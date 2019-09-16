package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {
    /* C. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        int[] catenated = new int[A.length + B.length];

        int count1 = 0;
        while (count1 < A.length){
            catenated[count1] = A[count1];
            count1++;
        }

        int count2 = 0;
        int index = A.length;
        while (count2 < B.length){
            catenated[index] = B[count2];
            count2++;
            index++;
        }

        return catenated;
    }

    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {

        int[] removed = new int[A.length-len];
        System.arraycopy(A, 0, removed, 0, start );
        System.arraycopy(A, start+len, removed, start, A.length-(start+len));
        return removed;
    }

    /* E. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        if (A == null) {
            return null;
        }

        int counter = 1;
        for(int i = 0; i < A.length-1; i++){
            if (A[i] > A[i + 1]){
                counter += 1;
            }
        }

        int[][] result = new int[counter][];

        int row=0;
        int start = 0;

        for (int i = 0; i < A.length; i++) {
            if (i < A.length -1 && A[i + 1] <= A[i]) {

                result[row] = new int[i - start + 1];
                System.arraycopy(A, start, result[row], 0, i - start + 1);
                start = i + 1;
                row = row + 1;

            } else if (i == A.length - 1) {
                result[row] = new int[i + 1 - start];
                System.arraycopy(A, start, result[row], 0, i + 1 - start);
            }
        }

        return result;
    }
}
