package jump61;

import java.util.ArrayList;
import java.util.Random;

import static jump61.Side.*;

/** There are two obvious ways to conduct a game-tree search in the AI.
 //
 // First, you can explore the consequences of a possible move from
 // position A by making a copy of the Board in position A, and then
 // modifying that copy.  Since you retain position A, you can return to
 // it to try other moves from that position.  With this strategy, you
 // will not need the undo method and can remove it from Board and
 // its subclasses.
 //
 // Second, you can explore the consequences of a possible move from
 // position A by making that move on your Board and then, when your
 // analysis of the move is complete, undoing the move to return you to
 // position A.  This method is more complicated to implement, but has
 // the advantage that it can be considerably faster than making copies
 // of the Board (you will need one copy per move tried, which will very
 // quickly be thrown away).


/** An automated Player.
 *  @author P. N. Hilfinger
 */
class AI extends Player {

    /** A new player of GAME initially COLOR that chooses moves automatically.
     *  SEED provides a random-number seed used for choosing moves.
     */
    AI(Game game, Side color, long seed) {
        super(game, color);
        _random = new Random(seed);
    }

    @Override
    String getMove() {
        Board board = getGame().getBoard();
        assert getSide() == board.whoseMove();
        int choice = searchForMove();
        getGame().reportMove(board.row(choice), board.col(choice));
        return String.format("%d %d", board.row(choice), board.col(choice));
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private int searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert getSide() == work.whoseMove();
        _foundMove = -1;
        if (getSide() == RED) {
            value = minMax(work, 3, true, 1, _INF, -_INF);
        } else {
            value = minMax(work, 3, true, -1, _INF, -_INF);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove,
                       int sense, int alpha, int beta) {

        if (depth == 0 || board.getWinner() != null) {
            return staticEval(board, _INF);
        }

        int best = -sense * _INF, m;
        ArrayList<Integer> moves = board.legalMoves(board.whoseMove());

        if (sense == 1) {
            for (Integer i: moves) {
                board.addSpot(RED, i);
                m = minMax(board, depth - 1, false, -1, alpha, beta);
                board.undo();
                if (m > best) {
                    best = m;
                    alpha = Math.max(alpha, best);
                    if (beta <= alpha) {
                        _foundMove = i;
                        break;
                    }
                }
            }
            return best;
        } else {
            for (Integer i : moves) {
                board.addSpot(BLUE, i);
                m = minMax(board, depth - 1, false, 1, alpha, beta);
                board.undo();
                if (m < best) {
                    best = m;
                    beta = Math.min(beta, best);
                    if (beta <= alpha) {
                        _foundMove = i;
                        break;
                    }
                }
            }
            return best;
        }
    }

    /** Return a heuristic estimate of the value of board position B.
     *  Use WINNINGVALUE to indicate a win for Red and -WINNINGVALUE to
     *  indicate a win for Blue. */
    private int staticEval(Board b, int winningValue) {
        if (b.getWinner() == RED) {
            return winningValue;
        } else if (b.getWinner() == BLUE) {
            return -winningValue;
        }
        return b.bestMove(getSide());
    }

    /** A random-number generator used for move selection. */
    private Random _random;

    /** Used to convey moves discovered by minMax. */
    private int _foundMove;

    /** Used to find the min evaluation or max evaluation for the bestMove. */
    private int _INF = Integer.MAX_VALUE;
}
