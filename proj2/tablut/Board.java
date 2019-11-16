package tablut;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;

import static tablut.Piece.*;
import static tablut.Square.*;

/** The state of a Tablut Game.
 *  @author Varun Jadia
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 9;

    /** The throne (or castle) square and its four surrounding squares.. */
    static final Square THRONE = sq(4, 4),
        NTHRONE = sq(4, 5),
        STHRONE = sq(4, 3),
        WTHRONE = sq(3, 4),
        ETHRONE = sq(5, 4);

    /** Initial positions of attackers. */
    static final Square[] INITIAL_ATTACKERS = {
        sq(0, 3), sq(0, 4), sq(0, 5), sq(1, 4),
        sq(8, 3), sq(8, 4), sq(8, 5), sq(7, 4),
        sq(3, 0), sq(4, 0), sq(5, 0), sq(4, 1),
        sq(3, 8), sq(4, 8), sq(5, 8), sq(4, 7)
    };

    /** Initial positions of defenders of the king. */
    static final Square[] INITIAL_DEFENDERS = {
        NTHRONE, ETHRONE, STHRONE, WTHRONE,
        sq(4, 6), sq(4, 2), sq(2, 4), sq(6, 4)
    };

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        if (model == this) {
            return;
        }
        _allSquares = model._allSquares.clone();
        _winner = model.winner();
        _turn = model.turn();
        _kingmoves = model.getkingmoves();
        _kingposition = model.kingPosition();
        _encodedboards = (ArrayList<String>) model._encodedboards.clone();
        _positionhash =  (ArrayList<Tracker>) model._positionhash.clone();
        _edgemuscovites = model._edgemuscovites;
        _moves = (Move.MoveList) model._moves.clone();
        _moveCount = model._moveCount;
        _movelimit = model._movelimit;
        _repeated = model._repeated;
    }

    /** Clears the board to the initial position. */
    void init() {
        _positionhash.clear();
        _encodedboards.clear();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                _allSquares[i][j] = EMPTY;
            }
        }

        for (int i = 0; i < INITIAL_ATTACKERS.length; i++) {
            int row = INITIAL_ATTACKERS[i].row();
            int col = INITIAL_ATTACKERS[i].col();
            _allSquares[col][row] = BLACK;
        }
        for (int i = 0; i < INITIAL_DEFENDERS.length; i++) {
            int row = INITIAL_DEFENDERS[i].row();
            int col = INITIAL_DEFENDERS[i].col();
            _allSquares[col][row] = WHITE;
        }
        _allSquares[THRONE.col()][THRONE.row()] = KING;
        _kingposition = Square.sq(THRONE.col(), THRONE.row());
        _moveCount = 0;
        _moves = new Move.MoveList();
        _turn = BLACK;
        _positionhash.add(new Tracker(this));
        _winner = null;
    }

    /** Set the move limit to LIM.  It is an error if 2*LIM <= moveCount(). */
    /** @param n */
    void setMoveLimit(int n) {
        _movelimit = n;
    }

    /** Return a Piece representing whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the winner in the current position, or null if there is no winner
     *  yet. */
    Piece winner() {
        return _winner;
    }

    /** Returns true iff this is a win due to a repeated position. */
    boolean repeatedPosition() {
        return _repeated;
    }

    /** Record current position and set winner() next mover if the current
     *  position is a repeat. */
    private void checkRepeated() {

        if (_encodedboards != null && _encodedboards.contains(encodedBoard())) {
            int index = _encodedboards.indexOf(encodedBoard());
            if (_encodedboards.get(index).charAt(0)
                    == _turn.toString().charAt(0)) {
                _winner = _turn;
                _repeated = true;
            }
        }
    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return _moves.size();
    }

    /** Return location of the king. */
    Square kingPosition() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (_allSquares[i][j] == KING) {
                    _kingposition = Square.sq(i, j);
                }
            }
        }
        return _kingposition;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        assert (Square.exists(col, row));
        return _allSquares[col][row];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        _allSquares[s.col()][s.row()] = p;
    }

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
        put(p, s);
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /** Return true iff FROM - TO is an unblocked rook move on the current
     *  board.  For this to be true, FROM-TO must be a rook move and the
     *  squares along it, other than FROM, must be empty. */
    boolean isUnblockedMove(Square from, Square to) {
        Square sway = null;
        if (from.isRookMove(to)) {
            for (Square s: ROOK_SQUARES[from.index()][from.direction(to)]) {
                if (s.equals(to)) {
                    sway = s;
                    break;
                }
                if (getpiece(s) != EMPTY) {
                    return false;
                }
            }
            return get(sway).equals(EMPTY);
        }
        return false;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /** Return true iff FROM-TO is a valid move. */
    boolean isLegal(Square from, Square to) {
        boolean checkturn = isLegal(from);
        boolean checkpaths = isUnblockedMove(from, to);
        boolean checkmove = from.isRookMove(to);

        if (to.equals(THRONE)) {
            if (get(from) != KING) {
                return false;
            }
        }
        return checkturn && checkpaths && checkmove;
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /** returns opponent. */
    Piece opponent() {
        if (_turn == WHITE) {
            return BLACK;
        }
        return WHITE;
    }

    /** checks if a SQ is hostile. return.
     */
    boolean ishostile(Square sq) {
        Piece hostile = _allSquares[sq.col()][sq.row()];
        if (sq.equals(THRONE)) {
            if (hostile == EMPTY) {
                return true;

            } else if (turn() == WHITE) {
                int hostilecount = 0;
                Square[] adj = {NTHRONE, STHRONE, ETHRONE, WTHRONE};
                for (int i = 0; i < 4; i++) {
                    if (getpiece(adj[i]) == BLACK) {
                        hostilecount++;
                    }
                }
                return hostilecount == 3;
            }
        }
        return hostile.side() == _turn;
    }

    /** Move FROM-TO, assuming this is a legal move. */
    void makeMove(Square from, Square to) {
        Piece topiece = _allSquares[from.col()][from.row()];

        _allSquares[from.col()][from.row()] = EMPTY;
        _allSquares[to.col()][to.row()] = topiece;


        for (int dir = 0; dir < 4; dir++) {
            Square capbud = to.rookMove(dir, 2);
            try {
                if (!ishostile(capbud)) {
                    continue;
                } else {
                    capture(to, capbud);
                }
            } catch (NullPointerException excp) {
                continue;
            }
        }
        if (kingPosition() != null && kingPosition().isEdge()) {
            _winner = WHITE;
        }

        if (legalMoves(WHITE) == null) {
            _winner = BLACK;
        } else if (legalMoves(BLACK) == null) {
            _winner = WHITE;
        }
        _turn = _turn.opponent();
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        int dummy;
        if (move == null) {
            dummy = 0;
        } else {
            makeMove(move.from(), move.to());
            _moves.add(move);
            _moveCount += 1;
            checkRepeated();
            _positionhash.add(new Tracker(this));
            _encodedboards.add(encodedBoard());
        }
    }

    /** capture for kings/throne SQ0 SQ1 SQ2 SQ3.
    */
    private void capture(Square sq0, Square sq1, Square sq2, Square sq3) {
        Square captured = sq0.between(sq2);

        if (ishostile(sq0) && ishostile(sq1)
                && ishostile(sq2) && ishostile(sq3)
                && getpiece(captured).side() == _turn.opponent()) {
            if (captured == kingPosition()) {
                _allSquares[captured.col()][captured.row()] = EMPTY;
                _winner = BLACK;
            }
        }
    }

    /** return piece given a SQ. */
    private Piece getpiece(Square sq) {
        return _allSquares[sq.col()][sq.row()];
    }

    /** Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     *  SQ0 and the necessary conditions are satisfied. */
    private void capture(Square sq0, Square sq2) {
        Square captured = sq0.between(sq2);
        Piece capiece = getpiece(captured);

        if ((captured.equals(THRONE) || captured.equals(NTHRONE)
                || captured.equals(ETHRONE) || captured.equals(WTHRONE)
                || captured.equals(STHRONE)) && capiece.equals(KING)) {
            capture(captured.rookMove(0, 1),
                    captured.rookMove(1, 1),
                    captured.rookMove(2, 1),
                    captured.rookMove(3, 1));

        } else if (ishostile(sq0)  && ishostile(sq2)
                && capiece.side() == _turn.opponent()) {
            _allSquares[captured.col()][captured.row()] = EMPTY;
        }
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            Tracker curr = _positionhash.get(_positionhash.size() - 1);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    _allSquares[i][j] = curr.squares[i][j];
                }
            }
            _moveCount -= 1;
            Piece[] copy = new Piece[2];
            copy[0] = convertchar(curr.boards.charAt(0));
            copy[1] = curr.currwin;
            _turn = copy[0];
            _winner = copy[1];
            _moves.remove(_moves.get(_moves.size() - 1));
            _encodedboards.remove(
                    _encodedboards.get(_encodedboards.size() - 1));
        }
    }

    /** convert X to return char.
     */
    private Piece convertchar(char x) {
        if (x == WHITE.toString().charAt(0)) {
            return WHITE;
        }
        return BLACK;
    }

    /** Remove record of current position in the set of positions encountered,
     *  unless it is a repeated position or we are at the first move. */
    private void undoPosition() {
        _positionhash.remove(_positionhash.size() - 1);
        _repeated = false;
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    void clearUndo() {
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        ArrayList<Square> squares = new ArrayList<Square>();
        ArrayList<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (_allSquares[i][j].side().equals(side)) {
                    squares.add(Square.sq(i, j));
                }
            }
        }

        for (Square square : squares) {
            SqList[] possibles = square.retmoves()[square.index()];
            for (SqList lst : possibles) {
                for (Square to : lst) {
                    Move move = Move.mv(square, to);
                    if (isLegal(move)) {
                        moves.add(move);
                        if (square == _kingposition) {
                            if (_kingmoves == null) {
                                _kingmoves = new ArrayList<Move>();
                            }
                            _kingmoves.add(move);
                        }
                    }
                }
            }
        }

        return moves;
    }

    /** Return true iff SIDE has a legal move. */
    boolean hasMove(Piece side) {
        return legalMoves(side) != null;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /** Return a text representation of this Board.  If COORDINATES, then row
     *  and column designations are included along the left and bottom sides.
     */
    String toString(boolean coordinates) {
        Formatter out = new Formatter();
        for (int r = SIZE - 1; r >= 0; r -= 1) {
            if (coordinates) {
                out.format("%2d", r + 1);
            } else {
                out.format("  ");
            }
            for (int c = 0; c < SIZE; c += 1) {
                out.format(" %s", get(c, r));
            }
            out.format("%n");
        }
        if (coordinates) {
            out.format("  ");
            for (char c = 'a'; c <= 'i'; c += 1) {
                out.format(" %c", c);
            }
            out.format("%n");
        }
        return out.toString();
    }

    /** Return the locations of all pieces on SIDE. */
    public HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;

        if (side.equals(BLACK)) {
            _edgemuscovites = 0;
        }
        HashSet<Square> retset = new HashSet<Square>();
        for (int i = 0; i < 9;  i++) {
            for (int j = 0; j < 9; j++) {
                if (_allSquares[i][j].side().equals(side)) {
                    retset.add(Square.sq(i, j));
                    if ((i == 0 || i == 8 || j == 0 || j == 8)
                            && side.equals(BLACK)) {
                        _edgemuscovites++;
                    }
                }
            }
        }
        return retset;
    }

    /** Return the contents of _board in the order of SQUARE_LIST as a sequence
     *  of characters: the toString values of the current turn and Pieces. */
    String encodedBoard() {
        char[] result = new char[Square.SQUARE_LIST.size() + 1];
        result[0] = turn().toString().charAt(0);
        for (Square sq : SQUARE_LIST) {
            result[sq.index() + 1] = get(sq).toString().charAt(0);
        }
        return new String(result);
    }

    /** Piece whose turn it is (WHITE or BLACK). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
    /** Number of (still undone) moves since initial position. */
    private int _moveCount;
    /** True when current board is a repeated position (ending the game). */
    private boolean _repeated;

    /** Tracks king position. */
    private Square _kingposition;

    /** Tracks all squares. */
    private Piece[][] _allSquares = new Piece[9][9];

    /** Tracks all moves. */
    private Move.MoveList _moves = new Move.MoveList();

    /** Keeps track of board positions. */
    private ArrayList<Tracker> _positionhash = new ArrayList<>();

    /** Keeps track of encoded boards. */
    private ArrayList<String> _encodedboards = new ArrayList<>();

    /** move limit. */
    private int _movelimit;

    /** Tracker class for positionhash. */
    private class Tracker {
        /** init. */
        /** @param x */
        Tracker(Board x) {
            squares = new Piece[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    squares[i][j] = _allSquares[i][j];
                }
            }
            boards = x.encodedBoard();
            currturn = x.turn();
            currwin = x.winner();
        }

        /** copy of squares. */
        private Piece[][] squares;
        /** copy of encoded board. */
        private String boards;
        /** curr turn. */
        private Piece currturn;
        /** current winner. */
        private Piece currwin;
    }

    /** keeps track of number of muscovites on edges.*/
    private int _edgemuscovites;

    /** keeps track of king legal moves. */
    private ArrayList<Move> _kingmoves;

    /** get king moves.
     * @return
     */
    public ArrayList<Move> getkingmoves() {
        return _kingmoves;
    }

    /** get edgemusc.
     * @return
     */
    public int getedgemuscovites() {
        return _edgemuscovites;
    }
}
