package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Varun Jadia
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);

        if (notches == null) {
            _notches = null;
        }

        _notches = new int[notches.length()];

        int counter = 0;
        while (counter < notches.length()) {
            _notches[counter] = alphabet().toInt(notches.charAt(counter));
            counter++;
        }
    }


    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    @Override
    boolean atNotch() {
        if (_notches == null) {
            return false;

        }
        for (int notch : _notches) {
            if (setting() == notch) {
                return true;
            }
        }

        return false;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void setringset(String set) {
        assert (alphabet().contains(set.charAt(0)));
        set(wrap(alphabet().toInt(set.charAt(0)) + setting()));

        for (int i = 0; i < _notches.length; i++) {
            _notches[i] = wrap(_notches[i] -  alphabet().toInt(set.charAt(0)));
        }
    }

    /**wrap.*/
    /** @param p
    /** @return */
    final int wrap(int p) {
        int r = p % alphabet().size();
        if (r < 0) {
            r += alphabet().size();
        }
        return r;
    }

    /** notches. */
    private int[] _notches;
}
