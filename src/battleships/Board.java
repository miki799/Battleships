package battleships;

import java.util.Random;

public class Board {

    // Const integers representing battlefield size and ship types amount
    public static final int BOARD_SIZE = 10;
    public static final int SHIP_TYPES_COUNT = 4;


    // 2D array of field objects
    private Field[][] fields = new Field[BOARD_SIZE][BOARD_SIZE];

    private int shipsCount;
    private int[] numberOfShipsByDeck = new int[SHIP_TYPES_COUNT];

    /**
     * Constructor fills board with fields in EMPTY state
     */
    public Board() {
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                fields[y][x] = new Field(x, y, State.EMPTY);
            }
        }
    }

    /**
     * Fills board randomly with ships
     */
    public void fillBoard() {

        Random random = new Random();

        // Starts from adding Submarines (decks = 1)
        for (int decks = 1; decks <= SHIP_TYPES_COUNT; decks++) {
            for (int i = 0; i < getTotalCountOfShips(decks); i++) {

                // set to false if space for ship deck is found
                boolean tryAgain;

                do {

                    // random coordinates for ship decks (0-9)
                    int x = random.nextInt(BOARD_SIZE);
                    int y = random.nextInt(BOARD_SIZE);

                    // random ship orientation
                    WarShip.Orientation orientation
                            = random.nextBoolean() ?
                            WarShip.Orientation.HORIZONTAL :    // true - horizontal
                            WarShip.Orientation.VERTICAL;       // false - vertical

                    // generates Ship
                    Ship ship = getShip(decks, orientation);

                    // adds ship to board
                    try {
                        addShip(x, y, ship);
                        tryAgain = false; // ship added
                    } catch (IllegalMoveException ex) {
                        tryAgain = true; // ship not added
                    }

                } while (tryAgain);
            }
        }
    }

    /**
     * Method generating ships, used in fillBoard()
     *
     * @param decks       ship decks amount which specifies type of ship
     * @param orientation orientation of ship - horizontal or vertical
     * @return returns Ship object
     */
    private Ship getShip(int decks, WarShip.Orientation orientation) {
        switch (decks) {
            case 1:
                return new Submarine(orientation);
            case 2:
                return new Destroyer(orientation);
            case 3:
                return new Cruiser(orientation);
            case 4:
                return new Battleship(orientation);
            default:
                return null;
        }
    }

    /**
     * Prints battle board on screen
     */
    public void printBoard() {
        printLetters();
        for (int y = 0; y < 10; y++) {
            // Prints row numbers
            int numberToPrint = y;
            System.out.print(numberToPrint);
            // Fills board with State chars
            for (int x = 0; x < 10; x++) {
                char shipValue = fields[y][x].stateToChar();
                System.out.print(shipValue);
            }
            System.out.print('\n');
        }
    }

    /**
     * Prints column letters
     */
    private void printLetters() {
        // rows
        System.out.print(" ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((char) ('A' + i));
        }
        System.out.print('\n');
    }

    /**
     * Adds ships to the board
     *
     * @param x    X axis coordinate
     * @param y    Y axis coordinate
     * @param ship Ship object
     * @throws IllegalMoveException if ship limit is met
     */
    public void addShip(int x, int y, Ship ship) throws IllegalMoveException {

        int count = ship.getDecksCount();

        // Checks if ship limit is met
        if (numberOfShipsByDeck[count - 1]
                == getTotalCountOfShips(count)) {
            throw new IllegalMoveException("You have all ships set!");
        }

        // creates field for ship
        Field[] field = new Field[count];

        int xToSet = x, yToSet = y;
        for (int i = 0; i < count; i++) {
            if (ship.getOrientation() == WarShip.Orientation.HORIZONTAL) {
                xToSet = x + i;
            } else {
                yToSet = y + i;
            }

            // Checks if deck won't be set outside board
            if (isOutside(xToSet, yToSet)) {
                throw new IllegalMoveException("Ship set outside board!");
            }

            field[i] = fields[yToSet][xToSet];

            // checks if deck won't be too close to other ship
            if (isFieldOccupied(field[i])) {
                throw new IllegalMoveException("Field is occupied!");
            }
        }

        // Sets ship deck on board fields if everything is OK
        for (int i = 0; i < count; i++) {
            ship.setOnField(field[i], i);
        }

        this.shipsCount++;
        numberOfShipsByDeck[count - 1]++; // -1 because it's an array
    }

    /**
     * Used for checking if ship deck won't be set outside of battle board
     *
     * @param x deck X axis coordinate
     * @param y deck Y axis coordinate
     * @return returns true if ship deck will be outside board
     */
    private boolean isOutside(int x, int y) {
        return x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE;
    }

    /**
     * Checks if coordinate is not close to other ship decks or already occupied
     *
     * @param field Field object, which specifies coordinate at which we want to place ship deck
     * @return true if field is occupied
     */
    private boolean isFieldOccupied(Field field) {
        for (int y = field.getY() - 1; y <= field.getY() + 1; y++) {
            for (int x = field.getX() - 1; x <= field.getX() + 1; x++) {

                if (isOutside(x, y)) {
                    continue; // checked field is outside board so there is no need to check further
                }

                if (fields[y][x].getState() != State.EMPTY) {
                    return true; // field is occupied
                }
            }
        }
        return false; // field is not occupied
    }


    /**
     * How many ships of one type can be on board?
     *
     * @param decksCount decks amount representing ship type
     * @return possible amount of specific ship types on board
     */
    private int getTotalCountOfShips(int decksCount) {
        return SHIP_TYPES_COUNT - decksCount + 1;
    }

    /**
     * Returns current count of ships on board
     *
     * @return amount of ships on board
     */
    public int getShipsCount() {
        return this.shipsCount;
    }

    /**
     * @param x X axis coordinate
     * @param y Y axis coordinate
     * @return returns Field coordinates
     */
    public Field getField(int x, int y) {
        return fields[y][x];
    }

    /**
     * Shooting method
     *
     * @param x X axis coordinate
     * @param y Y axis coordinate
     * @throws IllegalMoveException if input is wrong or we are shooting same place
     */
    public void shoot(int x, int y) throws IllegalMoveException {

        //If shot is outside board or input is wrong
        if (isOutside(x, y)) {
            throw new IllegalMoveException("Wrong input!");
        }

        Field field = getField(x, y);

        // Checks if field hasn't been shot before, at the beginning field is in EMPTY state
        if (field.getState() == State.MISS ||
                field.getState() == State.HIT ||
                field.getState() == State.SUNK) {
            throw new IllegalMoveException("You can't shot twice at the same place");
        }

        // Checking
        if (field.getState() == State.EMPTY) {
            System.out.println("MISS");
            field.setState(State.MISS);
        } else if (field.getState() == State.SHIP) {
            System.out.println("HIT");
            field.setState(State.HIT);
            field.getShip().hit(); // counts ship hits and checks if ship is sunk
            if (field.getShip().isSunk()) {
                shipsCount--;
            }
        }

    }
}

