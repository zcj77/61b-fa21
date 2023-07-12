package jump61;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.function.Consumer;

import static jump61.Side.*;
import static jump61.Square.square;

/** Represents the state of a Jump61 game.  Squares are indexed either by
 *  row and column (between 1 and size()), or by square number, numbering
 *  squares by rows, with squares in row 1 numbered from 0 to size()-1, in
 *  row 2 numbered from size() to 2*size() - 1, etc. (i.e., row-major order).
 *
 *  A Board may be given a notifier---a Consumer<Board> whose
 *  .accept method is called whenever the Board's contents are changed.
 *
 *  @author Jacky Zhao
 */
class Board {
    /** An uninitialized Board.  Only for use by subtypes. */
    protected Board() {
        _notifier = NOP;
    }

    /** An N x N board in initial configuration. */
    Board(int N) {
        this();
        _board = new Square[N * N];
        for (int j = 0; j < N * N; j++) {
            _board[j] = square(WHITE, 1);
        }
        _history = new ArrayList<>();
        _readonlyBoard = new ConstantBoard(this);
    }

    /** A board whose initial contents are copied from BOARD0, but whose
     *  undo history is clear, and whose notifier does nothing. */
    Board(Board board0) {
        this(board0.size());
        copy(board0);
        _history.clear();
        _notifier = NOP;
    }

    /** Returns a readonly version of this board. */
    Board readonlyBoard() {
        return _readonlyBoard;
    }

    /** (Re)initialize me to a cleared board with N squares on a side. Clears
     *  the undo history and sets the number of moves to 0. */
    void clear(int N) {
        Board n = new Board(N);
        _board = n._board;
        _history.clear();
        announce();
    }

    /** Copy the contents of BOARD into me. */
    void copy(Board board) {
        _board = new Square[size() * size()];
        internalCopy(board);
    }

    /** Copy the contents of BOARD into me, without modifying my undo
     *  history. Assumes BOARD and I have the same size. */
    private void internalCopy(Board board) {
        assert size() == board.size();
        for (int j = 0; j < size() * size(); j++) {
            _board[j] = board.get(j);
        }
    }

    /** Return the number of rows and of columns of THIS. */
    int size() {
        return (int) Math.sqrt(_board.length);
    }

    /** Returns the contents of the square at row R, column C
     *  1 <= R, C <= size (). */
    Square get(int r, int c) {
        return get(sqNum(r, c));
    }


    /** Returns the contents of square #N, numbering squares by rows, with
     *  squares in row 1 number 0 - size()-1, in row 2 numbered
     *  size() - 2*size() - 1, etc. */
    Square get(int n) {
        return _board[n];
    }

    /** Returns the total number of spots on the board. */
    int numPieces() {
        int res = 0;
        for (Square s: _board) {
           if (s.getSide() != WHITE){
               res += s.getSpots();
           }
            res += s.getSpots();
        }
        return res;
    }

    /** Returns the Side of the player who would be next to move.  If the
     *  game is won, this will return the loser (assuming legal position). */
    Side whoseMove() {
        return ((numPieces() + size()) & 1) == 0 ? RED : BLUE;
    }

    /** Return true iff row R and column C denotes a valid square. */
    final boolean exists(int r, int c) {
        return 1 <= r && r <= size() && 1 <= c && c <= size();
    }

    /** Return true iff S is a valid square number. */
    final boolean exists(int s) {
        int N = size();
        return 0 <= s && s < N * N;
    }

    /** Return the row number for square #N. */
    final int row(int n) {
        return n / size() + 1;
    }

    /** Return the column number for square #N. */
    final int col(int n) {
        return n % size() + 1;
    }

    /** Return the square number of row R, column C. */
    final int sqNum(int r, int c) {
        return (c - 1) + (r - 1) * size();
    }

    /** Return a string denoting move (ROW, COL)N. */
    String moveString(int row, int col) {
        return String.format("%d %d", row, col);
    }

