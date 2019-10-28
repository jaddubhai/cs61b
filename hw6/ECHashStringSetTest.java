import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    @Test
    public void testNothing() {
        ECHashStringSet bst = new ECHashStringSet();
        String yeboi = "Hello world";
        bst.put(yeboi);
        assert(bst.contains(yeboi));

        String yeboi2 = "Lowkey";
        bst.put(yeboi2);

        assert(bst.contains(yeboi2));
    }
}
