import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author your name here
 */
public class TrReader extends Reader {

    private Reader _reader;
    private String _from;
    private String _to;

    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        _reader = str;
        _from = from;
        _to = to;
        assert(_from.length() == _to.length());

    }

    @Override
    public int read(char[] chars, int start, int len) throws IOException {
        int charsread = _reader.read(chars, start, len);
        for (int i = start; i < start + charsread; i++) {
            chars[i] = translate(chars[i]);
        }

        return charsread;
    }

    private char translate(char in) {
        int k = _from.indexOf(in);
        if (k == -1) {
            return in;
        } else {
            return _to.charAt(k);
        }
    }

    public void close() throws IOException {
        _reader.close();
    }

}
