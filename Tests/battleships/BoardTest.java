package battleships;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    /*
     * Before tests
     */
    private Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board();
    }

    /*
     * Checks if Submarine was added
     */
    @Test
    public void shouldAddSubmarine() throws Exception {
        // act
        board.addShip(0, 0, new Submarine());
        // assert
        assertEquals(1, board.getShipsCount());
    }

    /*
     * Checks if Submarine is added to field
     */
    @Test
    public void shouldAddSubmarineOnField() throws Exception {
        // act
        board.addShip(0, 0, new Submarine());
        // assert
        Field field = board.getField(0, 0);
        assertEquals(State.SHIP, field.getState());
    }

    /*
     * Checks if Destroyer is added to field
     */
    @Test
    public void shouldAddDestroyerOnField() throws Exception {
        // act
        board.addShip(0, 0, new Destroyer(WarShip.Orientation.HORIZONTAL));
        // assert
        Field field = board.getField(0, 0);
        assertEquals(State.SHIP, field.getState());
    }


    /*
     * There only can be 4 Submarines on board
     */
    @Test(expected = IllegalMoveException.class)    // assert
    public void shouldNotBeAbleToAddFiveSubmarines() throws Exception {
        board.addShip(0, 0, new Submarine());
        board.addShip(1, 1, new Submarine());
        board.addShip(2, 2, new Submarine());
        board.addShip(3, 3, new Submarine());
        // error
        board.addShip(4, 4, new Submarine());
    }

    /*
     * There only can be 1 Battleship on board
     */
    @Test(expected = IllegalMoveException.class)    // assert
    public void shouldNotBeAbleToAddTwoBattleships() throws Exception {
        board.addShip(0, 0, new Battleship(WarShip.Orientation.HORIZONTAL));
        // error
        board.addShip(6, 6, new Battleship(WarShip.Orientation.HORIZONTAL));
    }

    /*
     * Tests ship coordinates. If test passed, ship is outside the board
     */
    @Test(expected = IllegalMoveException.class)    // assert
    public void shouldFailToAddOutsideX() throws Exception {
        // act
        board.addShip(-1, 0, new Submarine());
    }

    @Test(expected = IllegalMoveException.class)    // assert
    public void shouldFailToAddOutsideY() throws Exception {
        // act
        board.addShip(0, 11, new Submarine());
    }

    @Test(expected = IllegalMoveException.class)
    public void shouldNotBeAbleToGetOutside() throws Exception {
        //arrange
        //act
        board.addShip(9, 0, new Destroyer(WarShip.Orientation.HORIZONTAL));
    }

    @Test(expected = IllegalMoveException.class)
    public void shouldNotBeAbleToBeClose() throws Exception {
        // arrange
        board.addShip(0, 0, new Destroyer(WarShip.Orientation.HORIZONTAL));
        // act
        board.addShip(2, 0, new Destroyer(WarShip.Orientation.HORIZONTAL));
    }

    @Test
    public void shouldMarkMiss() throws Exception {
        // arrange
        // act
        board.shoot(0, 0);
        // assert
        assertEquals(State.MISS, board.getField(0, 0).getState());

    }

    @Test
    public void shouldMarkHit() throws Exception {
        // arrange
        board.addShip(0, 0, new Destroyer(WarShip.Orientation.HORIZONTAL));
        // act
        board.shoot(0, 0);
        // assert
        assertEquals(State.HIT, board.getField(0, 0).getState());

    }

    @Test
    public void shouldMarkAsSunk() throws Exception {
        // arrange
        board.addShip(0, 0, new Destroyer(WarShip.Orientation.HORIZONTAL));
        board.shoot(0, 0);
        // act
        board.shoot(1, 0);
        // assert
        assertEquals(State.SUNK, board.getField(0, 0).getState());
        assertEquals(State.SUNK, board.getField(1, 0).getState());
    }

    @Test
    public void shouldDecreaseShipCount() throws Exception {
        // arrange
        board.addShip(0, 0, new Submarine());
        // act
        board.shoot(0, 0);
        // assert
        assertEquals(0, board.getShipsCount());
    }

    @Test(expected = IllegalMoveException.class) // assert
    public void shouldNotBeAbleToShootTwice() throws Exception {
        // arrange
        board.shoot(0, 0);
        // act
        board.shoot(0, 0);
    }

    @Test
    public void shouldHaveAllShipsGenerated() throws Exception {
        // arrange
        // act
        board.fillBoard();
        // assert
        assertEquals(10, board.getShipsCount());
    }
}