import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Player {
    private char[][] board;
    private boolean completed;
    private int notFoundBombs;
    private Game game;

    public Player(Game game) {
        this.board = new char[game.getBoardWidth()][game.getBoardHeight()];
        for (char[] c : board) {
            Arrays.fill(c, '.');
        }
        this.game = game;
        this.completed = false;
        this.notFoundBombs = game.bombs;
    }


    private boolean isNotVisible(int x, int y) {
        return board[x][y] == '.';
    }

    private boolean isFlag(int x, int y) {
        return board[x][y] == '#';
    }

    private void markFlag(Coordinate coords) {
        if (board[coords.x][coords.y] != '#') {
            board[coords.x][coords.y] = '#';
            notFoundBombs--;
            updateAfterFlag(coords);
        }
    }

    private void getResultsOfClick(Coordinate coords) {
        if (!completed) {
            if (game.isBomb(coords)) {
                for (char[] c : board) {
                    Arrays.fill(c, 'X');
                }
                completed = true;
                print();
                System.out.println("You lost the game! Try Again");
                return;
            } else if (game.isNumber(coords)) {
                updateBoard(coords, game.getNumber(coords));
                print();
            } else {
                //start = true;
                openZeros(coords);
                print();
                coverTrivialCases();
            }
            coverTrivialCases();

        }
    }

    public void play() {
        while (!completed) {
            Random r = new Random();
            int r_row = r.nextInt(board.length);
            int r_col = r.nextInt(board[0].length);

            if (isNotVisible(r_row, r_col)) {
                Coordinate coords = new Coordinate(r_row, r_col);
                coords.print();
                getResultsOfClick(coords);
            }
        }
    }

    public void updateBoard(Coordinate coords, char to) {
        board[coords.x][coords.y] = to;
    }



    public void openZeros(Coordinate coords) {
        if (game.isZero(coords)) {
            updateBoard(coords, '0');
            for (int x = -1; x < 2; ++x) {
                for (int y = -1; y < 2; ++y) {
                    try {
                        if (isNotVisible(coords.x + x, coords.y + y)) {
                            openZeros(new Coordinate(coords.x + x, coords.y + y));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }

            }
        } else {
            updateBoard(coords, game.getNumber(coords));
        }
    }

    public void coverTrivialCases() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (game.isNumber(new Coordinate(i, j))) {
                    ArrayList<Coordinate> CoordinateList = new ArrayList<>();
                    int bombCount = 0;
                    for (int x = -1; x < 2; ++x) {
                        for (int y = -1; y < 2; ++y) {
                            try {
                                if ((isNotVisible(i + x, j + y) || isFlag(i + x, j + y))
                                        && (x != 0 || y != 0)) {
                                    bombCount++;
                                    if (isNotVisible(i + x, j + y) && !isFlag(i + x, j + y)) {
                                        CoordinateList.add(new Coordinate(i + x, j + y));
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                continue;
                            }
                        }
                    }

                    if (bombCount == Character.getNumericValue(game.getNumber(new Coordinate(i, j)))) {
                        for (Coordinate c : CoordinateList) {
                            markFlag(c);
                        }
                    }
                }
            }
        }
        print();
        System.out.println();
    }


    public void print() {
        if (notFoundBombs == 0) {
            completed = true;
            game.printSolution();
            System.out.println("You won the game!");
        } else {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println("Remaining bombs to find are " + notFoundBombs);
        }
    }


    private void updateAfterFlag(Coordinate coords) {
        int bombX = coords.x;
        int bombY = coords.y;
        for (int x = -1; x < 2; ++x) {
            for (int y = -1; y < 2; ++y) {
                try {
                    doubleClick(bombX + x, bombY + y);
                } catch(ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
    }

    private void doubleClick(int x, int y) {
        ArrayList<Coordinate> CoordinateList = new ArrayList<Coordinate>();
        int count = 0;
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                if (isFlag(i + x, j + y)) {
                    count++;
                } else if (isNotVisible(i+x, j+y)) {
                    CoordinateList.add(new Coordinate(i + x, j + y));
                }
            }
        }
        if (count ==  Character.getNumericValue(game.getNumber(new Coordinate(x, y)))) {
            for (Coordinate c : CoordinateList) {
                getResultsOfClick(c);
            }
        }
    }


}
