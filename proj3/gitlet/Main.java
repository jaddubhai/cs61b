package gitlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Varun Jadia
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    private static String[] trycommands =
        {"init", "add", "checkout", "commit", "rm", "log"};

    /** repo for gitlet.*/
    private static Repo _repo;

    /** Main function for gitlet. ARGS */
    public static void main(String... args) throws IOException {
        if (args == null || args.length == 0) {
            System.out.print("Please enter a command.");
            System.exit(0);
        } else if (args.length > 4) {
            System.out.print("Incorrect operands.");
            System.exit(0);
        }

        String command = args[0];
        String operand;
        try {
            operand = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            operand = null;
        }

        List<String> commandlst = Arrays.asList(trycommands);

        if (commandlst.contains(command)) {
            if (command.equals("init")) {
                _repo = init();
                save(_repo);
            } else if (command.equals("add") && operand != null) {
                _repo = load();
                _repo.add(operand);
                save(_repo);
            } else if (command.equals("commit") && operand != null) {
                String timestamp = ZonedDateTime.now().format
                        (DateTimeFormatter.ofPattern
                                ("EEE MMM d HH:mm:ss yyyy xxxx"));
                _repo = load();
                _repo.commit(operand, timestamp);
                save(_repo);
            } else if (command.equals("checkout") && args.length == 3) {
                _repo = load();
                _repo.checkout1(args[2]);
                save(_repo);
            } else if (command.equals("checkout") && args.length == 4) {
                _repo = load();
                _repo.checkout2(args[1], args[3]);
                save(_repo);
            } else if (command.equals("log")) {
                _repo = load();
                _repo.log();
                save(_repo);
            }
        } else {
            System.out.print("No command with that name exists.");
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
        File file = new File(".gitlet/repo");
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
