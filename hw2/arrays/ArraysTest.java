package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /**
     * FIXME
     */

    @Test
    public void testCatenate() {
        int[] a = {1, 2, 3};
        int[] b = {4, 5, 6};
        int[] c = {1, 2, 3, 4, 5, 6};
        assertEquals(true, Utils.equals(Arrays.catenate(a, b), c));
    }

    @Test
    public void testremove() {
        int[] A = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] C = {1, 2, 6, 7, 8};
        assertArrayEquals(C, Arrays.remove(A, 2, 3));

    }

    @Test
    public void testnaturalRuns() {
        int[] A = {1, 2, 6, 5, 4};
        int[][] B = {{1, 2, 6}, {5}, {4}};

        assertArrayEquals(B, Arrays.naturalRuns(A));
        assertArrayEquals(null, Arrays.naturalRuns(null));

    }
}
