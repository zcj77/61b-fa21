package gitlet;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;


import static gitlet.Utils.*;

/** Gitlet class, the tiny version-control system of git and contains the
 * commands to be able to use gitlet.
 *  @author Jacky Zhao
 */
public class Gitlet implements Serializable {
    /** Current branch name. */
    private String _head = "master";
    /** The stage for the staging area. */
    private StagingArea stage;
    /** The Current Working Directory. */
    private static File cwd = new File(System.getProperty("user.dir"));
    /** The parent of the current commit. */
    private Commit p;
    /** The current commit for global log. */
    private Commit gl;
    /** Sees whether there is a conflict in files before committing. */
    private boolean conflicted = false;
    /** Keeps track of the Commit Tree for merge. */
    private HashMap<String, Commit> cTree;

    /** Folder of the gitlet. */
    static final File GITLET_FOLDER = join(cwd, ".gitlet");
    /** Folder of the blobs. */
    static final File BLOBS = join(GITLET_FOLDER, "blobs");
    /** Folder of the branches. */
    static final File BRANCHES = join(GITLET_FOLDER, "branches");
    /** Folder of the commits. */
    static final File COMMITS = join(GITLET_FOLDER, "commits");
    /** Folder of the blobs. */
    static final File GL = join(GITLET_FOLDER, "global-log");
    /** Folder of the staging area. */
    static final File STAGING_AREA = join(GITLET_FOLDER, "staging_area");

    /** Creates a new Gitlet object. Keeps track of many collections involving
     * files and commits. _head is the name of the head branch and stage
     * represents the staging area for committing files. */
    public Gitlet() {
        if (join(BRANCHES, "HEAD").exists()) {
            _head = readContentsAsString(join(BRANCHES, "HEAD"));
        }
        File s = join(STAGING_AREA, "stage");
        if (s.exists()) {
            stage = readObject(s, StagingArea.class);
        }
    }

    /** Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit: a commit with
     * no files and has the message initial commit. It will have one branch:
     * master, which initially points to this initial commit, and master will
     * be the current branch. The timestamp for this initial commit will be
     * 00:00:00 UTC, Thursday, 1 January 1970 in whatever format you choose.
     * Since the initial commit in all repositories created by Gitlet will
     * have exactly the same content, it follows that all repositories will
     * automatically share this commit (they'll all have the same UID) and
     * all commits in all repositories will trace back to it. */
    public void init() {
        if (GITLET_FOLDER.exists()) {
            System.out.println("A gitlet version control system "
                    + "already exists in the current directory.");
        } else {
            GITLET_FOLDER.mkdirs();
            BLOBS.mkdirs();
            BRANCHES.mkdirs();
            COMMITS.mkdirs();
            GL.mkdirs();
            STAGING_AREA.mkdirs();
            Commit initCom = new Commit("initial commit", null, null);

            String hash = initCom.getHash();
            writeObject(join(COMMITS, hash), initCom);
            writeContents(join(BRANCHES, _head), hash);
            writeContents(new File(".gitlet/branches/HEAD"), "master");

            stage = new StagingArea();
            writeObject(join(STAGING_AREA, "stage"), stage);
        }
    }

    /** Adds a copy of the file that exists in the staging area (see commit).
     * Adding a file is also staging the file for addition. Staging an
     * already-staged file overwrites the previous entry in the staging area
     * with new contents. If the current working version of the file is
     * identical to the version in the current commit, don't stage it to be
     * added, and remove it from the staging area if it's there (can happen
     * when a file is changed, added, and then changed back). The file is no
     * longer be staged for removal, if it was at the time of the command.
     *
     * @param name is the filename passed in. */
    public void add(String name) {
        File add = join(cwd, name);
        if (!add.exists()) {
            System.out.print("File does not exist.");
        } else {
            String hash = sha1(readContents(add));
            String toAdd = getCommit().getTracking().get(name);
            if (toAdd != null && toAdd.equals(hash)) {
                if (stage.getRmFiles().contains(name)) {
                    writeObject(join(STAGING_AREA, "stage"), stage);
                }
                return;
            }
            if (stage.getRmFiles().contains(name)) {
                stage.getRmFiles().remove(name);
            }
            writeContents(join(BLOBS, hash), readContents(add));
            stage.add(name, hash);
            writeObject(join(STAGING_AREA, "stage"), stage);
        }
    }

