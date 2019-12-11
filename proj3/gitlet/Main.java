package gitlet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Varun Jadia
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    /** repo for gitlet.*/
    private static Repo _repo;

    /** Main function for gitlet. ARGS */
    public static void main(String... args) throws IOException {
        if (args == null || args.length == 0) {
            System.out.print("Please enter a command.");
            System.exit(0);
        }
        _repo = load();
        String command = args[0];
        try {
            switch (command) {
            case "init":
                _repo = init();
                break;
            case "add":
                _repo.add(args[1]);
                break;
            case "commit":
                String timestamp = retimestamp();
                _repo.commit(args[1], timestamp);
                break;
            case "checkout":
                checkouthelper(args);
                break;
            case "log":
                _repo.log();
                break;
            case "find":
                _repo.find(args[1]);
                break;
            case "global-log":
                _repo.globallog();
                break;
            case "rm":
                _repo.rm(args[1]);
                break;
            case "merge":
                _repo.merge(args[1]);
                break;
            case "status":
                _repo.status();
                break;
            case "branch":
                _repo.branch(args[1]);
                break;
            case "rm-branch":
                _repo.rmbranch(args[1]);
                break;
            case "reset":
                _repo.reset(args[1]);
                break;
            default:
                System.out.print("No command with that name exists.");
                break;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.print("Incorrect operands.");
        } catch (NullPointerException a) {
            System.out.print("Not in an initialized Gitlet directory.");
        }
        save(_repo);
    }

    /**helper for finding timestamp.RETURN.*/
    private static String retimestamp() {
        return ZonedDateTime.now().format
                (DateTimeFormatter.ofPattern
                        ("EEE MMM d HH:mm:ss yyyy xxxx"));
    }

    /**checkout helper for main.ARGS.*/
    private static void checkouthelper(String... args) {
        if (args.length == 3 && args[1].equals("--")) {
            _repo.checkout1(args[2]);
        } else if (args.length == 2) {
            _repo.checkout3(args[1]);
        } else if (args.length == 4 && args[2].equals("--")) {
            _repo.checkout2(args[1], args[3]);
        } else {
            System.out.print("Incorrect operands.");
            System.exit(0);
        }
    }

    /** initializes a new gitlet directory. RETURN */
    private static Repo init() {
        File directory = new File(".gitlet/");
        if (!directory.exists()) {
            directory.mkdirs();
            Repo newrep = new Repo();
            return newrep.init();
        } else {
            System.out.print("A Gitlet version-control system"
                    + " already exists in the current directory.");
            return null;
        }
    }

    /** helper for loading an initialized
     * gitlet directory if it already exists. RETURN*/
    private static Repo load() {
        File file = new File(".gitlet" + File.separator + "repo");
        Repo repo = null;
        if (file.exists()) {
            repo = Utils.readObject(file, Repo.class);
        }
        return repo;
    }

    /** helper to save repo. REPO */
    private static void save(Repo repo) {
        if (repo == null) {
            return;
        }
        try {
            File f = new File(".gitlet/repo");
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(repo);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
