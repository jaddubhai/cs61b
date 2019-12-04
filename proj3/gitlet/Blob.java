package gitlet;

import java.io.File;
import java.io.Serializable;

/** Blob class for Gitlet, the tiny stupid version-control system.
 *  @author Varun Jadia
 */
public class Blob implements Serializable {
    /** stores filename. */
    private String _filename;

    /** stores shacode. */
    private String _shacode;

    /** stores contents of file as a string. */
    private String _contents;

    /** initializing method. NAME */
    public Blob(String name) {
        File file = new File(name);
        _filename = name;
        _contents = Utils.readContentsAsString(file);
        _shacode = Utils.sha1(_filename, _contents);
    }

    /** get contents of blob. RETURN */
    public String getcontents() {
        return _contents;
    }

    /** get filename. RETURN */
    public String getfilename() {
        return _filename;
    }

    /** get shacode. RETURN */
    public String getshacode() {
        return _shacode;
    }
}