    /** Return a string denoting move N. */
    String moveString(int n) {
        return String.format("%d %d", row(n), col(n));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
        to square at row R, column C. */
    boolean isLegal(Side player, int r, int c) {
        return isLegal(player, sqNum(r, c));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
     *  to square #N. */
    boolean isLegal(Side player, int n) {
        return exists(n) && isLegal(player);
    }

    /** Returns true iff PLAYER is allowed to move at this point. */
    boolean isLegal(Side player) {
        return numOfSide(player.opposite()) != _board.length;
    }

    /** Returns the winner of the current position, if the game is over,
     *  and otherwise null. */
    final Side getWinner() {
        if (!isLegal(BLUE)) {
            return RED;
        } else if (!isLegal(RED)) {
            return BLUE;
        }
        return null;
    }

    /** Return the number of squares of given SIDE. */
    int numOfSide(Side side) {
        int res = 0;
        for (Square s: _board) {
            if (s.getSide().equals(side)) {
                res++;
            }
        }
        return res;
    }

    /** Add a spot from PLAYER at row R, column C.  Assumes
     *  isLegal(PLAYER, R, C). */
    void addSpot(Side player, int r, int c) {
        addSpot(player, sqNum(r, c));
    }

    /** Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N). */
    void addSpot(Side player, int n) {
        markUndo();
<<<<<<< HEAD
        if (get(n).getSpots() >= neighbors(n) && player == whoseMove()) {
            System.out.println("Jump");
=======
        if (player == whoseMove()) {
>>>>>>> de5e4f192123616b39edd365547d3672bd644fb3
            jump(n);
        }
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white). */
    void set(int r, int c, int num, Side player) {
        internalSet(r, c, num, player);
        announce();
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white).  Does not announce
     *  changes. */
    private void internalSet(int r, int c, int num, Side player) {
        internalSet(sqNum(r, c), num, player);
    }

    /** Set the square #N to NUM spots (0 <= NUM), and give it color PLAYER
     *  if NUM > 0 (otherwise, white). Does not announce changes. */
    private void internalSet(int n, int num, Side player) {
        if (exists(n)) {
            _board[n] = square(player, num);
        }
    }

    /** Undo the effects of one move (that is, one addSpot command).  One
     *  can only undo back to the last point at which the undo history
     *  was cleared, or the construction of this Board. */
    void undo() {
        copy(_history.remove(_history.size() - 1));
    }

    /** Record the beginning of a move in the undo history. */
    private void markUndo() {
        Board n = new Board(this);
        _history.add(n);

    }

    /** Add DELTASPOTS spots of side PLAYER to row R, column C,
     *  updating counts of numbers of squares of each color. */
    private void simpleAdd(Side player, int r, int c, int deltaSpots) {
        internalSet(r, c, deltaSpots + get(r, c).getSpots(), player);
    }

    /** Add DELTASPOTS spots of color PLAYER to square #N,
     *  updating counts of numbers of squares of each color. */
    private void simpleAdd(Side player, int n, int deltaSpots) {
        internalSet(n, deltaSpots + get(n).getSpots(), player);
    }

    /** Used in jump to keep track of squares needing processing.  Allocated
     *  here to cut down on allocations. */
    private final ArrayDeque<Integer> _workQueue = new ArrayDeque<>();

    /** Do all jumping on this board, assuming that initially, S is the only
     *  square that might be over-full. */
    private void jump(int S) {
        help(row(S), col(S), whoseMove());
    }

    /** Helper function for jumping on the board given R, C, and PLAYER. */
    private void help(int r, int c, Side player) {
<<<<<<< HEAD
        simpleAdd(player, r, c, 1);
        if (getWinner() == null) {
            if (get(r, c).getSpots()  > neighbors(r, c)) {
=======
        get(r, c).addjumps();
        simpleAdd(player, r, c, 1);
        if (getWinner() == null) {
            if (get(r, c).getSpots() > neighbors(r, c)) {
>>>>>>> de5e4f192123616b39edd365547d3672bd644fb3
                set(r, c, 1, player);

                int[] moves = { sqNum(r, c + 1), sqNum(r, c - 1),
                                sqNum(r + 1, c), sqNum(r - 1, c) };

                for (Integer i: moves) {
                    if (isLegal(player, i) && row(i) == r
                            || isLegal(player, i) && col(i) == c) {
                        help(row(i), col(i), player);
                    }
                }
            }
<<<<<<< HEAD
=======
        }
    }

    /** Returns the list of possible moves given a PLAYER. */
    public ArrayList<Integer> legalMoves(Side player) {
        ArrayList<Integer> moves = new ArrayList<>();
        for (int i = 0; i < _board.length; i++) {
            if (isLegal(player, i)) {
                moves.add(i);
            }
        }
        return moves;
    }


    /** Returns the best move based on jumps, given MOVES and P. */
    public int bestMove(Side p) {
        int ndx = 0, best = 0;
        for (int i = 0; i < _board.length; i++) {
            if (isLegal(p, i)) {
                if (isLegal(p, i) && get(i).getjumps() > best) {
                    best = get(i).getjumps();
                    ndx = i;
                }
            }
>>>>>>> de5e4f192123616b39edd365547d3672bd644fb3
        }
        return get(ndx).getjumps();
    }

    /** Returns my dumped representation. */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("\n===");
        for (int i = 1; i <= size(); i++) {
            out.format("\n   ");
            for (int j = 1; j <= size(); j++) {
                Square S = _board[sqNum(i, j)];
                String spots = String.valueOf(S.getSpots());
                String color = S.getSide().toString().substring(0, 1);
                if (S.getSide() == WHITE) {
                    color = "-";
                }
                out.format(" %s", spots + color);
            }
        }
        out.format("\n===");
        return out.toString();
    }

    /** Returns an external rendition of me, suitable for human-readable
     *  textual display, with row and column numbers.  This is distinct
     *  from the dumped representation (returned by toString). */
    public String toDisplayString() {
        String[] lines = toString().trim().split("\\R");
        Formatter out = new Formatter();
        for (int i = 1; i + 1 < lines.length; i += 1) {
            out.format("%2d %s%n", i, lines[i].trim());
        }
        out.format("  ");
        for (int i = 1; i <= size(); i += 1) {
            out.format("%3d", i);
        }
        return out.toString();
    }

    /** Returns the number of neighbors of the square at row R, column C. */
    int neighbors(int r, int c) {
        int size = size();
        int n;
        n = 0;
        if (r > 1) {
            n += 1;
        }
        if (c > 1) {
            n += 1;
        }
        if (r < size) {
            n += 1;
        }
        if (c < size) {
            n += 1;
        }
        return n;
    }

    /** Returns the number of neighbors of square #N. */
    int neighbors(int n) {
        return neighbors(row(n), col(n));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Board)) {
            return false;
        } else {
            Board B = (Board) obj;
            for (int i = 0; i < size() * size(); i++) {
                if (!_board[i].equals(B.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        return numPieces();
    }

    /** Set my notifier to NOTIFY. */
    public void setNotifier(Consumer<Board> notify) {
        _notifier = notify;
        announce();
    }

    /** Take any action that has been set for a change in my state. */
    private void announce() {
        _notifier.accept(this);
    }

    /** A notifier that does nothing. */
    private static final Consumer<Board> NOP = (s) -> { };

    /** A read-only version of this Board. */
    private ConstantBoard _readonlyBoard;

    /** Use _notifier.accept(B) to announce changes to this board. */
    private Consumer<Board> _notifier;

    /** The board itself. */
    private Square[] _board;

    /** Add Boards to history to allow for undo. */
    private ArrayList<Board> _history = new ArrayList<>();
}
