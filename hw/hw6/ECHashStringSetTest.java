import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {
    ECHashStringSet e = new ECHashStringSet();

    @Test
    public void tests() {
        ECHashStringSet ECHSS = new ECHashStringSet();
        ECHSS.put("Apple");
        ECHSS.put("Boots");
        ECHSS.put("Jeans");
        ECHSS.put("Bottom");

        assertTrue(ECHSS.contains("Apple"));
        assertTrue(ECHSS.contains("Boots"));
        assertTrue(ECHSS.contains("Jeans"));
        assertTrue(ECHSS.contains("Bottom"));
        assertFalse(ECHSS.contains("Appl"));

        for (LinkedList<String> l: ECHSS.buckets) {
            System.out.println(l);
        }

    }
}
