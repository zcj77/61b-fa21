package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Jacky Zhao
 */
public final class Main {
    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Check for all the bad settings. */
    private void badSettings() {
        String[] check = checkSettings;
        int numrtr = enigma.numRotors();
        boolean[] rotrExists = new boolean[numrtr + 1];
        rotrExists[0] = true;

        for (int i = 1; i < check.length; i++) {
            if (i >= numrtr + 1) {
                String p = "^([A-Za-z()]+|[0-9]+)$";
                if (!(Pattern.matches(p, check[i]))) {
                    throw new EnigmaException("Bad character in setting.");
                }
            }
            else if (check[i - 1].equals(check[i])) {
                throw new EnigmaException("Repeated Rotor");
            }
            for (Rotor a: all) {
                if (check[i].equals(a.name())) {
                    rotrExists[i] = true;
                    break;
                }
            }
        }
        for (boolean c: rotrExists) {
            if (!c) {
                throw new EnigmaException("Bad rotor name.");
            }
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        String s = "";
        enigma = readConfig();
        String next = _input.nextLine();

        if (!next.contains("*")) {
            throw new EnigmaException("Nothing passed in.");
        } else {
            checkSettings = next.split(" ");
            badSettings();
        }

        while (_input.hasNext()) {
            if (!next.contains("*")) {
                throw new EnigmaException("Wrong format for setting");
            }
            setUp(enigma, next);
            next = _input.nextLine();

            while (!(next.contains("*"))) {
                String res = enigma.convert(next.replaceAll(" ", ""));
                if (next.isEmpty()) {
                    _output.println();
                } else {
                    printMessageLine(res);
                }
                if (_input.hasNext()) {
                    next = _input.nextLine();
                    s = "hi";
                } else {
                    next = "*";
                }
            }
        }

        while (s.equals("hi")) {
            if (_input.hasNextLine()) {
                _output.println();
                s = _input.nextLine();
                s += "hi";
            } else {
                break;
            }
        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String a = _config.next();
            if (a.contains("*") || a.contains("(") || a.contains(")")) {
                throw new EnigmaException("Wrong configuration format.");
            }
            _alphabet = new Alphabet(a);

            if (!_config.hasNextInt()) {
                throw new EnigmaException("Wrong configuration format.");
            }
            int nrotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Wrong configuration format.");
            }
            int paws = _config.nextInt();
            nxt =  _config.next();
            while (_config.hasNext()) {
                nombre = nxt;
                notches = _config.next();
                all.add(readRotor());
            }
            return new Machine(_alphabet, nrotors, paws, all);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String cycles = "";
            nxt = _config.next();
            while (nxt.contains("(") && _config.hasNext()) {
                cycles += nxt + " ";
                nxt = _config.next();
            }
            if (!_config.hasNext()) {
                cycles += nxt + " ";
            }


            Permutation perm = new Permutation(cycles, _alphabet);
            if (notches.charAt(0) == 'R') {
                return new Reflector(nombre, perm);
            } else if (notches.charAt(0) == 'N') {
                return new FixedRotor(nombre, perm);
            }
            return new MovingRotor(nombre, perm, notches.substring(1));

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] cycles = settings.split(" ");
        if (cycles.length - 1 < M.numRotors()) {
            throw new EnigmaException("Not enough arguments in setting");
        }

        String[] r = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i++) {
            r[i] = cycles[i + 1];
        }

        for (int i = 0; i < r.length - 1; i++) {
            for (int j = 0; j < r.length && i != j; j++) {
                if (r[i].equals(r[j])) {
                    throw new EnigmaException("Rotor is Repeated.");
                }
            }
        }

        String s = "";
        for (int i = r.length + 2; i < cycles.length; i++) {
            s = s.concat(cycles[i] + " ");
        }
        M.insertRotors(r);
        if (!M.get(0).reflecting()) {
            throw new EnigmaException("First rotor should reflect.");
        }
        M.setRotors(cycles[M.numRotors() + 1]);
        M.setPlugboard(new Permutation(s, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            int max = msg.length() - i;
            if (max <= 5) {
                _output.println(msg.substring(i, i + max));
            } else {
                _output.print(msg.substring(i, i + 5) + " ");
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** The next configuration. */
    private String nxt;

    /** Name of the current rotor. */
    private String nombre;

    /** Notches of the current rotor. */
    private String notches;

    /** An ArrayList that has all the rotors. */
    private ArrayList<Rotor> all = new ArrayList<>();

    /** Array of String to check for bad settings. */
    private String[] checkSettings = new String[all.size()];

    /** The e is the enigma machine. */
    private Machine enigma;
}
