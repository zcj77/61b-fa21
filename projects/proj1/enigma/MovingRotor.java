package enigma;


/** Class that represents a rotating rotor in the enigma machine.
 *  @author Jacky Zhao
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        noches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < noches.length(); i++) {
            if (alphabet().toInt(noches.charAt(i)) == this.setting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        set(this.setting() + 1);
    }

    /** Current notch passed in. */
    private String noches;
}
