package enigma;

import java.util.Collection;


/** Class that represents a complete enigma machine.
 *  @author Jacky Zhao
 */
class Machine {
    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        nrotors = numRotors;
        pauls = pawls;
        all = allRotors;
        r = new Rotor[nrotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return nrotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return pauls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor a: all) {
                if (rotors[i].equals(a.name())) {
                    r[i] = a;
                    break;
                }
            }
        }
        if (r.length != rotors.length) {
            throw new EnigmaException("Rotors don't match in length.");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("First position of string wrong length.");
        }
        for (int i = 1; i < numRotors(); i++) {
            if (!_alphabet.contains(setting.charAt(i - 1))) {
                throw new EnigmaException("First pos of str not in alphabet.");
            }
            r[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        pboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        for (int i = numRotors() - 1; i > 0; i--) {
            for (int j = 0; j < numPawls(); j++) {
                if (i - j > 0) {
                    if (r[i].atNotch()) {
                        r[i - 1].advance();
                    } else {
                        break;
                    }
                }
            }
        }
        r[nrotors - 1].advance();
        int res = pboard.permute(c);
        for (int i = nrotors - 1; i >= 0; i--) {
            res = r[i].convertForward(res);
        }
        for (int i = 1; i < nrotors; i++) {
            res = r[i].convertBackward(res);
        }

        return pboard.permute(res);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String res = "";
        for (int i = 0; i < msg.length(); i++) {
            int ndx = convert(_alphabet.toInt(msg.charAt(i)));
            res += _alphabet.toChar(ndx);
        }
        return res;
    }

    /** Returns the rotor at position P to check if it's reflecting. */
    Rotor get(int p) {
        return r[p];
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Keeps track of number of rotors. */
    private final int nrotors;

    /** Keeps track of number of pawls. */
    private final int pauls;

    /** Keeps track of all the rotors. */
    private final Collection<Rotor> all;

    /** Keeps track of the rotors. */
    private Rotor[] r;

    /** Plugboard for permutation. */
    private Permutation pboard;
}
