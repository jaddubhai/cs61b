package tablut;

import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static tablut.Piece.*;
import static tablut.Square.*;
import static tablut.Move.mv;


/** The state of a Tablut Game.
 *  @author
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
        init();
        // FIXME
    }

    /** Clears the board to the initial position. */
    void init() {
        for (int i = 0; i < INITIAL_ATTACKERS.length; i++) {
            int row = INITIAL_ATTACKERS[i].row();
            int col = INITIAL_ATTACKERS[i].col();
            _allSquares[row][col] = BLACK;
        }
        for (int i = 0; i < INITIAL_DEFENDERS.length; i++) {
            int row = INITIAL_DEFENDERS[i].row();
            int col = INITIAL_DEFENDERS[i].col();
            _allSquares[row][col] = WHITE;
        }
        _allSquares[THRONE.row()][THRONE.col()] = KING;
    }

    /** Set the move limit to LIM.  It is an error if 2*LIM <= moveCount(). */
    void setMoveLimit(int n) {
        // FIXME
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
        // FIXME
    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return _moveCount;
    }

    /** Return location of the king. */
    Square kingPosition() {
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
        // FIXME
    }

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
        // FIXME
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /** Return true iff FROM - TO is an unblocked rook move on the current
     *  board.  For this to be true, FROM-TO must be a rook move and the
     *  squares along it, other than FROM, must be empty. */
    boolean isUnblockedMove(Square from, Square to) {
        if (from.isRookMove(to)) {
            int dir = from.direction(to);
            int steps;
            if (dir == 0 || dir == 2) {
                steps = to.row() - from.row();
            } else {
                steps = to.col() - from.col();
            }
            if (steps < 0) {
                steps = steps*-1;
            }
            return from.rookMove(dir, steps) == null;
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

        return checkturn && checkpaths && checkmove;
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /** Move FROM-TO, assuming this is a legal move. */
    void makeMove(Square from, Square to) {
        assert isLegal(from, to);
        Move move = Move.mv(from, to);
        _moves.add(move);
        _moveCount += 1;
        // need to update squares and keep track of moves
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to());
    }

    private void capture(Square sq0, Square sq1, Square sq2, Square sq3) {
        if (_allSquares[sq0.row()][sq0.col()] == BLACK
                && _allSquares[sq1.row()][sq1.col()] == BLACK
                && _allSquares[sq2.row()][sq2.col()] == BLACK
                && _allSquares[sq3.row()][sq3.col()] == BLACK) {
            Square captured = sq0.between(sq2);

            if (_allSquares[captured.row()][captured.col()] == KING) {
                _SquareMap.remove(captured);
                _SquareMap.put(captured, null);
                _allSquares[captured.row()][captured.col()] = null;
            }
        }
    }

    /** Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     *  SQ0 and the necessary conditions are satisfied. */
    private void capture(Square sq0, Square sq2) {
        Square captured = sq0.between(sq2);
        if (captured == THRONE || captured == NTHRONE ||
                captured == ETHRONE || captured == WTHRONE || captured == STHRONE) {
            capture(captured.rookMove(0, 1),
                    captured.rookMove(1, 1),
                    captured.rookMove(2, 1),
                    captured.rookMove(3, 1));
        }

        _SquareMap.remove(captured);
        _SquareMap.put(captured, null);
        _allSquares[captured.row()][captured.col()] = null;
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            // FIXME
        }
    }

    /** Remove record of current position in the set of positions encountered,
     *  unless it is a repeated position or we are at the first move. */
    private void undoPosition() {
        // FIXME
        _repeated = false;
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    void clearUndo() {
        // FIXME
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        return null;  // FIXME
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
    private HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;
        HashSet<Square> retset = new HashSet<Square>();
        for (int i = 0; i < 9;  i++) {
            for (int j = 0; j < 9; j++) {
                if (_allSquares[i][j] == side) {
                    retset.add(Square.sq(i, j));
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

    /** Tracks king position */
    private Square _kingposition;

    /** Tracks all squares */
    private Piece[][] _allSquares = new Piece[9][9];

    /** Tracks all moves */
    private Move.MoveList _moves = new Move.MoveList();

    /** Tracks pieces in squares */
    private HashMap<Square, Piece> _SquareMap = new HashMap<Square, Piece>();

    /** Square List. Possible matching with _allSquares */
    private SqList _squarelist = new SqList();

    // FIXME: Other state?

}
