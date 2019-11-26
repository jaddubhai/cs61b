import java.util.Arrays;

/** A partition of a set of contiguous integers that allows (a) finding whether
 *  two integers are in the same partition set and (b) replacing two partitions
 *  with their union.  At any given time, for a structure partitioning
 *  the integers 1-N, each partition is represented by a unique member of that
 *  partition, called its representative.
 *  @author
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        _subsets = new int[N + 1];
        _size = new int[N + 1];
        for (int i = 1; i < N + 1; i++) {
            _subsets[i] = i;
            _size[i] = 1;
        }

    }

    /** Return the representative of the partition currently containing V.
     *  Assumes V is contained in one of the partitions.  */
    public int find(int v) {
        while (_subsets[v] != v) {
            v = _subsets[v];
        }
        return v;
    }

    /** Return true iff U and V are in the same partition. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single partition, returning its representative. */
    public int union(int u, int v) {
        u = find(u);
        v = find(v);

        if (u == v) {
            return u;
        }

        if (_size[u] < _size[v]) {
            _subsets[u] = v;
            _size[v] += _size[u];
            return v;
        } else {
            _subsets[v] = u;
            _size[u] += _size[v];
            return u;
        }
    }

    private int[] _subsets;
    private int[] _size;
}
