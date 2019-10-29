import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet {

    public ECHashStringSet() {

        _HashMap = new LinkedList[5];
        for (int i = 0; i < 5; i++) {
            _HashMap[i] = new LinkedList<String>();
        }
    }

    @Override
    public void put(String s) {
        if (load() > _maxload) {
            List vals = asList();
            _HashMap = new LinkedList[2*_HashMap.length];

            for (Object elem : vals) {
                int idx = (elem.hashCode() & 0x7fffffff) %_HashMap.length;
                if (_HashMap[idx] == null) {
                    _HashMap[idx] = new LinkedList<>();
                }
                _HashMap[idx].add((String) elem);
            }
        }

        int idx;
        if (_HashMap == null) {
             idx = (s.hashCode() & 0x7fffffff);
        } else {
             idx = (s.hashCode() & 0x7fffffff) % _HashMap.length;
        }

        _HashMap[idx].add(s);
    }

    @Override
    public boolean contains(String s) {
        int idx;
        if (_HashMap == null) {
            idx = (s.hashCode() & 0x7fffffff);
        } else {
            idx = (s.hashCode() & 0x7fffffff) % _HashMap.length;
        }

        for (String item: _HashMap[idx]) {
            if (item.equals(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> asList() {
        LinkedList<String> retarr = new LinkedList<String>();
        for (LinkedList<String> strlst : _HashMap) {
            retarr.addAll(strlst);
        }
        return  retarr;
    }

    private int load() {
        if (_HashMap == null) {
            return 0;
        }
        return _HashMap[0].size()/_HashMap.length;
    }

    private LinkedList<String>[] _HashMap;
    private final int _minload = 0/2;
    private final int _maxload = 5;
}
