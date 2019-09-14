package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author FIXME
 */

public class ListsTest {
    /** FIXME
     */


    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.
    @Test
    public void naturalRunstest() {
        assertEquals(null, Lists.naturalRuns(IntList.list()));

        IntList one = IntList.list(1,2,5);
        IntList two = IntList.list(2);
        IntList test = new IntListList ( one, two);
        assertEquals(test, Lists.naturalRuns(IntList.list(1,2,5,2)));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
