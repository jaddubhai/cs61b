package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class Repo implements Serializable {

    private String _lastcommit;

    private HashMap<String, Blob> _stagefiles = new HashMap<>();

    /** Hashmap of branch names to their head commits. */
    private HashMap<String, String> _branchmap = new HashMap<>();

    /** Tracks current branch by its name. */
    private String _currbranch;

    public Repo init() {

        Commit initcom = new Commit("initial commit", "Thu Jan 1 00:00:00 1970 -0800", null);

        File commits = new File(".gitlet/commits");
        commits.mkdir();
        File staging = new File(".gitlet/staging");
        staging.mkdir();

        _stagefiles = new HashMap<String, Blob>();
        _lastcommit = initcom.gethash();

        String hash = initcom.gethash();
        File comm = new File(".gitlet/commits/" + hash);
        Utils.writeContents(comm, Utils.serialize(initcom));
        _branchmap.put("master", hash);
        _currbranch = "master";
        return this;
    }

    public void copy(Repo repo) {
        _stagefiles = repo._stagefiles;
        _lastcommit = _lastcommit;
    }

    public void add(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.print("File does not exist.");
            System.exit(0);
        }

        Blob fileblob = new Blob(filename);
        String hashfile = fileblob.getshacode();
        Commit lastcommit = Utils.readObject(new File(".gitlet/commits/" + _lastcommit), Commit.class);

        for (String name : lastcommit.getfiles().keySet()) {
            if (hashfile.equals(lastcommit.getfiles().get(name).getshacode())) {
                if (_stagefiles.containsKey(name)) {
                    File remove = new File(".gitlet/staging" + lastcommit.getfiles().get(name).getshacode());
                    remove.delete();
                }
            }
            return;
        }

        File stage = new File(".gitlet/staging/" + hashfile);
        _stagefiles.put(filename, fileblob);
        Utils.writeObject(stage, fileblob);
    }

    public void commit(String message, String time) {
        if (message.equals("")) {
            System.out.print("Please enter a commit message.");
            System.exit(0);
        }

        if (_stagefiles == null) {
            System.out.print("No changes added to the commit.");
            System.exit(0);
        }

        Commit lastcommit = Utils.readObject(new File(".gitlet/commits/" + _lastcommit), Commit.class);
        Commit comm = new Commit(message, time, lastcommit.getfiles(), _lastcommit);

        if (comm.gethash().equals(_lastcommit)) {
            System.out.print("No changes added to the commit.");
            System.exit(0);
        }

        for (String filename : comm.getfiles().keySet()) {
            if (_stagefiles.containsKey(filename)) {
                comm.getfiles().remove(filename);
                comm.getfiles().put(filename, _stagefiles.get(filename));
            }
        }

        for (String filename : _stagefiles.keySet()) {
            if (!comm.getfiles().containsKey(filename)) {
                comm.getfiles().put(filename, _stagefiles.get(filename));
                File delstage = new File(".gitlet/staging/" + _stagefiles.get(filename).getshacode());
                delstage.delete();
            }
        }

        File addcomm = new File(".gitlet/commits/" + comm.gethash());
        Utils.writeObject(addcomm, comm);

        _stagefiles.clear();
        _lastcommit = comm.gethash();
    }

    public void checkout1(String filename) {
        Commit lastcommit = Utils.readObject(new File(".gitlet/commits/" + _lastcommit), Commit.class);
        Blob blb = null;
        if (lastcommit.getfiles().containsKey(filename)) {
            blb = lastcommit.getfiles().get(filename);
        } else {
            System.out.print("File does not exist in that commit.");
            System.exit(0);
        }
        File overwrite = new File(filename);
        overwrite.delete();
        Utils.writeObject(overwrite, blb.getcontents());
    }

    public void checkout2(String commitid, String filename) {
        File checkcomm = new File(".gitlet/commits/" + commitid);
        Commit lastcommit = null;
        if (checkcomm.exists()) {
            lastcommit = Utils.readObject(new File(".gitlet/commits/" + checkcomm), Commit.class);
        } else {
            System.out.print("No commit with that id exists.");
            System.exit(0);
        }
        Blob blb = null;
        if (lastcommit.getfiles().containsKey(filename)) {
            blb = lastcommit.getfiles().get(filename);
        } else {
            System.out.print("File does not exist in that commit.");
            System.exit(0);
        }
        File overwrite = new File(filename);
        overwrite.delete();
        Utils.writeObject(overwrite, blb.getcontents());
    }

    public void checkout3(String branchid) {
        if (_branchmap.containsKey(branchid)) {
            String headcommit = _branchmap.get(branchid);
        } else {
            System.out.print("No such branch exists.");
            System.exit(0);
        }
    }

    public void log() {
        String counter = _lastcommit;
        while (counter != null) {
            Commit lastcommit = Utils.readObject(new File(".gitlet/commits/" + counter), Commit.class);
            System.out.println("===");
            System.out.println("commit "+lastcommit.gethash());
            System.out.println(lastcommit.gettimestamp());
            System.out.println(lastcommit.getlogmsg());
            System.out.println();
            System.out.print("===");
            counter = lastcommit.getparenthash();
        }
    }
}


