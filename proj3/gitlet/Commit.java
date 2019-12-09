package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Commit class for Gitlet, the tiny stupid version-control system.
 *  @author Varun Jadia
 */

public class Commit implements Serializable {

    /** timestamp of commit. */
    private String _timestamp;

    /** log message for commit. */
    private String _logmsg;

    /** repo/directory that the commit is part of. */
    private Repo _ref;

    /** uid for commit. */
    private String _uid;

    /** Hashmap containing files. */
    private HashMap<String, Blob> _files = new HashMap<>();

    /** Hashmap containing tracked files. */
    private HashMap<String, String> _trackedfiles = new HashMap<>();

    /** Commit parent id. */
    private String _parenthash;

    /** Commit mergeparent id. */
    private String _mergeparenthash;

    /** String contents of all blobs. */
    private ArrayList<String> _filecontents;

    /** Return parents of a commit.RETURN*/
    public ArrayList<String> getparents() {
        ArrayList<String> arr = new ArrayList<>();
        if (_parenthash != null) {
            arr.add(_parenthash);
        }
        if (_mergeparenthash != null) {
            arr.add(_mergeparenthash);
        }
        return arr;
    }

    /** commit initializer for initial commit. MSG TIME PARENT*/
    Commit(String msg, String time, String parent) {
        _logmsg = msg;
        _timestamp = time;
        _parenthash = parent;
    }

    /**commit initializer for other commits. MSG TIME FILES PARENT*/
    Commit(String msg, String time,
           HashMap<String, Blob> files, String parent) {
        _logmsg = msg;
        _timestamp = time;
        _files = files;

        for (String file : _files.keySet()) {
            if (_filecontents == null) {
                _filecontents = new ArrayList<>();
            }
            _filecontents.add(_files.get(file).getcontents());
        }
        _parenthash = parent;
    }

    /** commit initializer for merges. */
    Commit(String msg, String time,
           HashMap<String, Blob> files, String parent, String mergeparent) {
        _logmsg = msg;
        _timestamp = time;
        _files = files;

        for (String file : _files.keySet()) {
            if (_filecontents == null) {
                _filecontents = new ArrayList<>();
            }
            _filecontents.add(_files.get(file).getcontents());
        }
        _parenthash = parent;
        _mergeparenthash = mergeparent;
    }

    /** set timestamp for commit. TIMESTAMP*/
    public void settimestamp(String timestamp) {
        this._timestamp = timestamp;
    }

    /** get files. RETURN*/
    public HashMap<String, Blob> getfiles() {
        return _files;
    }

    /** get tracked files. RETURN*/
    public HashMap<String, String> gettrackedfiles() {
        return _trackedfiles;
    }

    /** get hashid for commit. RETURN*/
    public String gethash() {
        if (_logmsg.equals("initial commit")) {
            List<Object> shalist = new ArrayList<>();
            shalist.add(_logmsg);
            shalist.add("buffer");
            return Utils.sha1(shalist);
        }
        List<Object> shalist = new ArrayList<>();
        shalist.add(_logmsg);
        shalist.add("yeeet");
        shalist.add(_parenthash);
        return Utils.sha1(shalist);
    }

    /** set files. FILES*/
    public void setfiles(HashMap<String, Blob> files) {
        this._files = files;
    }

    /** set tracked files. TRACKEDFILES*/
    public void settrackedfiles(HashMap<String, String> trackedfiles) {
        this._trackedfiles = trackedfiles;
    }

    /** get timestamp. RETURN. */
    public String gettimestamp() {
        return _timestamp;
    }

    /**get commit msg. RETURN. */
    public String getlogmsg() {
        return _logmsg;
    }

    /**get parent commit hash. RETURN. */
    public String getparenthash() {
        return _parenthash;
    }

    /**get mergeparent hash. RETURN. */
    public String getmergeparenthash() {
        return _mergeparenthash;
    }

    /** set mergeparent hash. RETURN. */
    public void set_mergeparenthash(String set) {
        _mergeparenthash = set;
    }
}
