package enigma;

import javax.crypto.Mac;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author
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
        Machine _machine = readConfig();
        String firstline = _input.nextLine();
        if (!(firstline.substring(0, 1).equals("*"))) {
            throw error("configuration required!");
        }

        firstline = firstline.replaceAll("\\*", "");
        firstline = firstline.trim();

        setUp(_machine, firstline);

        while (_input.hasNextLine()) {
            String nextline = _input.nextLine().trim();
            nextline = nextline.replaceAll("\\s", "");
            printMessageLine(_machine.convert(nextline));
        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {

        ArrayList _rotors = new ArrayList<>();
        try {

            String alphabetline = _config.nextLine();
            alphabetline = alphabetline.replaceAll("\\s","");

            //convert everything to uppercase!

            _alphabet = new Alphabet(alphabetline);

            String rotorinfo = _config.nextLine();
            rotorinfo = rotorinfo.replaceAll("\\s","");

            int numRotors = Character.getNumericValue(rotorinfo.charAt(0));
            int pawls = Character.getNumericValue(rotorinfo.charAt(1));

            while (_config.hasNextLine()) {
                Scanner oldconfig = _config;
                String rotorline = _config.nextLine().trim();
                String nextline = _config.nextLine().trim();

                if (nextline.charAt(0) == '(') {
                    rotorline = rotorline + " " + nextline;

                } else {
                    _config = oldconfig;
                }

                _rotors.add(readRotor(rotorline));
            }

            return new Machine(_alphabet, numRotors, pawls, _rotors);

        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor(String rotorline) {
        String name = "";
        String notches = "";
        int breakindex = 0;

        try {
            for (int i = 0; i < rotorline.length(); i++) {
                if (Character.isWhitespace(rotorline.charAt(i))) {
                    breakindex = i;
                    break;
                }
                name += rotorline.charAt(i);
            }

            String remaining = rotorline.substring(breakindex);
            remaining = remaining.trim();

            char rotortype = remaining.charAt(0);
            remaining = remaining.substring(1);
            int break2 = 0;

            for (int i = 0; i < remaining.length(); i++) {
                if (Character.isWhitespace(rotorline.charAt(i))) {
                    break2 = i;
                    break;
                }
                 notches += remaining.charAt(i);
            }

            String cycles = remaining.substring(break2);
            cycles = cycles.trim();

            if (rotortype == 'R') {
                return new Reflector(name.toUpperCase(), new Permutation(cycles, _alphabet));
            }

            if (rotortype == 'M') {
                return new MovingRotor(name.toUpperCase(), new Permutation(cycles, _alphabet), notches);
            }

            if (rotortype == 'N') {
                return new FixedRotor(name.toUpperCase(), new Permutation(cycles, _alphabet));
            }

            else {
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

        String[] components = settings.split(" ");
        for (String component : components) {
            if (M.contains(component)) {
                setrotors += 1;
            }
        }

        String set = components[setrotors];
        String[] insertrotrs = new String[setrotors];

        System.arraycopy(components, 0 , insertrotrs, 0, setrotors);
        M.insertRotors(insertrotrs);
        M.setRotors(set);

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
        String output = "";
        int counttofive = 1;

        for (int i = 0; i < msg.length(); i++) {
            if (counttofive % 5 == 0) {
                _output.println(output);
                output = "";
            }
            output += msg.charAt(i);
            counttofive += 1;
        }
        _output.println(output);
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
