package enigma;

import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Varun Jadia
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {

        _alphabet = alpha;
        _pawls = pawls;
        _rotors = new Rotor[numRotors];
        _availableRotors = new HashMap<>();

        for (Rotor rtr : allRotors) {
            _availableRotors.put(rtr.name(), rtr);
        }

    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _rotors.length;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {

        HashMap<String, Integer> map = new HashMap<>();
        for (String c : rotors) {
            if (map.containsKey(c)) {
                throw new EnigmaException("Duplicate Rotors");
            }
            map.put(c, 1);
        }

        int counter = 0;

        for (String rtr : rotors) {
            if (_availableRotors.containsKey(rtr)) {
                _rotors[counter] = _availableRotors.get(rtr);
            }
            counter++;
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {

        if (setting.length() != _rotors.length - 1) {
            throw new EnigmaException("invalid setting length");
        }

        int counter = 0;
        while (counter < setting.length()) {
            if (!_alphabet.contains(setting.charAt(counter))) {
                throw new EnigmaException("Setting character not in Alphabet");
            }
            _rotors[counter + 1].set(setting.charAt(counter));
            counter++;
        }

    }

    /** Set the plugboard to PLUGBOARD. */

    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {

        int permuted = _plugboard.permute(c);
        boolean hasadvanced = false;

        boolean[] rotate = new boolean[_rotors.length - 1];

        if (c == -1) {
            throw new EnigmaException("Character not in Alphabet!");
        }

        for (int i = _rotors.length - 1; i > 0; i--) {
            if (_rotors[i].atNotch()) {
                if (_rotors[i-1].rotates()) {
                    rotate[i] = true;
                    rotate[i-1] = true;
                }
                if (i == _rotors.length - 1) {
                    _rotors[i].advance();
                    hasadvanced = true;
                }
            }
        }

        if (!hasadvanced) {
            _rotors[_rotors.length - 1].advance();
            rotate[rotate.length - 1] = false;
        }

        for (int i = rotate.length - 1; i >= 0 ; i--) {
            if (rotate[i]) {
                _rotors[i].advance();
            }
        }

        for (int i = _rotors.length - 1; i >= 0; i--) {
            permuted = _rotors[i].convertForward(permuted);
        }

        for (int i = 1; i < _rotors.length; i++) {
            permuted = _rotors[i].convertBackward(permuted);
        }

        return _plugboard.permute(permuted);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        if (msg.equals("")) {
            return msg;
        }

        String replaced = msg.replaceAll("\\s", "");
        String[] msgarray = replaced.split("(?!^)");

        char[] output = new char[msgarray.length];

        for (int i = 0; i < msgarray.length; i++) {
            output[i] = _alphabet.toChar(convert(
                    _alphabet.toInt(replaced.charAt(i))));
        }

        return new String(output);
    }

    /** Change rotor positions based on ringsetting.
     * @param setting */
    void ringsetting(String setting) {

        assert (setting.length() == numRotors() - 1);
        char[] settingarr = setting.toCharArray();

        for (int i = 1; i < _rotors.length; i++) {
            _rotors[i].setringsetting(_alphabet.toInt(settingarr[i - 1]));
        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** method to check if a rotor is in the machine.
     * @param name rotor name
     * @return boolean*/

    boolean contains(String name) {
        return _availableRotors.containsKey(name);
    }

    /** plugboard. */
    private Permutation _plugboard;
    /** pawls. */
    private int _pawls;
    /** available rotors. */
    private HashMap<String, Rotor> _availableRotors;
    /** Rotors. */
    private Rotor[] _rotors;
}
