package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Jacky Zhao
 */
public class Main {
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0 || args == null) {
            System.out.println("Please enter a command.");
            return;
        }
        Gitlet git = new Gitlet();
        if (args[0].equals("init")) {
            validateNumArgs("init", args, 1);
            git.init();
        } else if (args[0].equals("add")) {
            validateNumArgs("add", args, 2);
            git.add(args[1]);
        } else if (args[0].equals("commit")) {
            validateNumArgs("commit", args, 2);
            git.commit(args[1]);
        } else if (args[0].equals("rm")) {
            validateNumArgs("rm", args, 2);
            git.rm(args[1]);
        } else if (args[0].equals("log")) {
            validateNumArgs("log", args, 1);
            git.log();
        } else if (args[0].equals("global-log")) {
            validateNumArgs("global-log", args, 1);
            git.globalLog();
        } else if (args[0].equals("find")) {
            validateNumArgs("find", args, 2);
            git.find(args[1]);
        } else if (args[0].equals("status")) {
            validateNumArgs("status", args, 1);
            git.status();
        } else if (args[0].equals("checkout")) {
            git.checkout(args);
        } else if (args[0].equals("branch")) {
            validateNumArgs("branch", args, 2);
            git.branch(args[1]);
        } else if (args[0].equals("rm-branch")) {
            validateNumArgs("rm-branch", args, 2);
            git.rmBranch(args[1]);
        } else if (args[0].equals("reset")) {
            validateNumArgs("reset", args, 2);
            git.reset(args[1]);
        } else if (args[0].equals("merge")) {
            validateNumArgs("merge", args, 2);
            git.merge(args[1]);
        } else {
            System.out.println("No command with that name exists.");
        }
        return;
    }


    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
