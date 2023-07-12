package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/** Creates a staging area for the commit.
 * @author Jacky Zhao
 * */
public class StagingArea implements Serializable {
    /** Tracks the Commits in the staging area with the string. */
    private HashMap<String, String> adding;
    /** Tracks the removed files in the staging area. */
    private ArrayList<String> removing;

    /** Initializes a new staging area. */
    public StagingArea() {
        adding = new HashMap<>();
        removing = new ArrayList<>();
    }

    /** Adds onto the Hashmap to save each hash code.
     * @param name
     *  is the filename passed in.
     * @param sha1
     *  is the commit. */
    public void add(String name, String sha1) {
        adding.put(name, sha1);
    }

    /** Adds onto the list to save each removed file name.
     * @param name is the filename passed in. */
    public void addToRm(String name) {
        removing.add(name);
    }

    /** Removes the files in the Hashmap.
     * @param name is the filename passed in.
     *  */
    public void rmAdd(String name) {
        adding.remove(name);
    }

    /** Removes from the list.
     * @param name is the filename passed in. */
    public void rmFromRm(String name) {
        removing.remove(name);
    }

    /** Clears the staging area. */
    public void clear() {
        adding = new HashMap<>();
        removing = new ArrayList<>();
    }

    /** Returns the Hashmap of all the files that need to be added. */
    public HashMap<String, String> getAddFiles() {
        return adding;
    }

    /** Returns list of all the files to be removed. */
    public ArrayList<String> getRmFiles() {
        return removing;
    }
}
