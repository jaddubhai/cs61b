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
        int index = A.length -1;
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
        /* *Replace this body with the solution. */
        int index = 0;

        if (len <= 0 || A.length == 0){
            return A;
        }

        while (index < A.length){
            if (A[index] == start){
                break;
            }
            index ++;
        }
        int[] result = new int[A.length - (index + len -1)];

        for (int i = 0; i< index; i ++){
            result[i] = A[i];
        }

        for (int i = index+len; i < A.length; i++){
            result[i] = A[i];
        }

        return result;
    }

    /* E. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        /* *Replace this body with the solution. */
        int tracker = 0;
        int first = A[0];

        for (int i = 0; i < A.length; i++) {
            if (first > A[i]) {
                tracker++;
            }
        }

        int[][] result = new int[tracker][];
        int[] indexes = new int[tracker];

        for (int i = 0; i < tracker; i++) {
            //int h = A[0];
            for (int j = 0; j < A.length; j++) {
                if (first > A[j]) {
                    indexes[i] = j;
                }
            }
        }
        return result;
    }
}
