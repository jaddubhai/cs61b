package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class Repo implements Serializable {

    private String _lastcommit;

    private HashMap<String, Blob> _stagefiles = new HashMap<>();


    public Repo init() {

        Commit initcom = new Commit("initial commit", java.sql.Timestamp.valueOf("1970-01-01 00:00:00.0"), null);

        File commits = new File(".gitlet/commits");
        commits.mkdir();
        File staging = new File(".gitlet/staging");
        staging.mkdir();

        _stagefiles = new HashMap<String, Blob>();

        String hash = initcom.gethash();
        File comm = new File(".gitlet/commits/" + hash);
        Utils.writeContents(comm, Utils.serialize(initcom));
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

    public void commit(String message, Timestamp time) {
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
                File delstage = new File(".gitlet/staging/" + _stagefiles.get(filename));
                Utils.restrictedDelete(delstage);
            }
        }

        File addcomm = new File(".gitlet/commits/" + comm.gethash());
        Utils.writeObject(addcomm, comm);

        _stagefiles.clear();
        _lastcommit = comm.gethash();
    }
}


