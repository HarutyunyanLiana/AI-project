import java.util.Arrays;
import java.util.Random;

public class Game {
    private int[][] board;
    int bombs;

    public Game(int nrow, int ncol, int bombs) {
        this.board = new int[nrow][ncol];
        this.bombs = bombs;

        Random r = new Random();
        while (bombs > 0) {
            int rPos = r.nextInt(nrow);
            int cPos = r.nextInt(ncol);
            if (board[rPos][cPos] >= 0) {
                board[rPos][cPos] = -9;
                for (int x = -1; x < 2; ++x) {
                    for (int y = -1; y < 2; ++y) {
                        // surrounding squares
                        try {
                            board[rPos + x][cPos + y]++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                }
                bombs--;
            }
        }
    }

    public boolean isBomb(Coordinates coords) {
        return board[coords.getX()][coords.getY()] < 0;
    }

    public boolean isZero(Coordinates coords) {
        return board[coords.getX()][coords.getY()] == 0;
    }

    public boolean isNumber(Coordinates coords) {
        return board[coords.getX()][coords.getY()] > 0;
    }

    public char getNumber(Coordinates coords) {
        return (char) (board[coords.getX()][coords.getY()] + (int) '0');
    }


    public void printSolution() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] < 0) {
                    System.out.print("X ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public int getBoardWidth() {
        return board.length;
    }

    public int getBoardHeight() {
        return board[0].length;
    }


}
