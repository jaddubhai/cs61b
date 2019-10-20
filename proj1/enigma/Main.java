package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Varun Jadia
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        String firstline = _input.nextLine();

        if (firstline.charAt(0) != '*') {
            throw new EnigmaException("Invalid Config!");
        }

        while (_input.hasNextLine()) {

            if (firstline.charAt(0) != '*') {
                throw new EnigmaException("Invalid Config!");
            }

            if (firstline.charAt(0) == '*') {

                firstline = firstline.replaceAll("\\*", "");
                firstline = firstline.trim();

                setUp(machine, firstline);

                String nextline = _input.nextLine().trim();

                if (!nextline.equals("")) {
                    while (nextline.charAt(0) != '*') {
                        nextline = nextline.replaceAll("\\s", "");
                        printMessageLine(machine.convert(nextline));
                        try {
                            nextline = _input.nextLine().trim();
                        } catch (NoSuchElementException excp) {
                            break;
                        }
                    }
                } else {
                    try {
                        nextline = _input.nextLine().trim();
                    } catch (NoSuchElementException excp) {
                        break;
                    }
                }

                firstline = nextline;
            }

        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {

        Collection<Rotor> rotors = new ArrayList<Rotor>();
        int dummy;
        try {
            _alphabet = new Alphabet(_config.next("\\S+"));
            int numrotors = _config.nextInt();
            int pawls = _config.nextInt();

            while (_config.hasNext(".+")) {
                rotors.add(readRotor());
            }
            if (rotors.size() != 0) {
                dummy = 0;
            } else {
                throw new EnigmaException("Config error");
            }

            return new Machine(_alphabet, numrotors, pawls, rotors);

        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        String name = "";
        String notches = "";
        String cycles = "";
        char rotortype;

        try {
            name = _config.next("[^()\\s]+");
            String rotornotches = _config.next("[^()\\s]+");
            rotortype = rotornotches.charAt(0);
            notches = rotornotches.substring(1);

            while (_config.hasNext("\\s*[(].+[)]\\s*")) {
                cycles += _config.next("\\s*[(].+[)]\\s*");
            }
            if (rotortype == 'R') {
                return new Reflector(name, new Permutation(cycles, _alphabet));
            }
            if (rotortype == 'M') {
                return new MovingRotor(name,
                        new Permutation(cycles, _alphabet), notches);
            }
            if (rotortype == 'N') {
                return new FixedRotor(name, new Permutation(cycles, _alphabet));
            } else {
                throw new EnigmaException("Invalid Rotor");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        int setrotors = 0;

        HashMap<String, Integer> map = new HashMap<>();

        String[] components = settings.split(" ");
        for (String component : components) {
            if (M.contains(component)) {
                if (!map.containsKey(component)) {
                    setrotors += 1;
                } else {
                    throw new EnigmaException("Duplicate rotors");
                }
                map.put(component, 1);
            }
        }

        String set = components[setrotors];
        String[] insertrotrs = new String[setrotors];
        String ringsetting = "";

        System.arraycopy(components, 0, insertrotrs, 0, setrotors);
        M.insertRotors(insertrotrs);
        M.setRotors(set);

        try {
            if (components[setrotors + 1].matches("[^()\\s]+")) {
                ringsetting = components[setrotors + 1];
                M.ringsetting(ringsetting);
                setrotors += 1;
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException excp) {
            ringsetting = "";
        }

        String joined = "";
        for (int i = setrotors + 1; i < components.length; i++) {
            joined += components[i];
            joined += " ";
        }

        joined = joined.trim();
        M.setPlugboard(new Permutation(joined, _alphabet));

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */

    private void printMessageLine(String msg) {
        String msgline = msg;
        if (msgline.length() == 0) {
            _output.println();

        } else {
            while (msgline.length() > 0) {
                int msglen = msgline.length();
                if (msglen <= 5) {
                    _output.println(msgline);
                    msgline = "";
                } else {
                    _output.print(msgline.substring(0, 5) + " ");
                    msgline = msgline.substring
                            (5, msglen);
                }
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
