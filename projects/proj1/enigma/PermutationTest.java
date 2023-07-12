package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Jacky Zhao
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */
    private Permutation perm;
    private String alpha = UPPER_STRING;

    private ArrayList<Rotor> rotors = ALL_ROTORS;
    private Machine machine;
    private String[] insert = {"B", "BETA", "III", "IV", "I"};

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setMachine(Alphabet a, int numrotors,
                            int pawls, Collection<Rotor> allrotors) {
        machine = new Machine(a, numrotors, pawls, allrotors);
    }

    /* ***** TESTS ***** */
    @Test
    public void testInsertRotors() {
        setMachine(UPPER, 5, 3, rotors);
        machine.insertRotors(insert);
        assertEquals("Wrong rotor at 0", rotors.get(0), machine.get(0));
        assertEquals("Wrong rotor at 4", rotors.get(2), machine.get(4));
    }

    @Test
    public void testSetRotors() {
        setMachine(UPPER, 5, 3, rotors);
        machine.insertRotors(insert);
        machine.setRotors("AXLE");
        assertEquals("Wrong setting at 1", 0, machine.get(1).setting());
        assertEquals("Wrong setting at 2", 23, machine.get(2).setting());
        assertEquals("Wrong setting at 3", 11, machine.get(3).setting());
        assertEquals("Wrong setting at 4", 4, machine.get(4).setting());
    }

    @Test
    public void testConvert() {
        setMachine(UPPER, 5, 3, rotors);
        machine.insertRotors(insert);
        machine.setRotors("AXLE");
        machine.setPlugboard(
                new Permutation("(HQ) (EX) (IP) (TR) (BY)", UPPER));
        assertEquals("Wrong convert", "QVPQ", machine.convert("FROM"));
        setMachine(UPPER, 5, 3, rotors);
        machine.insertRotors(insert);
        machine.setRotors("AXLE");
        machine.setPlugboard(
                new Permutation("(HQ) (EX) (IP) (TR) (BY)", UPPER));
        assertEquals("Wrong convert", "FROM", machine.convert("QVPQ"));
    }


    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testPerm() {
        Alphabet a = new Alphabet("ABCD");
        perm = new Permutation("(BACD)", a);
        assertEquals('A', perm.alphabet().toChar(0));
        assertEquals(4, perm.size());
        assertEquals(0, perm.wrap(0));

        assertEquals('B', perm.invert('A'));
        assertEquals('D', perm.invert('B'));

        perm = new Permutation("(AELTPHQXRU)", new Alphabet("AELTPHQXRU"));
        assertEquals(1, perm.wrap(1));
        assertEquals(7, perm.wrap(-3));
        assertEquals(0, perm.wrap(20));
        assertEquals(6, perm.wrap(16));

        assertEquals('E', perm.permute('A'));
        assertEquals('L', perm.permute('E'));
        assertEquals('T', perm.permute('L'));
        assertEquals('P', perm.permute('T'));
        assertEquals('H', perm.permute('P'));
        assertEquals('Q', perm.permute('H'));
        assertEquals('X', perm.permute('Q'));
        assertEquals('R', perm.permute('X'));
        assertEquals('U', perm.permute('R'));
        assertEquals('A', perm.permute('U'));

        assertEquals('U', perm.invert('A'));
        assertEquals('A', perm.invert('E'));
        assertEquals('E', perm.invert('L'));
        assertEquals('L', perm.invert('T'));
        assertEquals('T', perm.invert('P'));
        assertEquals('P', perm.invert('H'));
        assertEquals('H', perm.invert('Q'));
        assertEquals('Q', perm.invert('X'));
        assertEquals('X', perm.invert('R'));
        assertEquals('R', perm.invert('U'));

        assertEquals(true, perm.derangement());

        perm = new Permutation("(HIG)(NF)(L)", new Alphabet("HILFNGR"));
        assertEquals('I', perm.permute('H'));
        assertEquals('G', perm.permute('I'));
        assertEquals('L', perm.permute('L'));
        assertEquals('N', perm.permute('F'));
        assertEquals('F', perm.permute('N'));
        assertEquals('H', perm.permute('G'));
        assertEquals('R', perm.permute('R'));

        assertEquals('G', perm.invert('H'));
        assertEquals('H', perm.invert('I'));
        assertEquals('L', perm.invert('L'));
        assertEquals('N', perm.invert('F'));
        assertEquals('F', perm.invert('N'));
        assertEquals('I', perm.invert('G'));
        assertEquals('R', perm.invert('R'));

        assertEquals(false, perm.derangement());
    }
}
