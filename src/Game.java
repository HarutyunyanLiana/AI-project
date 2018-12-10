import java.util.Random;

public class Game {
    private int[][] board;
    public int bombs;

    public Game(int nrow, int ncol, int bombs) {
        this.board = new int[nrow][ncol];
        this.bombs = bombs;
        initialize_board(nrow, ncol, bombs);
    }

    private void initialize_board(int nrow, int ncol, int bombs) {
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

    public boolean isBomb(Coordinate coords) {
        return board[coords.x][coords.y] < 0;
    }

    public boolean isZero(Coordinate coords) {
        return board[coords.x][coords.y] == 0;
    }

    public boolean isNumber(Coordinate coords) {
        return board[coords.x][coords.y] > 0;
    }

    public char getNumber(Coordinate coords) {
        return (char) (board[coords.x][coords.y] + (int) '0');
    }


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

    public int getBoardWidth() {
        return board.length;
    }

    public int getBoardHeight() {
        return board[0].length;
    }


}
