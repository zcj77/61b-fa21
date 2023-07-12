package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import ucb.junit.textui;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void testInvertChar() {
        Permutation perm = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('A', perm.alphabet().toChar(0));
        assertEquals('B', perm.alphabet().toChar(1));
        assertEquals('C', perm.alphabet().toChar(2));
        assertEquals('D', perm.alphabet().toChar(3));

        assertEquals('B', perm.invert('A'));
        assertEquals('D', perm.invert('B'));

        perm = getNewPermutation("(AELTPHQXRU)", getNewAlphabet("AELTPHQXRU"));

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

        perm = getNewPermutation("(HIG)(NF) (L)", getNewAlphabet("HILFNGR"));
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
