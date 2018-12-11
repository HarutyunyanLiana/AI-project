import java.util.Random;

public class Game {
    private int[][] board;
    public int bombs;

    /**
     * The constructor of the class
     *
     * @param nrow
     * @param ncol
     * @param bombs
     */
    public Game(int nrow, int ncol, int bombs) {
        this.board = new int[nrow][ncol];
        this.bombs = bombs;
        initializeBoard(nrow, ncol, bombs);
    }

    /**
     * Initialized the game board with the given sizes and number of bombs
     *
     * @param nrow
     * @param ncol
     * @param bombs
     */
    private void initializeBoard(int nrow, int ncol, int bombs) {
        Random r = new Random();
        while (bombs > 0) {
            int r_row = r.nextInt(nrow);
            int r_col = r.nextInt(ncol);
            if (board[r_row][r_col] >= 0) {
                board[r_row][r_col] = -9;
                for (int x = -1; x < 2; ++x) {
                    for (int y = -1; y < 2; ++y) {
                        // surrounding squares
                        try {
                            board[r_row + x][r_col + y]++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                }
                bombs--;
            }
        }
    }


    /**
     * Checks whether the cell with the given coordinates is a bomb.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isBomb(int x, int y) {
        return board[x][y] < 0;
    }

    /**
     * Checks whether the cell value with the given coordinates is zero.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isZero(int x, int y) {
        return board[x][y] == 0;
    }

    /**
     * Checks whether the cell with the given coordinates is a number.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isNumber(int x, int y) {
        return board[x][y] > 0;
    }

    /**
     * Gets the numeric value of the cell.
     *
     * @param x
     * @param y
     * @return
     */
    public char getNumber(int x, int y) {
        return (char) (board[x][y] + (int) '0');
    }

    /**
     * Prints the solution of the game.
     *
     * @param win
     */
    public void printSolution(boolean win) {
        char danger = win ? '#' : 'X';
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] < 0) {
                    System.out.print(danger + " ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Returns the width of the board.
     *
     * @return
     */
    public int getBoardWidth() {
        return board.length;
    }

    /**
     * Returns the height of the board.
     *
     * @return
     */
    public int getBoardHeight() {
        return board[0].length;
    }
}
