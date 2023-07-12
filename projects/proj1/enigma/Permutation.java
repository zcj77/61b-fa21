package enigma;

import java.util.List;
import java.util.Arrays;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jacky Zhao
 */
class Permutation {
    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String c = cycles.replace("(", "").replace(")", " ");
        cycless = Arrays.asList(c.split(" "));
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        cycless.add(cycle);
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
        char c = _alphabet.toChar(wrap(p));
        for (String cycle: cycless) {
            int len = cycle.length();
            for (int i = 0; i < len; i++) {
                char a = cycle.charAt(wrap((i + 1) % len));
                if (cycle.charAt(i) == c) {
                    return _alphabet.toInt(a);
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to C modulo the alphabet size. */
    int invert(int c) {
        char n = _alphabet.toChar(wrap(c));
        for (String cycle: cycless) {
            for (int i = 0; i < cycle.length(); i++) {
                char charr = cycle.charAt(wrap(i - 1) % cycle.length());
                if (i == 0) {
                    charr = cycle.charAt(cycle.length() - 1);
                }
                if (cycle.charAt(i) == n) {
                    return _alphabet.toInt(charr);
                }
            }
        }
        return c;
    }


    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int count = 0;
        for (String c: cycless) {
            count += c.length();
        }
        return count == _alphabet.size();
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** List of strings to keep track of cycles. */
    private List<String> cycless;

    private String[] cycler;
}
