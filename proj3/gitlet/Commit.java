package gitlet;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

public class Commit implements Serializable {

    /** timestamp of commit. */
    private Timestamp _timestamp;

    /** log message for commit. */
    private String _logmsg;

    /** repo/directory that the commit is part of. */
    private Repo _ref;

    /** parent commit. */
    private Commit _parent;

    /** uid for commit. */
    private String _uid;

    /** Hashmap containing files. */
    private HashMap<String, String> _files = new HashMap<>();

    /** Hashmap containing tracked files. */
    private HashMap<String, String> _trackedfiles = new HashMap<>();


    Commit (String msg, Timestamp time, HashMap<String, String> files) {
        _logmsg = msg;
        _timestamp = time;
        _files = files;
        _uid = Utils.sha1(_timestamp.toString(), _logmsg);
    }

    /** set timestamp for commit. */
    public void settimestamp(Timestamp _timestamp) {
        this._timestamp = _timestamp;
    }

    /** get files. */
    public HashMap<String, String> getfiles() {
        return _files;
    }

    /** get tracked files. */
    public HashMap<String, String> gettrackedfiles() {
        return _trackedfiles;
    }

    /** get hashid for commit. */
    public String gethash() {
        String files;
        if (_files == null) {
            files = "";
        } else {
            files = _files.toString();
        }
        return Utils.sha1(_logmsg, files, _timestamp.toString());
    }

    /** set files. */
    public void setfiles(HashMap<String, String> _files) {
        this._files = _files;
    }

    /** set tracked files. */
    public void settrackedfiles(HashMap<String, String> _trackedfiles) {
        this._trackedfiles = _trackedfiles;
    }
}
