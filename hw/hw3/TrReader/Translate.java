import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/** String translation.
 *  @author Jacky Zhao
 */
public class Translate {
    /** This method should return the String S, but with all characters that occur in FROM changed to
     *  the corresponding characters in TO. FROM and TO must have the same length.
     *  NOTE: You must use your TrReader to achieve this. */
    static String translate(String S, String from, String to) {
        /* NOTE: The try {...} catch is a technicality to keep Java happy. */
        char[] buffer = new char[S.length()];
        try {
            TrReader a = new TrReader(new StringReader(S), from, to);
            a.read(buffer, 0, buffer.length);
            return new String(buffer);
        } catch (IOException e) {
            return null;
        }
    }
    /*
       REMINDER: translate must
      a. Be non-recursive
      b. Contain only 'new' operations, and ONE other method call, and no
         other kinds of statement (other than return).
      c. Use only the library classes String, and any classes with names
         ending with "Reader" (see online java documentation). */
}
