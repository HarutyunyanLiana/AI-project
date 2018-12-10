import java.util.Scanner;

public class Main {
    private static final int MIN_SIZE = 5;
    private static final int MAX_SIZE = 50;
    private static final double MAX_BOMB_COVERAGE = 0.3;

    // swing for visualization ,, oracle
    public static void main(String[] args) {
//        int[] parameters = initializeParameters();
//        Game newGame = new Game(parameters[0], parameters[1], parameters[2]);
//        newGame.printSolution(false);
//
//        Player p = new Player(newGame);
//        p.play();

        int win_count= 0;
        for (int i = 0; i < 100 ; i++) {
            System.out.println("aaaa" + i);
            Game newGame = new Game(16, 16, 40);

            Player p = new Player(newGame);
            p.play();
            win_count += p.success;
            System.out.println(i + "th success is " + p.success);

        }
        System.out.println("Wiiiiiiiiiiiin" + win_count);
    }

    private static int[] initializeParameters() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter the height of the board: ");
        int nrow = reader.nextInt();
        while (nrow < 5 || nrow > MAX_SIZE) {
            System.out.println("Please enter another number (min " + MIN_SIZE + ", max " + MAX_SIZE + "): ");
            nrow = reader.nextInt();
        }
        System.out.println("Please enter the width of the board: ");
        int ncol = reader.nextInt();
        while (ncol < 5 || ncol > MAX_SIZE) {
            System.out.println("Please enter another number (min " + MIN_SIZE + ", max " + MAX_SIZE + "): ");
            ncol = reader.nextInt();
        }

        System.out.println("Please enter the number of bombs: ");
        int bombs = reader.nextInt();
        int bombUpperBound = (int) (nrow * ncol * MAX_BOMB_COVERAGE);
        while (bombs < 1 || bombs > bombUpperBound) {
            System.out.println("Please enter another number (min 1, max " + bombUpperBound + "): ");
            bombs = reader.nextInt();
        }

        return new int[]{nrow, ncol, bombs};
    }
}
