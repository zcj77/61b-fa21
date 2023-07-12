import afu.org.checkerframework.checker.igj.qual.I;
import antlr.CharBuffer;
import antlr.preprocessor.Hierarchy;

import javax.swing.*;
import java.io.Reader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


/** Translating Reader: a stream that is a translation of an existing reader.
*  @author Jacky Zhao
*
*  NOTE: Until you fill in the right methods, the compiler will reject this file, saying that
 *  you must declare TrReader abstract.  Don't do that; define the right methods instead!
*/
public class TrReader extends Reader {
    Reader str;
    String from, to;
    /** A new TrReader that produces the stream of characters produced by STR, converting all characters
     *  that occur in FROM to the corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters in STR unchanged.
     *  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) throws IOException {
        this.str = str;
        this.from = from;
        this.to = to;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int num = 0;
        for (int i = 0; i < len; i++) {
            int n = str.read();
            if (n == -1) {
                if (i < 3 && cbuf.length>i+off) {
                    return -1;
                }
                break;
            }
            char curr = (char)n;
            if (from.indexOf(curr) != -1) {
                curr = to.charAt(from.indexOf(curr));
            }
            cbuf[off+i] = curr;
            num++;
        }
        return num;
    }

    @Override
    public void close() throws IOException {
    }
}
