/** HW #7, Two-sum problem.
 * @author
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        MySortingAlgorithms.MergeSort sorter = new MySortingAlgorithms.MergeSort();
        sorter.sort(A, A.length);
        sorter.sort(B, B.length);

        for (int i = A.length - 1, j = 0; i > -1 && j < B.length;) {
            if (B[j] + A[i] == m) {
                return true;
            } else if (B[j] + A[i] < m) {
                j++;
            } else {
                i--;
            }
        }
        return false;
    }
}
