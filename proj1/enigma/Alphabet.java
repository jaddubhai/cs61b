package enigma;

import java.util.HashMap;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Varun Jadia
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _alphabet = chars;
        char[] charray = _alphabet.toCharArray();

        HashMap<Character, Integer> map = new HashMap<>();
        for (char c : charray) {
            if (map.containsKey(c)) {
                throw new EnigmaException(
                        "Only unique characters in alphabet!");
            }
            map.put(c, 1);
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alphabet.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        return _alphabet.indexOf(ch) != -1;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _alphabet.charAt(index);
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return _alphabet.indexOf(ch);
    }

    /** alphabet in consideration. */
    private String _alphabet;

}
