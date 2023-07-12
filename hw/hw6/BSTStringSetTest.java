import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {
    private BSTStringSet BST = new BSTStringSet();
    private ArrayList<String> exp = new ArrayList<>();

    @Test
    public void testput() {
        exp.add("how");
        exp.add("are");
        exp.add("you");
        exp.add("?");
        Collections.sort(exp);

        BST.put("how");
        BST.put("are");
        BST.put("you");
        BST.put("?");

        assertFalse(BST.contains("hello"));
        assertFalse(BST.contains("hell"));
        assertFalse(BST.contains(","));
        assertTrue(BST.contains("how"));
        assertTrue(BST.contains("?"));

        assertEquals(exp, BST.asList());
    }

    @Test
    public void testempty() {
        BST = new BSTStringSet();

        BST.put(" ");
        assertFalse(BST.contains("hello"));
        assertFalse(BST.contains("hell"));
        assertFalse(BST.contains(","));
        assertTrue(BST.contains(" "));
        assertFalse(BST.contains(""));

        exp.add(" ");
        assertEquals(exp, BST.asList());
    }
}