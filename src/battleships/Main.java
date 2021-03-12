package battleships;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        Board board = new Board();
        // fills board randomly with ships
        board.fillBoard();
        // user input scanner
        Scanner input = new Scanner((System.in));

        // game is on
        while (board.getShipsCount() > 0) {

            board.printBoard();

            System.out.print("Target: ");

            String move = input.nextLine();
            move = move.toUpperCase();

            if (move.length() == 0) {
                System.out.println("No input");
                continue;
            }

            int x = move.charAt(0) - 'A';
            int y = move.charAt(1) - '0';

            try {
                board.shoot(x, y);
            } catch (IllegalMoveException ex) {
                System.out.println("Error: " + ex.getMessage());
            }

        }
        System.out.println("Game is over");
    }
}