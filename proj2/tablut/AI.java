package tablut;

import java.util.List;

import static java.lang.Math.*;

import static tablut.Piece.*;

/** A Player that automatically generates moves.
 *  @author Varun Jadia
 */
class AI extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A position-score magnitude indicating a forced win in a subsequent
     *  move.  This differs from WINNING_VALUE to avoid putting off wins. */
    private static final int WILL_WIN_VALUE = Integer.MAX_VALUE - 40;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        findMove();
        System.out.println("* " + _lastFoundMove.toString());
        return _lastFoundMove.toString();
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        _lastFoundMove = null;
        int sense = ((myPiece() == BLACK) ? -1 : 1);

        findMove(board(), maxDepth(board()), true, sense, -1 * INFTY, INFTY);
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    public int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (board.winner() != null) {
            if (board.repeatedPosition() && depth >= maxDepth(board) - 2) {
                return sense * INFTY;
            }
            if (depth >= maxDepth(board) - 2) {
                return -sense * WINNING_VALUE;
            } else {
                return -sense * WILL_WIN_VALUE;
            }
        } else if (depth == 0) {
            return staticScore(board);
        }

        if (sense == -1) {
            int currbeta = INFTY;
            int newbeta;
            List<Move> muscomoves = board.legalMoves(BLACK);
            for (Move m : muscomoves) {
                board.makeMove(m);
                newbeta = findMove(board, depth - 1, false, 1, alpha, beta);
                board.undo();

                if (newbeta < currbeta) {
                    currbeta = Math.min(newbeta, currbeta);
                    if (saveMove) {
                        _lastFoundMove = m;
                    }
                }
                beta = Math.min(currbeta, beta);
                if (alpha >= beta) {
                    break;
                }
            }
            return currbeta;
        } else {
            int curralpha = -1 * INFTY;
            int newalpha;
            List<Move> swedeomoves = board.legalMoves(WHITE);
            for (Move m : swedeomoves) {
                board.makeMove(m);
                newalpha = findMove(board, depth - 1, false, -1, alpha, beta);
                board.undo();

                if (newalpha > curralpha) {
                    curralpha = Math.max(newalpha, curralpha);
                    if (saveMove) {
                        _lastFoundMove = m;
                    }
                }

                alpha = max(alpha, curralpha);

                if (alpha >= beta) {
                    break;
                }
            }
            return curralpha;
        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board) {
        return 4;
    }

    /** Return a heuristic value for BOARD. */
    public int staticScore(Board board) {
        return boardstate(myPiece(), board);
    }

    /** calculates a heuristic based on board state. BOARD. SIDE. return.**/

    public int boardstate(Piece side, Board board) {
        Square kingpos = board.kingPosition();
        if (kingpos == null) {
            return -1 * INFTY;
        }

        int row = min(kingpos.row(), 8 - kingpos.row());
        int col = min(kingpos.col(), 8 - kingpos.col());

        int minkingmov = Math.floorDiv(Math.min(row, col), 4);
        int diff = board.pieceLocations(side).size()
                - board.pieceLocations(side.opponent()).size();
        int kingedge = closestkingedge(board);
        int edgemusc = Math.floorDiv(board.getedgemuscovites(), 4);

        return diff - 4 * kingedge - 5 * minkingmov + 5 * edgemusc;
    }

    /** return closest king edge value. BOARD.*/
    private int closestkingedge(Board board) {
        Square kingpos = board.kingPosition();
        int rowblock = 0;
        int colblock = 0;
        for (int i = 0; i < 9; i++) {
            if (board.get(kingpos.col(), i) != EMPTY) {
                rowblock++;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (board.get(i, kingpos.row()) != EMPTY) {
                colblock++;
            }
        }

        if (colblock == 0 || rowblock == 0 && myPiece() == WHITE) {
            return WINNING_VALUE;
        } else if (colblock == 0 || rowblock == 0 && myPiece() == BLACK) {
            return WILL_WIN_VALUE;
        } else {
            return Math.floorDiv(rowblock + colblock, 4);
        }
    }
}