    /** Saves a snapshot of tracked files in the current commit and staging area
     * so they can be restored later, creating a new commit. The commit tracks
     * the saved files. By default, each commit's snapshot of files will be the
     * same as its parent's snapshot of files; it will keep versions of files
     * as they are, and not update them. It will only update the contents of
     * files it is tracking that have been staged for addition at the time of
     * commit, which now includes the version of the file that was staged
     * instead of the version it got from its parent. A commit will save and
     * start tracking files that were staged for addition but weren't tracked by
     * its parent. Finally, files tracked in the current commit may be untracked
     * in the new commit as a result being staged for removal.
     *
     * @param msg is the message of the commit input by the user. */
    public void commit(String msg) {
        HashMap<String, String> added = stage.getAddFiles();
        ArrayList<String> removed = stage.getRmFiles();
        if (msg.equals("")) {
            System.out.println("Please enter a commit message.");
        } else if (removed.isEmpty() && added.isEmpty()) {
            System.out.println("No changes added to the commit.");
        } else {
            Commit parent = getCommit();
            HashMap<String, String> trackedFiles = parent.getTracking();
            Set<String> filesToAdd = added.keySet();
            for (String name: filesToAdd) {
                trackedFiles.put(name, added.get(name));
            }
            for (String rm: removed) {
                trackedFiles.remove(rm);
            }
            parent.setTracking(trackedFiles);

            Commit c = new Commit(msg, parent, trackedFiles);
            writeContents(join(BRANCHES, _head), c.getHash());
            writeObject(join(COMMITS, c.getHash()), c);

            stage.clear();
            writeObject(join(STAGING_AREA, "stage"), stage);
        }
    }

    /** Unstage the file if it is currently staged for addition. If the file is
     * tracked in the current commit, stage it for removal and remove the file
     * from the working directory if the user has not already done so
     * (don't remove it unless it is tracked in the current commit).
     *
     * @param name is the filename passed in. */
    public void rm(String name) {
        HashMap<String, String> added = stage.getAddFiles();
        if (isTracked(getCommit(), name)) {
            restrictedDelete(name);
            stage.addToRm(name);
            if (added.containsKey(name)) {
                stage.rmAdd(name);
            }
            writeObject(join(STAGING_AREA, "stage"), stage);
        } else if (added.containsKey(name)) {
            stage.rmAdd(name);
            writeObject(join(STAGING_AREA, "stage"), stage);
        } else {
            System.out.println("No reason to remove the file.");
        }
    }

    /** Starting at the current head commit, display information about each
     * commit backwards along the commit tree until the first commit, following
     * the first parent commit links, ignoring any second parents found in merge
     * commits. (In Git, this is what you get with git log --first-parent).
     * This set of commit nodes is called the commit's history. For every node
     * in this history, the information it should display is the commit id, the
     * time, and the commit message. */
    public void log() {
        for (Commit c = getCommit(); c != null; c = c.getParent()) {
            System.out.println(c.reportLog());
        }
    }

    /** Like log, except displays information about all commits ever made.
     * The order doesn't matter. Hint: there is a useful method in Utils
     * that will help you iterate over files within a directory. */
    public void globalLog() {
        if (!join(GL, "gl").exists()) {
            gl = getCommit();
            writeContents(GL, gl.getHash());
        }
        if (gl != null) {
            System.out.println(gl.reportLog());
            gl = gl.getParent();
        }
    }

