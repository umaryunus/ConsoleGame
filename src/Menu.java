/**
 * Created by Umar Yunus on 02/02/2016.
 * Coursework - Algorithms and Data Structures
 * umarYunuscw1
 */
import java.util.*;
public class Menu {
    public static void main(String[] args){
        gameMain game = new gameMain();
        Scanner user_input = new Scanner(System.in);
        boolean exitChoice = false;
        game.mainMenu();
        while (exitChoice == false) {
            System.out.print("Please Enter Your Menu Choice: ");
            try {
                int choice = user_input.nextInt();
                System.out.println();
                if (choice == 0) {
                    exitChoice = true;
                    System.out.println("Exiting Game. GoodBye!");
                    // Exit Game;
                    break;
                } else if (choice == 1) {
                    System.out.println("Starting Game ...");
                    // Start the Cheetah vs Rabbit Game
                    game.playGame();
                } else if (choice == 2) {
                    System.out.println("Loading Game Options ...");
                    // Choose Game Bounds
                    game.getGameSize();
                } else if (choice == 3){
                    System.out.println("Displaying All Paths ...");
                    // Show All Paths Taken in the Game
                    game.displayAllWalks();
                }
            } catch (InputMismatchException e){
                System.out.println("Incorrect Menu Choice. Please Enter Again: ");
                user_input.next();
            }
        }
    }
}
