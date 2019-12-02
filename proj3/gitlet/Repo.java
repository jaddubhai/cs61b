package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class Repo implements Serializable {

    private Branch _currbranch;

    private ArrayList<Branch> _branches = new ArrayList<Branch>();

    private HashMap<String, String> _stagefiles = new HashMap<>();

    private Commit lastcommit;

    Repo init() {

        Commit initcom = new Commit("initial commit", java.sql.Timestamp.valueOf("1970-01-01 00:00:00.0"), new HashMap<>());
        Branch master = new Branch();
        master.sethead(initcom);

        File commits = new File(".gitlet/commits");
        commits.mkdir();
        File staging = new File(".gitlet/staging");
        staging.mkdir();

        _currbranch = master;
        _branches.add(master);
        _stagefiles = new HashMap<String, String>();

        String hash = initcom.gethash();
        File comm = new File(".gitlet/commits/" + hash);
        Utils.writeContents(comm, Utils.serialize(initcom));

        return this;
    }

    void add(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.print("File does not exist.");
            System.exit(0);
        }

        String hashfile = Utils.sha1(Utils.readContentsAsString(file));
        File stage = new File(".gitlet/staging/" + hashfile);

        if (!lastcommit.getfiles().containsKey(filename) ||
                !lastcommit.getfiles().get(filename).equals(hashfile)) {
            _stagefiles.put(filename, hashfile);
            Utils.writeContents(stage, Utils.readContentsAsString(file));
        } else if (stage.exists()) {
            _stagefiles.remove(filename);
        }
    }

    void commit(String message, Timestamp time) {
        if (message.equals("")) {
            System.out.print("Please enter a commit message.");
            System.exit(0);
        }

        if (_stagefiles == null) {
            System.out.print("No changes added to the commit.");
            System.exit(0);
        }

        Commit comm = new Commit(message, time, _currbranch.gethead().getfiles());
        _currbranch.commit(comm);

        for (String filename: _currbranch.gethead().gettrackedfiles().keySet()) {
            if (_stagefiles.containsKey(filename)) {
                String hash = _currbranch.gethead().gettrackedfiles().get(filename);
                if (_stagefiles.get(filename).equals(hash)) {
                    _currbranch.gethead().gettrackedfiles().remove(filename);
                    _currbranch.gethead().gettrackedfiles().put(filename, _stagefiles.get(filename));
                }
            }
        }
    }

    public Commit uidToCommit(String uid) {
        File f = new File(".gitlet/commits/" + uid);
        if (f.exists()) {
            return Utils.readObject(f, Commit.class);
        } else {
            Utils.message("No commit with that id exists.");
            throw new GitletException();
        }
    }
}
