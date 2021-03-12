package battleships;

public abstract class WarShip implements Ship {

    // ship orientation
    enum Orientation {
        HORIZONTAL, VERTICAL
    }

    /// ship orientation on board
    private Orientation orientation;

    // ship hit count
    private int hits;

    // fields occupied by ship
    private Field[] occupied;

    public WarShip(Orientation orientation) {
        this.orientation = orientation;
        this.occupied = new Field[this.getDecksCount()];
    }

    // Sets ship deck on field
    public void setOnField(Field field, int deckNo) {
        field.setShip(this);
        field.setState(State.SHIP);
        this.occupied[deckNo] = field;
    }

    /**
     * Counts hits and checks if ship is sunk
     */
    @Override
    public void hit() {
        this.hits++;
        if (isSunk()) {
            for (int i = 0; i < this.occupied.length; i++) {
                this.occupied[i].setState(State.SUNK);
            }
        }
    }

    /**
     * Checks if whole ship is sunk
     *
     * @return true if ship is sunk
     */
    @Override
    public boolean isSunk() {
        return this.hits == getDecksCount();
    }


    public Orientation getOrientation() {
        return this.orientation;
    }
}


/**
 * Single mast ship
 */
class Submarine extends WarShip {

    public Submarine(Orientation orientation) {
        super(orientation);
    }

    public Submarine() {
        this(Orientation.HORIZONTAL); // constructor with parameter will be called
    }

    @Override
    public int getDecksCount() {
        return 1;
    }
}

/**
 * Two mast ship
 */
class Destroyer extends WarShip {

    public Destroyer(Orientation orientation) {
        super(orientation);
    }

    @Override
    public int getDecksCount() {
        return 2;
    }
}

/**
 * Three mast ship
 */
class Cruiser extends WarShip {

    public Cruiser(Orientation orientation) {
        super(orientation);
    }

    @Override
    public int getDecksCount() {
        return 3;
    }
}

/**
 * Four mast ship
 */
class Battleship extends WarShip {

    public Battleship(Orientation orientation) {
        super(orientation);
    }

    @Override
    public int getDecksCount() {
        return 4;
    }
}