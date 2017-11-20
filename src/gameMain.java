/**
 * Created by Umar on 02/02/2016.
 */

import java.util.*;
public class gameMain {
    Scanner user_input = new Scanner(System.in); // Get user input
    int gameWidth = 14; // Platform Width
    int gameHeight = 8; // Platform Height
    String[][] gameArray = new String[gameHeight][gameWidth]; // Declaring the Platform
    int pCy, pCx,pRy, pRx; // Integers which store the previous values of the Cheetah and Rabbit Coordinates
    int Cy, Cx, Ry, Rx, Ex, Ey; // Store the coordinates for the Cheetah, Rabbit and Escape hole
    LinkedList<String> walk = new LinkedList<>(); // Stores the walk of the Cheetah
    LinkedList<String> Rwalk = new LinkedList<>(); // Stores the walk of the Rabbit
    String convertWalk; // Used to turn the Cheetah walk list into a single line
    String convertRWalk; // Used to turn the Rabbit walk list into a single line
    ArrayList<String> allWalks = new ArrayList<>(); // Used to store all the walks made by the Cheetah
    ArrayList<String> allRWalks = new ArrayList<>(); // Used to Store all the walks made by the Rabbit

    public void getGameSize(){
        // If the user wants to change the size of the platform we use this function which asks the user the size of the gameWidth and gameHeight
        boolean gdChoice1, gdChoice2;
        gdChoice1 = false;
        while (gdChoice1 == false) {
        System.out.println();
        System.out.print("Please Enter Width of Game Board (Width > 8 & Width < 20): ");
            try {
                int w = user_input.nextInt();
                if (w >= 8 && w <= 20) {
                    gdChoice1 = true;
                    gameWidth = w;
                    Cx = w - 1; // Getting Y Coordinate of Rabbit if game size is changed
                }
            } catch (InputMismatchException e){
                System.out.println("Illegal Character Entered. Please Enter Width Again: ");
                user_input.next();
            }
        }
        gdChoice2 = false;
        while (gdChoice2 == false) {
            System.out.print("Please Enter Height of Game Board (Height > 8 & Height < 15): ");
            try {
                int h = user_input.nextInt();
                if (h >= 8 & h <= 15) {
                    gdChoice2 = true;
                    gameHeight = h;
                    Cy = h - 1; // Getting X coordinate of rabbit if game size is changed
                }
            } catch (InputMismatchException e){
                System.out.println("Illegal Character Entered. Please Enter Height Again: ");
                user_input.next();
            }
        }
        System.out.println();
        mainMenu();
    }

    public void makeGame(){
        // Making the game Platform
        for (int i = 0; i < gameHeight; i++){
            for (int j = 0; j < gameWidth; j++){
                gameArray[i][j] = "| - |";
            }
        }

        gameArray[Ry][Rx] = "| R |"; // Assign initial Rabbit place
        gameArray[Ey][Ex] = "| + |"; // Assign initial Escape place
        gameArray[Cy][Cx] = "| C |"; // Assign initial Cheetah place

        assignPreviousState(); // This gets the previous coordinates of the Cheetah and Rabbit

        displayGameScreen(); // This outputs the Platform onto the Screen

        System.out.println();
    }

    public void displayGameScreen(){
        // Printing the game screen out onto the console
        for (int i = 0; i < gameHeight; i++){
            System.out.println();
            for (int j = 0; j < gameWidth; j++){
                System.out.print(gameArray[i][j]);
            }
        }

        System.out.println();
    }

    public void playGame(){
        // This is the starting function for the game
        gameArray = new String[gameHeight][gameWidth]; // This creates the Platform size if modified or not
        Ry = gameHeight-1; // X-Coordinate of Rabbit
        Rx = gameWidth-1; // Y-Coordinate of Rabbit
        Cy = 0; // X-Coordinate of Cheetah
        Cx = 0; // Y-Coordinate of Cheetah
        convertRWalk = ""; // emptying the string which gets added to Rabbit walk list
        convertWalk = ""; // emptying the string which gets added to Cheetah walk list
        Rwalk.clear(); // emptying the list which contains the rabbit walk
        walk.clear(); // emptying the list which contains the cheetah walk
        getEscapeBlock(); // This gets coordinates for the escape hole which will be placed onto the platform in the makeGame() function
        makeGame(); // This calls the makeGame() function which calls the displayGameScreen() function
        getPlayerMove(); // This calls the getPlayerMove() function
    }

