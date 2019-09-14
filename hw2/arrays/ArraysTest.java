package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /** FIXME
     */

    @Test
    public void testCatenate() {
        int[] a = {1, 2, 3};
        int[] b = {4, 5, 6};
        int[] c = {1, 2, 3, 4, 5, 6};
        assertEquals(true, Utils.equals(Arrays.catenate(a, b), c));
    }

    @Test
    public void testRemove() {
        int[] A = {1, 2, 3, 4, 5};
        int[] B = {3,4};
        assertEquals(true, Utils.equals(Arrays.remove(A, 2, 2), B));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
