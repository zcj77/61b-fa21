package jump61;

import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;


import java.util.concurrent.ArrayBlockingQueue;

/** The GUI controller for jump61.  To require minimal change to textual
 *  interface, we adopt the strategy of converting GUI input (mouse clicks)
 *  into textual commands that are sent to the Game object through a
 *  a Writer.  The Game object need never know where its input is coming from.
 *  A Display is an Observer of Games and Boards so that it is notified when
 *  either changes.
 *  @author Jacky Zhao
 */
class Display extends TopLevel implements View, CommandSource, Reporter {

    /** A new window with given TITLE displaying GAME, and using COMMANDWRITER
     *  to send commands to the current game. */
    Display(String title) {
        super(title, true);

        addMenuButton("Game->Quit", this::quit);
        addMenuButton("Game->New Game", this::newGame);

        addMenuButton("Options->Red Manual", this::rm);
        addMenuButton("Options->Red AI", this::rai);
        addMenuButton("Options->Blue Manual", this::bm);
        addMenuButton("Options->Blue AI", this::bai);

        addMenuButton("Options->Size", this::size);

        _boardWidget = new BoardWidget(_commandQueue);
        add(_boardWidget, new LayoutSpec("y", 1, "width", 2));
        display(true);
    }

    /** Response to "Quit" button click. */
    void quit(String dummy) {
        System.exit(0);
    }

    /** Response to "New Game" button click. */
    void newGame(String dummy) {
        _commandQueue.offer("new");
    }

    /** Response to red manual. */
    void rm(String dummy) {
        _commandQueue.offer("manual red");
    }

    /** Response to red AI. */
    void rai(String dummy) {
        _commandQueue.offer("auto red");
    }

    /** Response to blue manual. */
    void bm(String dummy) {
        _commandQueue.offer("manual blue");
    }

    /** Response to blue AI. */
    void bai(String dummy) {
        _commandQueue.offer("auto blue");
    }

    /** Response to size. */
    void size(String dummy) {
        String s = getTextInput("size", "size", "plain", null);
        _commandQueue.offer("size " + s);
    }


    @Override
    public void update(Board board) {
        if (_boardWidget == null) {
            _boardWidget = new BoardWidget(_commandQueue);
        }

        _boardWidget.update(board);
        pack();
        _boardWidget.repaint();
    }

    @Override
    public String getCommand(String ignored) {
        try {
            return _commandQueue.take();
        } catch (InterruptedException excp) {
            throw new Error("unexpected interrupt");
        }
    }

    @Override
    public void announceWin(Side side) {
        showMessage(String.format("%s wins!", side.toCapitalizedString()),
                    "Game Over", "information");
    }

    @Override
    public void announceMove(int row, int col) {
    }

    @Override
    public void msg(String format, Object... args) {
        showMessage(String.format(format, args), "", "information");
    }

    @Override
    public void err(String format, Object... args) {
        showMessage(String.format(format, args), "Error", "error");
    }

    /** Time interval in msec to wait after a board update. */
    static final long BOARD_UPDATE_INTERVAL = 50;

    /** The widget that displays the actual playing board. */
    private BoardWidget _boardWidget;
    /** Queue for commands going to the controlling Game. */
    private final ArrayBlockingQueue<String> _commandQueue =
        new ArrayBlockingQueue<>(5);
}
