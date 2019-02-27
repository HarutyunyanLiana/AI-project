import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Player {
    private char[][] board;
    private boolean completed;
    private int notFoundBombs;
    private Game game;
    public int success = 0;

    /**
     * The constructor of the game
     *
     * @param game
     */
    public Player(Game game) {
        this.board = new char[game.getBoardWidth()][game.getBoardHeight()];
        for (char[] c : board) {
            Arrays.fill(c, '.');
        }
        this.game = game;
        this.completed = false;
        this.notFoundBombs = game.bombs;
    }

    /**
     * Checks the visibility of the cell.
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isNotVisible(int x, int y) {
        return board[x][y] == '.';
    }

    /**
     * Checks whether the cell is marked with flag.
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isFlag(int x, int y) {
        return board[x][y] == '#';
    }

    /**
     * Checks whether the cell is both opened and number.
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isOpenedNumber(int x, int y) {
        return !isNotVisible(x, y) && !isFlag(x, y);
    }

    /**
     * Updates the cell of the board with the given coordinates.
     *
     * @param x
     * @param y
     * @param value
     */
    private void updateCell(int x, int y, char value) {
        board[x][y] = value;
    }

    /**
     * Marks the given cell with flag and does the corresponding updates.
     *
     * @param x
     * @param y
     */
    private void markFlag(int x, int y) {
        if (isNotVisible(x, y)) {
            updateCell(x, y, '#');
            notFoundBombs--;
            updateAfterFlag(x, y);
            print();
        }
    }

    /**
     * Clicks randomly on the closed cells the board.
     */
    private void clickRandom() {
        Random r = new Random();
        int r_row = r.nextInt(board.length);
        int r_col = r.nextInt(board[0].length);

        if (isNotVisible(r_row, r_col)) {
            getResultsOfClick(r_row, r_col);
        }
    }

    /**
     * Determines the result of each click with the given coordinates.
     *
     * @param x
     * @param y
     */
    private void getResultsOfClick(int x, int y) {
        if (!completed) {
            if (game.isBomb(x, y)) {
                for (char[] c : board) {
                    Arrays.fill(c, 'X');
                }
                completed = true;

                System.out.println();
                print();
                System.out.println("I lost the game! You can run me again.");
                return;
            } else if (game.isNumber(x, y)) {
                updateCell(x, y, game.getNumber(x, y));
            } else {
                openZeros(x, y);
                coverTrivialCases();
            }
            coverTrivialCases();
        }
    }

    /**
     * Opens the empty parts of the board that are marked as zeros in our game.
     *
     * @param x
     * @param y
     */
    private void openZeros(int x, int y) {
        if (game.isZero(x, y)) {
            updateCell(x, y, '0');
            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {
                    try {
                        if (isNotVisible(x + i, y + j)) {
                            openZeros(x + i, y + j);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }

            }
        } else {
            updateCell(x, y, game.getNumber(x, y));
            print();
        }
    }

    /**
     * Marks the obvious bomb cells with flags.
     *
     * @param i
     * @param j
     */
    private void deactivateObviousBombs(int i, int j) {
        ArrayList<Coordinate> CoordinateList = new ArrayList<>();
        int bombCount = 0;
        for (int x = -1; x < 2; ++x) {
            for (int y = -1; y < 2; ++y) {
                try {
                    if (isNotVisible(i + x, j + y) || isFlag(i + x, j + y)) {
                        bombCount++;
                        if (!isFlag(i + x, j + y)) {
                            CoordinateList.add(new Coordinate(i + x, j + y));
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }

        if (bombCount == Character.getNumericValue(game.getNumber(i, j))) {
            for (Coordinate c : CoordinateList) {
                markFlag(c.x, c.y);
            }
        }
    }

    /**
     * Covers the most trivial cases of the board.
     */
    private void coverTrivialCases() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (isOpenedNumber(i, j)) {
                    deactivateObviousBombs(i, j);
                }
            }
        }
    }

    /**
     * Updates the board correspondingly after marking a flag.
     *
     * @param bomb_x
     * @param bomb_y
     */
    private void updateAfterFlag(int bomb_x, int bomb_y) {
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                try {
                    doubleClick(bomb_x + i, bomb_y + j);
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
    }

    /**
     * Recursively opens the adjacent cells of a specific cell if possible.
     *
     * @param x
     * @param y
     */
    private void doubleClick(int x, int y) {
        ArrayList<Coordinate> CoordinateList = new ArrayList<>();
        int count = 0;
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                try {
                    if (isFlag(i + x, j + y)) {
                        count++;
                    } else if (isNotVisible(i + x, j + y)) {
                        CoordinateList.add(new Coordinate(i + x, j + y));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        if (count == Character.getNumericValue(game.getNumber(x, y))) {
            for (Coordinate c : CoordinateList) {
                getResultsOfClick(c.x, c.y);
                if (!completed) {
                    doubleClick(c.x, c.y);
                }
            }
        }
    }

    /**
     * Finds the places of possible patterns and checks them.
     */
    private void findPatterns() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (isNotVisible(i, j)) {
                    for (int x = -1; x < 2; x += 2) {
                        try {
                            if (isOpenedNumber(i + x, j)) {
                                checkPattern(i, j, x, true);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }
                    }

                    for (int x = -1; x < 2; x += 2) {
                        try {
                            if (isOpenedNumber(i, j + x) && !isNotVisible(i, j)) {
                                checkPattern(i, j, x, false);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks horizontally the existence of patterns and handles them.
     *
     * @param i
     * @param j
     * @param step
     */
    private void checkPattern(int i, int j, int step, boolean isHorizontal) {
        int count = 0;
        int[] patterns = new int[4];
        for (int x = 0; x < 4; ++x) {
            try {
                if (isOpenedNumber(i + (isHorizontal ? step : x), j + (isHorizontal ? x : step)
                ) && isNotVisible(i + (isHorizontal ? 0 : x), j + (isHorizontal ? x : 0))) {
                    patterns[x] = realValueOfCell(i + (isHorizontal ? step : x), j + (isHorizontal ? x : step));
                    count++;
                } else {
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }
        if (count == 3 && patterns[0] == 1 && patterns[1] == 2 && patterns[2] == 1) {
            markFlag(i, j);
            markFlag(i + (isHorizontal ? 0 : 2), j + (isHorizontal ? 2 : 0));
        } else if (count == 4 && patterns[0] == 1 && patterns[1] == 2 && patterns[2] == 2 && patterns[3] == 1) {
            markFlag(i + (isHorizontal ? 0 : 1), j + (isHorizontal ? 1 : 0));
            markFlag(i + (isHorizontal ? 0 : 2), j + (isHorizontal ? 2 : 0));
        }
    }

    /**
     * Gets the count of remaining bombs around the cell, i.e. its real value.
     *
     * @param x
     * @param y
     * @return
     */
    private int realValueOfCell(int x, int y) {
        int currentValue = Character.getNumericValue(game.getNumber(x, y));
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                try {
                    if (isFlag(x + i, y + j)) {
                        currentValue--;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }

            }
        }
        return currentValue;
    }


    /**
     * Plays the game by doing the first step randomly
     * and then running the main algorithm.
     */
    public void play() {
        clickRandom();
        while (notFoundBombs != 0 && !completed) {
            findPatterns();
            clickRandom();
        }
        if (notFoundBombs == 0) {
            completed = true;
            success = 1;
            game.printSolution(true);
            System.out.println("I won the game!");
        }

    }

    /**
     * Prints the board and the current status.
     */
    private void print() {
        if (notFoundBombs != 0) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println("Remaining bombs to find are " + notFoundBombs);
            System.out.println();
        }

        // In order to print the solution with some delays uncomment the following block of code.
        /*
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }*/
    }
}
