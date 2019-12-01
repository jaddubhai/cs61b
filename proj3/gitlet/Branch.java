package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Branch implements Serializable {

    /**head pointer for branc. */
    private Commit _head;

    /** array of commit-hashids for branch. */
    private ArrayList<String> _uids = new ArrayList<String>();

    public Branch() {

    }

    public void sethead(Commit _head) {
        this._head = _head;
    }

    public Commit gethead() {
        return _head;
    }

    public void commit(Commit comm) {
        _head = comm;
        _uids.add(comm.gethash());
        comm.setfiles(_head.getfiles());
        comm.settrackedfiles(_head.gettrackedfiles());
    }
}
