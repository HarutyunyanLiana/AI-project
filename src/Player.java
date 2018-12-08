import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Player {
    private char[][] board;
    private Game game;
    private boolean completed;
    private int notFoundBombs;
    //private boolean start;

    public Player(Game game) {
        this.board = new char[game.getBoardWidth()][game.getBoardHeight()];
        for (char[] c : board) {
            Arrays.fill(c, '.');
        }
        this.game = game;
        this.completed = false;
        this.notFoundBombs = game.bombs;
        //this.start = false;
    }

    public void updateBoard(Coordinates coords, char to) {
        board[coords.getX()][coords.getY()] = to;
    }

    public void clickRandom() {
        //while (!start && completed) {
        while (!completed) {
            Random r = new Random();
            int rPos = r.nextInt(board.length);
            int cPos = r.nextInt(board[0].length);

            if (isNotVisible(rPos, cPos)) {
                Coordinates coords = new Coordinates(rPos, cPos);
                coords.print();
                getResultsOfClick(coords);
            }
        }
    }

    public void getResultsOfClick(Coordinates coords) {
        if (!completed) {
            if (game.isBomb(coords)) {
                for (char[] c : board) {
                    Arrays.fill(c, 'X');
                }
                completed = true;
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

    public void openZeros(Coordinates coords) {
        if (game.isZero(coords)) {
            updateBoard(coords, '0');
            for (int x = -1; x < 2; ++x) {
                for (int y = -1; y < 2; ++y) {
                    try {
                        if (isNotVisible(coords.getX() + x, coords.getY() + y)) {
                            openZeros(new Coordinates(coords.getX() + x, coords.getY() + y));
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
                if (game.isNumber(new Coordinates(i, j))) {
                    ArrayList<Coordinates> coordinatesList = new ArrayList<>();
                    int bombCount = 0;
                    for (int x = -1; x < 2; ++x) {
                        for (int y = -1; y < 2; ++y) {
                            try {
                                if ((isNotVisible(i + x, j + y) || isFlag(i + x, j + y))
                                        && (x != 0 || y != 0)) {
                                    bombCount++;
                                    if (isNotVisible(i + x, j + y) && !isFlag(i + x, j + y)) {
                                        coordinatesList.add(new Coordinates(i + x, j + y));
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                continue;
                            }
                        }
                    }

                    if (bombCount == Character.getNumericValue(game.getNumber(new Coordinates(i, j)))) {
                        for (Coordinates c : coordinatesList) {
                            markFlag(c);
                        }
                    }
                }
            }
        }
        print();
        System.out.println();
    }

    public void play() {
        clickRandom();
//        coverTrivialCases();
//        print();
//        while(completed) {
//            clickRandom();
//        }
    }

    public void print() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

        if (notFoundBombs == 0) {
            System.out.println("You won the game!");
            completed = true;
        } else {
            System.out.println("Remaining bombs to find are " + notFoundBombs);
        }
    }

    private boolean isNotVisible(int x, int y) {
        return board[x][y] == '.';
    }

    private boolean isFlag(int x, int y) {
        return board[x][y] == '#';
    }

    private void markFlag(Coordinates coords) {
        board[coords.getX()][coords.getY()] = '#';
        notFoundBombs--;
        updateAfterFlag(coords);
    }

    private void updateAfterFlag(Coordinates coords) {
        int bombX = coords.getX();
        int bombY = coords.getY();
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
        ArrayList<Coordinates> coordinatesList = new ArrayList<Coordinates>();
        int count = 0;
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                if (isFlag(i + x, j + y)) {
                    count++;
                } else if (isNotVisible(i+x, j+y)) {
                    coordinatesList.add(new Coordinates(i + x, j + y));
                }
            }
        }
        if (count ==  Character.getNumericValue(game.getNumber(new Coordinates(x, y)))) {
            for (Coordinates c : coordinatesList) {
                getResultsOfClick(c);
            }
        }
    }


}
