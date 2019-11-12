package tablut;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** The suite of all JUnit tests for the enigma package.
 *  @author
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test as a placeholder for real ones. */
    @Test
    public void inittest() {
        Board board = new Board();
        board.init();
        assert (board.get(4, 4) == Piece.KING);
        assert (board.turn() == Piece.BLACK);
        assert (board.pieceLocations(Piece.BLACK).size() == 16);
    }

    @Test
    public void movetest() {
        Board board = new Board();
        board.init();
        board.makeMove(Move.mv(Square.sq(3, 0 ), Square.sq(0, 0)));
        assert (board.pieceLocations(Piece.BLACK).contains(Square.sq(0, 0)));
        assertFalse(board.pieceLocations(Piece.BLACK).contains(Square.sq(3, 0)));
        System.out.println(board.encodedBoard());
    }

    @Test
    public void illegalmove() {
        Board board = new Board();
        board.init();
        board.makeMove(Move.mv(Square.sq(3, 0 ), Square.sq(2, 1)));
        assert (board.pieceLocations(Piece.BLACK).contains(Square.sq(3, 0)));
        assertFalse(board.pieceLocations(Piece.BLACK).contains(Square.sq(3, 1)));
    }

    @Test
    public void capturetest() {
        Board board = new Board();
        board.init();
        board.makeMove(Move.mv(Square.sq(3, 0 ), Square.sq(3, 3)));
        board.makeMove(Move.mv(Square.sq(2, 4), Square.sq(2, 8)));
        board.makeMove(Move.mv(Square.sq(5, 0 ), Square.sq(5, 3)));
        assertFalse(board.pieceLocations(Piece.WHITE).contains(Square.sq(4, 3)));
        System.out.println(board);
    }

    @Test
    public void capturetestthrone() {
        Board board = new Board();
        board.init();
        board.makeMove(Move.mv(Square.sq(3, 0 ), Square.sq(3, 3)));
        board.makeMove(Move.mv(Square.sq(2, 4), Square.sq(2, 8)));
        board.makeMove(Move.mv(Square.sq(5, 0 ), Square.sq(5, 3)));
        board.makeMove(Move.mv(Square.sq(3, 4 ), Square.sq(3, 7)));
        board.makeMove(Move.mv(Square.sq(4, 0 ), Square.sq(8, 0)));
        board.makeMove(Move.mv(Square.sq(5, 4 ), Square.sq(5, 7)));
        assertFalse(board.pieceLocations(Piece.BLACK).contains(Square.sq(4, 7)));
        board.makeMove(Move.mv("d4-5"));
        board.makeMove(Move.mv("c9-a"));
        board.makeMove(Move.mv("f4-5"));
        assert board.kingPosition() == Board.THRONE;
    }

    @Test
    public void repeatedtest(){
        Board board = new Board();
        board.init();
        board.makeMove(Move.mv(Square.sq(3, 0 ), Square.sq(2, 1)));
        assert (board.pieceLocations(Piece.BLACK).contains(Square.sq(3, 0)));
        assertFalse(board.pieceLocations(Piece.BLACK).contains(Square.sq(3, 1)));
        assertNotSame(board.winner(), Piece.WHITE);
    }

    @Test
    public void repeatTest() {
        Board board = new Board();
        board.makeMove(Move.mv("i6-g"));
        board.makeMove(Move.mv("d5-7"));
        board.makeMove(Move.mv("g6-i"));
        board.makeMove(Move.mv("d7-5"));
        assertEquals(board.winner(), Piece.BLACK);
    }
}



