import java.util.Iterator;
import utils.Predicate;
import utils.Filter;

/** A kind of Filter that tests the elements of its input sequence of
 *  VALUES by applying a Predicate object to them.
 *  @author You
 */
class PredicateFilter<Value> extends Filter<Value> {
    private Predicate<Value> func;

    /** A filter of values from INPUT that tests them with PRED,
     *  delivering only those for which PRED is true. */
    PredicateFilter(Predicate<Value> pred, Iterator<Value> input) {
        super(input); //FIXME ??
        func = pred;
    }

    @Override
    protected boolean keep() {

        if (func.test(_next)){
            current = _next;
            return true;

        }

        return false;  // FIXME: REPLACE THIS LINE WITH YOUR CODE
    }

    private Value current;

}
