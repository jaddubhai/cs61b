package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _CYCLE = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _CYCLE += cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int wrapped = wrap(p);
        char permuted = permute(_alphabet.toChar(wrapped));
        return _alphabet.toInt(permuted);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int wrapped = wrap(c);
        char permuted = invert(_alphabet.toChar(wrapped));
        return _alphabet.toInt(permuted);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {

        if (_alphabet.contains(p) == false){
            return p;
        }

        if (_CYCLE == null){
            return p;
        }

        String s = Character.toString(p);

        if (_CYCLE.indexOf(s) != -1){
            char nxtChar = _CYCLE.charAt(_CYCLE.indexOf(s) + 1);
            if (nxtChar == ')'){
                int currIndex = _CYCLE.indexOf(s);
                while (_CYCLE.charAt(currIndex - 1) != '('){
                    currIndex--;
                }
                return _CYCLE.charAt(currIndex);
            } else {
                return  _CYCLE.charAt(_CYCLE.indexOf(s) + 1);
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (_alphabet.contains(c) == false){
            return c;
        }

        if (_CYCLE == null){
            return c;
        }

        String s = Character.toString(c);

        if (_CYCLE.indexOf(s) != -1){
            char nxtChar = _CYCLE.charAt(_CYCLE.indexOf(s) - 1);
            if (nxtChar == '('){
                int currIndex = _CYCLE.indexOf(s);
                while (_CYCLE.charAt(currIndex + 1) != ')'){
                    currIndex++;
                }
                return _CYCLE.charAt(currIndex);
            } else {
                return  _CYCLE.charAt(_CYCLE.indexOf(s) - 1);
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int count = 0;

        while (count < _alphabet.size()){
            if (_alphabet.toChar(count) == permute(_alphabet.toChar(count))){
                return false;
            }
        }

        return true;
    }

    private String _CYCLE;

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

}
