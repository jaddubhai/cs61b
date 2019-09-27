import java.util.Iterator;
import utils.Filter;

/** A kind of Filter that lets through every other VALUE element of
 *  its input sequence, starting with the first.
 *  @author Your Name
 */
class AlternatingFilter<Value> extends Filter<Value> {

    /** A filter of values from INPUT that lets through every other
     *  value. */
    AlternatingFilter(Iterator<Value> input) {
        super(input); //FIXME?
    }

    @Override
    protected boolean keep() {
        if (counter%2 == 0){
            counter += 1;
            return true;
        }
        else{
            counter += 1;
            return false;
        }
    }


    private int counter = 0;

}