    /** Prints out all commits ids that have the commit message, 1 per line.
     * If there are multiple such commits, it prints them on separate lines. The
     * commit message is a single operand; for a multiword message, put the
     * operand in quotation marks, as for the commit command above.
     *
     * @param msg is the message of the commit input by the user. */
    public void find(String msg) {
        int count = 0;
        for (String s: COMMITS.list()) {
            Commit c = readObject(join(COMMITS, s), Commit.class);
            if (c.getMessage().equals(msg)) {
                System.out.println(c.getHash());
                count++;
            }
        }
        if (count == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    /** Displays what branches currently exist, marks the current branch,
     * and displays what files have been staged for addition/removal. */
    public void status() {
        if (!GITLET_FOLDER.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        ArrayList<String> branches = new ArrayList<>();
        if (BRANCHES.exists()) {
            File[] files = BRANCHES.listFiles();
            for (File f: files) {
                branches.add(f.getName());
            }
        }
        branches.remove("HEAD");
        branches.remove(_head);
        branches.add("*" + _head);
        Collections.sort(branches);

        System.out.println("=== Branches ===");
        for (String b: branches) {
            System.out.println(b);
        }

        System.out.println("\n=== Staged Files ===");
        if (join(STAGING_AREA, "stage").exists()) {
            for (String s: stage.getAddFiles().keySet()) {
                System.out.println(s);
            }
        }

        System.out.println("\n=== Removed Files ===");
        if (join(STAGING_AREA, "stage").exists()) {
            for (String s : stage.getRmFiles()) {
                System.out.println(s);
            }
        }
        System.out.println("\n=== Modifications Not Staged For Commit ===");

        System.out.println("\n=== Untracked Files ===");
    }

    /** Depending on which type of checkout, it calls the helper function.
     * @param args is the operands for the checkout command. */
    public void checkout(String[] args) {
        if (args.length == 2) {
            checkoutBranch(args[1]);
        } else if (args.length == 3 && args[1].equals("--")) {
            checkoutFile(args[2]);
        } else if (args.length == 4 && args[2].equals("--")) {
            checkoutCommit(args[1], args[3]);
        } else {
            System.out.println("Incorrect operands.");
        }
    }

    /** Creates a new branch with the given name, and points it at the current
     * head node. A branch is nothing more than a name for a reference
     * (a SHA-1 identifier) to a commit node. This command DOESN'T immediately
     * switch to the newly created branch (just as in real Git). Before you
     * ever call branch, your code should be running with a default branch
     * called "master".
     *
     * @param name is the file name. */
    public void branch(String name) {
        File f = join(BRANCHES, name);
        if (f.exists()) {
            System.out.print("A branch with that name already exists.");
        } else {
            String s = readContentsAsString(join(BRANCHES, _head));
            writeContents(join(BRANCHES, name), s);
        }
    }

    /** Deletes the branch with the given name. This only means to delete the
     * pointer associated with the branch; it does not mean to delete all
     * commits that were created under the branch, or anything like that.
     *
     * @param name is the filename passed in. */
    public void rmBranch(String name) {
        if (name.equals(readContentsAsString(join(BRANCHES, "HEAD")))) {
            System.out.print("Cannot remove the current branch.");
        } else if (!join(BRANCHES, name).delete()) {
            System.out.print("A branch with that name does not exist.");
        }
    }

    /** Checks out all the files tracked by the given commit. Removes tracked
     * files that aren't present in that commit. Also moves the current branch's
     * head to that commit node. See the intro for an example of what happens
     * to the head pointer after using reset. The [commit id] may be abbreviated
     * as for checkout. The staging area is cleared. The command is essentially
     * checkout of an arbitrary commit that also changes the head branch.
     *
     * @param id is the ID of the commit. */
    public void reset(String id) {
        Commit c = readObject(join(COMMITS, id), Commit.class);
        if (!join(COMMITS, id).exists() || c == null) {
            System.out.println("No commit with that id exists.");
        } else {
            c.conflicted(cwd, getCommit().getTracking());
            stage.clear();
            writeContents(join(BRANCHES, "HEAD"), id);
            writeObject(join(STAGING_AREA, "stage"), stage);
        }
    }

    /** Merges files from the given branch into the current branch.
     *
     * @param name is the filename passed in. */
    public void merge(String name) {
        if (!ready2Merge(name)) {
            return;
        }
        Commit cCommit = getCommit();
        String bCID = readContentsAsString(join(BRANCHES, name));
        Commit bCommit = readObject(join(COMMITS, bCID), Commit.class);
        cCommit.conflicted(cwd, bCommit.getTracking());

        updateCTree(cCommit, bCommit, bCID);

        HashMap<String, String> cMap = cCommit.getTracking();
        HashMap<String, String> bMap = bCommit.getTracking();
        HashMap<String, String> pMap = p.getTracking();

        for (String s: bMap.keySet()) {
            if (!pMap.keySet().contains(s)) {
                checkout(new String[]{"checkout", bCID, "--", s});
                add(s);
            }
        }
        for (String s: pMap.keySet()) {
            if (cMap.containsKey(s) && !bMap.keySet().contains(s)) {
                if (pMap.get(s).equals(cMap.get(s))) {
                    rm(s);
                }
            }
        }
        if (conflicted) {
            System.out.println("Encountered a merge conflict.");
        } else {
            commit("Merged " + _head + " with " + name + ".");
        }
    }


    /* HELPER METHODS */

    /** Returns the current commit. */
    public Commit getCommit() {
        File f = join(BRANCHES, "HEAD");
        String currentBranch = readContentsAsString(f);
        String hash = readContentsAsString(join(BRANCHES, currentBranch));
        return readObject(join(COMMITS, hash), Commit.class);
    }

    /**
     * Takes the version of the file as it exists in the head commit, the front
     * of the current branch, and puts it in the working directory, overwriting
     * the version of the file that's already there if there is one. The new
     * version of the file is not staged.
     * @param name is the file name. */
    public void checkoutBranch(String name) {
        Commit curr = getCommit();
        if (!join(BRANCHES, name).exists()) {
            System.out.println("No such branch exists.");
        } else if (name.equals(_head)) {
            System.out.println("No need to checkout the current branch.\n");
        } else {
            String branch = readContentsAsString(join(BRANCHES, name));
            Commit c = readObject(join(COMMITS, branch), Commit.class);
            curr.conflicted(cwd, c.getTracking());
            stage.clear();
            writeObject(join(STAGING_AREA, "stage"), stage);
            writeContents(join(BRANCHES, "HEAD"), name);
        }
    }

    /**
     * Takes the version of the file as it exists in the commit with the id, and
     * puts it in the working directory, overwriting the version of the file
     * that's already there. The new version of the file isn't staged.
     * @param name is the file name. */
    public void checkoutFile(String name) {
        HashMap<String, String> trackedFiles = getCommit().getTracking();
        if (!trackedFiles.containsKey(name)) {
            System.out.println("File does not exist in that commit.");
        } else {
            if (join(cwd, name).exists()) {
                restrictedDelete(join(cwd, name));
            }

            File blob = join(BLOBS, trackedFiles.get(name));
            String content = readContentsAsString(blob);
            writeContents(join(cwd, name), content);
        }
    }

    /**
     * Takes all files in the commit at the head of the branch, and puts them in
     * the working directory, overwriting the files that already exist. Also, at
     * the end of this command, the given branch will now be considered the
     * current branch (HEAD). Any files that are tracked in the current branch
     * but aren't present in the checked-out branch are deleted. The staging
     * area is cleared, unless the checked-out branch is the current branch.
     * @param name is the file name.
     * @param id is the commit id. */
    public void checkoutCommit(String id, String name) {
        for (String s: COMMITS.list()) {
            if (s.contains(id)) {
                id = s;
                break;
            }
        }
        if (!join(COMMITS, id).exists()) {
            System.out.println("No commit with that id exists.");
        } else {
            Commit c = readObject(join(COMMITS, id), Commit.class);
            if (!c.getTracking().containsKey(name)) {
                System.out.println("File does not exist in that commit.");
            } else {
                String b = c.getTracking().get(name);
                writeContents(join(cwd, name), readContents(join(BLOBS, b)));
            }
        }
    }

    /** Returns true whether the commit is ready to merge.
     * @param name is the file name. */
    public boolean ready2Merge(String name) {
        HashMap<String, String> added = stage.getAddFiles();
        ArrayList<String> removed = stage.getRmFiles();
        if (!added.isEmpty() && !removed.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return false;
        } else if (!join(BRANCHES, name).exists()) {
            System.out.println("A branch with that name does not exist.");
            return false;
        } else if (name.equals(_head)) {
            System.out.println("Cannot merge a branch with itself.");
            return false;
        }
        return true;
    }

    /** Updates the commit tree. Helps check if a given branch is an
     * ancestor of the current branch. Prints errors if there is any.
     * @param cComm is the current commit.
     * @param bComm is the branch that is being committed.
     * @param id is the commit id of that branch. */
    public void updateCTree(Commit cComm, Commit bComm,
                                            String id) {
        cTree = new HashMap<>();

        cComm = findBranchPath(cComm);
        bComm = findBranchPath2(bComm);

        String print = "There was an error finding the split point.";
        if (p == null) {
            System.out.println(print);
        }
        if (p.getHash().equals(cComm.getHash())) {
            writeContents(join(BRANCHES, _head), bComm);
            System.out.println("Current branch fast-forwarded.");
            return;
        } else if (cTree.containsKey(bComm)) {
            print = "Given branch is an ancestor of the current branch.";
            System.out.print(print);
            return;
        }

        HashMap<String, String> cMap = cComm.getTracking();
        HashMap<String, String> bMap = cComm.getTracking();
        for (String s: cMap.keySet()) {
            String ch = cMap.get(s);
            String bh = bMap.get(s);
            if (p.getTracking().containsKey(s)) {
                String pHash = p.getTracking().get(s);
                if (bMap.containsKey(s)) {
                    if (!pHash.equals(bh) && pHash.equals(ch)) {
                        checkout(new String[]{"checkout", id, "--", s});
                        add(s);
                        writeObject(join(STAGING_AREA, "stage"), stage);
                    }
                    if (!pHash.equals(bh) || !pHash.equals(ch)
                            || !bh.equals(ch)) {
                        mergeFormat2(ch, bh, s);
                        conflicted = true;
                    }
                } else if (!bMap.containsKey(s) && !pHash.equals(ch)) {
                    mergeFormat(ch, bh, s);
                    conflicted = true;
                }
            }
        }
    }

    /** Returns a commit given the branch. Helps check if a given branch is an
     * ancestor of the current branch.
     * @param c is the current commit. */
    public Commit findBranchPath(Commit c) {
        Commit comm = readObject(join(COMMITS, c.getHash()), Commit.class);
        while (c != null && comm != null) {
            cTree.put(c.getHash(), c);
            if (c.getParent() != null) {
                File parent = join(COMMITS, c.getParent().getHash());
                c = readObject(parent, Commit.class);
            } else {
                break;
            }
        }
        return c;
    }

    /** Returns a commit given the branch. Helps check if a given branch is an
     * ancestor of the current branch.
     * @param c is the current commit. */
    public Commit findBranchPath2(Commit c) {
        Commit comm = readObject(join(COMMITS, c.getHash()), Commit.class);
        while (c != null && comm != null) {
            if (cTree.containsKey(c.getHash())) {
                p = cTree.get(c.getHash());
                break;
            }
            if (c.getParent() != null) {
                File parent = join(COMMITS, c.getParent().getHash());
                c = readObject(parent, Commit.class);
            } else {
                break;
            }
        }
        return c;
    }

    /** One of the Formats for merge.
     * @param c is the current commit.
     * @param b is the branch that is being committed.
     * @param name is the file name. */
    public void mergeFormat(String c, String b, String name) {
        File merge = join(cwd, name);
        byte[] all = concat("<<<<<<< HEAD\n".getBytes(StandardCharsets.UTF_8),
                readContents(join(BLOBS, c)));
        all = concat(all, "=======\n".getBytes(StandardCharsets.UTF_8));
        all = concat(all, ">>>>>>>\n".getBytes(StandardCharsets.UTF_8));
        writeContents(merge, all);
    }

    /** Format for merge.
     * @param c is the current commit.
     * @param b is the branch that is being committed.
     * @param name is the file name. */
    public void mergeFormat2(String c, String b, String name) {
        File merge = join(cwd, name);
        byte[] all = concat("<<<<<<< HEAD\n".getBytes(StandardCharsets.UTF_8),
                readContents(join(BLOBS + c)));
        all = concat(all, "=======\n".getBytes(StandardCharsets.UTF_8));
        all = concat(all, readContents(join(BLOBS, b)));
        all = concat(all, ">>>>>>>\n".getBytes(StandardCharsets.UTF_8));
        writeContents(merge, all);
    }

    /** Returns the contents in bytes after concatenating them.
     * @param toAdd is the content that needs to be added to.
     * @param knew is the new stuff being added to print out the format. */
    private byte[] concat(byte[] toAdd, byte[] knew) {
        byte[] res = new byte[toAdd.length + knew.length];
        System.arraycopy(toAdd, 0, res, 0, toAdd.length);
        System.arraycopy(knew, 0, res, toAdd.length, knew.length);
        return res;
    }
}
