package battleships;

public class IllegalMoveException extends Exception {
    public IllegalMoveException(String message) {
        super(message); // passing message to Exception class
    }
}
