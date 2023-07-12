package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.*;

/** Commit class for Gitlet, used for committing and other gitlet commands.
 *  @author Jacky Zhao
 */
public class Commit implements Serializable {
    /** The name. */
    private String hash;
    /** The parent hash. */
    private Commit parent;
    /** The date and time of the commit. */
    private Date time;
    /** The log message. */
    private String message;
    /** Keeps track of the Commit based on the hash name. */
    private HashMap<String, String> tracking;
    /** Checks if the commit has a conflict with merge before checkout. */
    private boolean conflicted = false;

    /** Initializes a new commit.
     *
     * @param msg is the message of the commit.
     * @param pHash is the hash of the parent.
     * @param files is the Hashmap of the commits.
     * */
    public Commit(String msg, Commit pHash, HashMap<String, String> files) {
        if (pHash == null) {
            message = "initial commit";
            tracking = new HashMap<>();
            time = new Date(0);
        } else {
            message = msg;
            parent = pHash;
            time = new Date();
            tracking = files;
        }
        hash = sha1(serialize(this));
    }

    /** Returns the message of the commit. */
    public String getMessage() {
        return message;
    }

    /** Returns the Hash name of the commit. */
    public String getHash() {
        return hash;
    }

    /** Returns the time of the commit. */
    public Date getTime() {
        return time;
    }

    /** Returns the parent of the commit. */
    public Commit getParent() {
        return parent;
    }

    /** Returns whether there's a merge conflict.
     *
     * @param dir is the directory.
     * @param n is the hashmap of the new commit.
     * */
    public void conflicted(File dir, HashMap<String, String> n) {
        File[] files = dir.listFiles();
        HashMap<String, String> c = getTracking();
        for (File f: files) {
            String name = f.getName();
            if (!c.containsKey(name) && n.containsKey(name)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                return;
            }
        }
        for (File f: files) {
            String name = f.getName();
            if (!n.containsKey(name) && c.containsKey(name)) {
                restrictedDelete(f);
            }
        }

        for (String f: n.keySet()) {
            String blob = dir.getPath() + "/.gitlet/blobs/" + n.get(f);
            writeContents(new File(f), readContents(new File(blob)));
        }
    }

    /** Returns the list of tracked files. */
    public HashMap<String, String> getTracking() {
        return tracking;
    }

    /** Sets the parent of the commit.
     * @param pHash is the hash of the parent. */
    public void setParent(Commit pHash) {
        parent = pHash;
    }

    /** Sets the tracked files of the commit.
     * @param newTracking is where files are being tracked in the commit.
     */
    public void setTracking(HashMap<String, String> newTracking) {
        tracking = newTracking;
    }

    /** Returns the message log of the commit. */
    public String reportLog() {
        SimpleDateFormat date
                = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z");

        String res = "===\ncommit " + hash + "\nDate: " + date.format(time);
        res += "\n" + message;
        if (!message.equals("initial commit")) {
            res += "\n";
        }
        return res;
    }
}
