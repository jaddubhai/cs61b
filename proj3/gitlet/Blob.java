package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private String _filename;

    private String _shacode;

    private String _contents;

    public Blob(String name) {
        File file = new File(name);
        _filename = name;
        _contents = Utils.readContentsAsString(file);
        _shacode = Utils.sha1(_filename, _contents);
    }

    public String getcontents() {
        return _contents;
    }

    public String getfilename() {
        return _filename;
    }

    public String getshacode() {
        return _shacode;
    }
}
