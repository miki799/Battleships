package battleships;

public class Field {

    // coordinates
    private final int x;
    private final int y;
    // field state
    private State state;
    // ship
    private Ship ship;

    public Field(int x, int y, State state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    /**
     * Changes field state to sign, which will be printed on board
     *
     * @return char representing board field with specific state
     */
    public char stateToChar() {
        char value = switch (this.state) {
            case SHIP, EMPTY -> ' ';
            case HIT -> 'O';
            case SUNK -> 'X';
            case MISS -> '!';
            default -> '?';
        };
        return value;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip() {
        return ship;
    }


    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

}
