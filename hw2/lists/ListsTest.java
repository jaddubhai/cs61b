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
    public void testnaturalRuns() {
        IntList null_check = IntList.list();
        assertEquals(null, Lists.naturalRuns(null_check));

        IntList A = IntList.list(1, 2, 6, 5, 4);
        IntList B = IntList.list(1, 2, 6);
        IntList C = IntList.list(5);
        IntList D = IntList.list(4);
        IntListList E = IntListList.list(B, C, D);
        assertEquals(E, Lists.naturalRuns(A));


    }

}