    public void getPlayerMove(){ // SHOW USER OPTIONS TO MOVE
        // This function is where the user inputs in the moves where they want the Cheetah to go
        // This function has a loop until the boolean endGame is equal to true
        Scanner scan = new Scanner(System.in); // Get user input for the move
        String input = "";
        boolean endGame = false;
        while (endGame == false) { // Constantly loop asking the user to input their choice
            System.out.println("Enter M to display Moves.");
            System.out.println("Enter 0 to Exit.");
            System.out.print("Please Enter Move. Recommended Move (" + getRecommendedMove() + ") : "); // Print out the best move for the user to choose
            try {
                try {
                    input = scan.next();
                    if (input.equals("0")){ // If the input is 0, quit and go back to main menu
                        System.out.println();
                        mainMenu();
                        break; // End the current while loop
                    } else {
                        if (input.equals("r") || // Conditions which the user must fulfil, i.e. being the only choices available for the user to choose from
                                input.equals("R") ||
                                input.equals("l") ||
                                input.equals("L") ||
                                input.equals("u") ||
                                input.equals("U") ||
                                input.equals("d") ||
                                input.equals("D") ||
                                input.equals("ul") ||
                                input.equals("UL") ||
                                input.equals("ur") ||
                                input.equals("UR") ||
                                input.equals("dl") ||
                                input.equals("DL") ||
                                input.equals("dr") ||
                                input.equals("DR") ||
                                input.equals("rdm") ||
                                input.equals("RDM")) {
                            cheetahMoves(input); // If the Choices are acceptable i.e. being one of the ones shown above, call the cheetahMoves() function
                            if (foundRabbit() == true) {
                                // Checks to see if the Cheetah has caught the Rabbit, if it has it will end the game and go back to the main menu
                                eatenRabbit(); // Calls the eatenRabbit() function if the Rabbit has been caught
                                endGame = true;
                                break;
                            }
                            moveRabbit(); // calls the moveRabbit() function
                            if (rabbitEscaped() == true) {
                                // Checks to see if the Rabbit has escaped, if it has it will end the game and go back to the main menu
                                escapedRabbit(); // Calls the escapedRabbit() function if the Rabbit has escaped
                                endGame = true;
                                break;
                            }
                            makeGame(); // Makes the game platform with the modified coordinates and prints them out onto the console
                        } else if (input.equals("m") || input.equals("M")){
                            makeGame();
                            showMoves();
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    // Checks to see if the user is trying to get the Cheetah to move outside of the platform
                    resetMainGame(); // Reassigns the previous coordinates so that the rabbit doesn't have a move advantage as the user was trying to escape
                    makeGame(); // Makes the game platform with the previous coordinates and prints it out onto the console
                    System.out.println("Stop Running Away, Catch The Rabbit.");
                    System.out.println();
                    walk.removeLast(); // Removes the user option from which they were trying to escape the platform from
                }
            } catch (InputMismatchException e){
                // Checks to see if the user has inputted the incorrect move option, if it has
                resetMainGame(); // it will take the game back to the previous state
                makeGame(); // Makes the game platform with the previous state and displays the platform onto the console
                System.out.println("Incorrect Move. Please Enter Move Again");
                scan.next(); // Asks the user to re-enter in their choice
            }
        }
    }

    public boolean foundRabbit(){
        // Checks to see if the Rabbit has been caught by the Cheetah
        if ((Cx == Rx) && (Cy == Ry)){
            return true;
        } else if ((Rx == Cx) && (Ry == Cy)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean rabbitEscaped(){
        // Checks to see if the Rabbit has escaped
        if ((Rx == Ex) && (Ry == Ey)){
            return true;
        } else {
            return false;
        }
    }

    public void eatenRabbit(){
        // If the rabbit has been eaten, this function will tell the user the number of steps taken to catch the rabbit and what steps it had taken
        System.out.println();
        System.out.println("You have caught the Rabbit in " + walk.size() + " Steps.");
        System.out.println("The Steps taken to get to the Rabbit is: " + walk);
        getAllWalks();
        System.out.println();
        mainMenu();
    }

    public void escapedRabbit(){
        // If the rabbit has escaped, this function will tell the user the number of steps taken for the rabbit to escape and what steps it had taken
        System.out.println();
        System.out.println("The Rabbit has Escaped in " + Rwalk.size() + " Steps.");
        System.out.println("The Steps taken for the Rabbit to Escape is: " + Rwalk);
        getAllRWalks();
        System.out.println();
        mainMenu();
    }

    public void showMoves(){
        // If the user enters M as their input, this function will load and show the user what moves are possible by the Cheetah
        System.out.println("-------- GAME MOVES --------");
        System.out.println("U - Move Cheetah UP");
        System.out.println("D - Move Cheetah DOWN");
        System.out.println("L - Move Cheetah LEFT");
        System.out.println("R - Move Cheetah RIGHT");
        System.out.println("UL - Move Cheetah UP & LEFT");
        System.out.println("UR - Move Cheetah UP & RIGHT");
        System.out.println("DL - Move Cheetah DOWN & LEFT");
        System.out.println("DR - Move Cheetah DOWN & RIGHT");
        System.out.println("RDM - Make Cheetah Move in a RANDOM Direction");
        System.out.println("----------------------------");
        System.out.println();
    }

    public void assignPreviousState(){ // If index out of bounds go back to this state

        pCy = Cy;
        pCx = Cx;
        pRy = Ry;
        pRx = Rx;
    }

    public void resetMainGame(){
        // This changes the game to its previous state if game changing errors occur
        Cy = pCy;
        Cx = pCx;
        Ry = pRy;
        Rx = pRx;
    }

    public void cheetahMoves(String s){
        // This gets the user input and checks to see if it equal to any of the inputs below and responds with the corresponding action
        if (s.equals("r") || s.equals("R")){
            Cx++;
            walk.add("R");
            // Move right
        } else if (s.equals("d") || s.equals("D")){
            Cy++;
            walk.add("D");
            // Move down
        } else if (s.equals("l") || s.equals("L")) {
            Cx--;
            walk.add("L");
            // Move Left
        } else if (s.equals("u") || s.equals("U")) {
            Cy--;
            walk.add("U");
            // Move up
        } else if (s.equals("ur") || s.equals("UR")) {
            Cy--;
            Cx++;
            walk.add("UR");
            // Move up and right
        } else if (s.equals("ul") || s.equals("UL")) {
            Cy--;
            Cx--;
            walk.add("UL");
            // Move up and left
        } else if (s.equals("dl") || s.equals("DL")) {
            Cy++;
            Cx--;
            walk.add("DL");
            // Move down and Left
        } else if (s.equals("dr") || s.equals("DR")) {
            Cy++;
            Cx++;
            walk.add("DR");
            // Move down and right
        } else if (s.equals("rdm") || s.equals("RDM")) {
            getRandomMove();
            // Get a random move
        } else if (s.equals("m") || s.equals("M")) {
            showMoves();
            // Show the input which will move the Cheetah in a certain direction
        }
    }

    public void getRandomMove(){
        // If the user has chosen for the Cheetah to take a random move, the cheetah will move towards the rabbit after it compares its position with itself
        if (Cx > Rx && Cy > Ry){
            Cx--;
            Cy--; // Move UP & LEFT
            walk.add("UL");
        } else if (Cx < Rx && Cy > Ry){
            Cx++;
            Cy--; // Move UP & RIGHT
            walk.add("UR");
        } else if (Cx == Rx && Cy > Ry){
            Cx--; // Move UP
            walk.add("U");
        } else if (Cx > Rx && Cy == Ry){
            Cy--; // Move LEFT
            walk.add("L");
        } else if (Cx < Rx && Cy == Ry){
            Cx++; // Move RIGHT
            walk.add("R");
        } else if (Cx == Rx && Cy < Ry){
            Cy++; // Move DOWN
            walk.add("D");
        } else if (Cx > Rx && Cy < Ry){
            Cx--;
            Cy++; // Move DOWN & LEFT
            walk.add("DL");
        } else if (Cx < Ry && Cy < Ry){
            Cx++;
            Cy++; // Move DOWN & RIGHT
            walk.add("DR");
        }
    }

    public void mainMenu(){
        // this is the main menu which shows all the choices the user can select
        System.out.println("--------------------------");
        System.out.println("           MENU           ");
        System.out.println("--------------------------");
        System.out.println("1. Play Game");
        System.out.println("2. Game Options");
        System.out.println("3. Show All Paths");
        System.out.println();
        System.out.println("0. Exit Game");
        System.out.println("--------------------------");
        System.out.println();
    }

    public void getEscapeBlock() {
        // This generates the coordinates for the escape hole needed by the rabbit
        int x = Math.round(gameWidth/2) - 1;
        int y = Math.round(gameHeight/2) - 1;
        Ex = x;
        Ey = y;
    }

    public void moveRabbit(){ // Moving the Rabbit towards the escape hole
        // This function automatically moves the rabbit to the escape hole once the Cheetah has moved
        if (Rx > Ex && Ry > Ey){
            Rx--;
            Ry--; // Move UP & LEFT
            Rwalk.add("UL");
        } else if (Rx < Ex && Ry > Ey){
            Rx++;
            Ry--; // Move UP & RIGHT
            Rwalk.add("UR");
        } else if (Rx == Ex && Ry > Ey){
            Ry--; // Move UP
            Rwalk.add("U");
        } else if (Rx > Ex && Ry == Ey){
            Rx--; // Move LEFT
            Rwalk.add("L");
        } else if (Rx < Ex && Ry == Ey){
            Rx++; // Move RIGHT
            Rwalk.add("R");
        } else if (Rx == Ex && Ry < Ey){
            Ry++; // Move DOWN
            Rwalk.add("D");
        } else if (Rx > Ex && Ry < Ey){
            Rx--;
            Ry++; // Move DOWN & LEFT
            Rwalk.add("DL");
        } else if (Rx < Ey && Ry < Ey){
            Rx++;
            Ry++; // Move DOWN & RIGHT
            Rwalk.add("DR");
        }
    }

    public String getRecommendedMove(){
        // This shows the user what move they should input if they want to catch the Rabbit in the shortest path
        if (Cx > Rx && Cy > Ry){
            return "| UL | -> UP & LEFT";
        } else if (Cx < Rx && Cy > Ry){
            return "| UR | -> UP & RIGHT";
        } else if (Cx == Rx && Cy > Ry){
            return "| U | -> UP";
        } else if (Cx > Rx && Cy == Ry){
            return "| L | -> LEFT";
        } else if (Cx < Rx && Cy == Ry){
            return "| R | -> RIGHT";
        } else if (Cx == Rx && Cy < Ry){
            return "| D | -> DOWN";
        } else if (Cx > Rx && Cy < Ry){
            return "| DL | -> DOWN & LEFT";
        } else if (Cx < Ry && Cy < Ry){
            return "| DR | -> DOWN & RIGHT";
        }
        return null;
    }

    public void getAllWalks(){
        // This function gets the walk taken by the Cheetah and stores it so that the user can see them later
        convertWalk = "";
        for (int i = 0; i < walk.size(); i++){
            convertWalk += walk.get(i) + ", ";
        }
        allWalks.add(convertWalk);
        walk.clear();
    }

    public void getAllRWalks(){
        // This function gets the walk taken by the Rabbit and stores it so that the user can see them later
        convertRWalk = "";
        for (int i = 0; i < Rwalk.size(); i++){
            convertRWalk += Rwalk.get(i) + ", ";
        }
        allRWalks.add(convertRWalk);
        Rwalk.clear();
    }

    public void displayAllWalks(){
        // This prints out all the paths taken by the Rabbit and the Cheetah
        System.out.println();
        System.out.println("All Cheetah Catch Paths ...");
        System.out.println();
        for (int i = 0; i < allWalks.size(); i++){
            System.out.println((i + 1) + ". " + allWalks.get(i));
        }
        System.out.println();
        System.out.println("All Rabbit Escape Paths ...");
        System.out.println();
        for (int j = 0; j < allRWalks.size(); j++){
            System.out.println((j + 1) + ". " + allRWalks.get(j));
        }
        System.out.println();
        mainMenu();
    }
}




