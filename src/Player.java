import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Player {
    private char[][] board;
    private boolean completed;
    private int notFoundBombs;
    private Game game;
    public int success = 0;

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

    private boolean isOpenedNumber(int x, int y) {
        return (!isNotVisible(x, y) && !isFlag(x, y));
    }

    private void updateBoard(Coordinate coord, char to) {
        board[coord.x][coord.y] = to;
    }

    private void markFlag(Coordinate coord) {
        if (board[coord.x][coord.y] != '#') {
            board[coord.x][coord.y] = '#';
            notFoundBombs--;
            updateAfterFlag(coord);
        }
    }

    private void getResultsOfClick(Coordinate coord) {
        if (!completed) {
            if (game.isBomb(coord)) {
                for (char[] c : board) {
                    Arrays.fill(c, 'X');
                }
                completed = true;
                print();
                System.out.println("You lost the game! Try Again");
                return;
            } else if (game.isNumber(coord)) {
                updateBoard(coord, game.getNumber(coord));
//                print();
            } else {
                //start = true;
                openZeros(coord);
                //print();
                coverTrivialCases();
            }
            coverTrivialCases();

        }
    }


    private void openZeros(Coordinate coord) {
        if (game.isZero(coord)) {
            updateBoard(coord, '0');
            for (int x = -1; x < 2; ++x) {
                for (int y = -1; y < 2; ++y) {
                    try {
                        if (isNotVisible(coord.x + x, coord.y + y)) {
                            openZeros(new Coordinate(coord.x + x, coord.y + y));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }

            }
        } else {
            updateBoard(coord, game.getNumber(coord));
        }
    }


    private void deactivateCertainBombs(int i, int j) {
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

        if (bombCount == Character.getNumericValue(game.getNumber(new Coordinate(i, j)))) {
            for (Coordinate c : CoordinateList) {
                markFlag(c);
            }
//            print();
        }
    }

    private void coverTrivialCases() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (isOpenedNumber(i, j)) {
                    deactivateCertainBombs(i, j);
                }
            }
        }

//        System.out.println();
    }


    private void updateAfterFlag(Coordinate bomb) {
        int bombX = bomb.x;
        int bombY = bomb.y;
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
        if (count ==  Character.getNumericValue(game.getNumber(new Coordinate(x, y)))) {
            for (Coordinate c : CoordinateList) {
                getResultsOfClick(c);
//                c.print();
                if (!completed) {
                    doubleClick(c.x, c.y);
                }
            }
        }
    }

    public void play() {
        int count = 0;
        while (!completed) {
            count++;
//            System.out.println("aaaa" + count);
            Random r = new Random();
            int r_row = r.nextInt(board.length);
            int r_col = r.nextInt(board[0].length);

            if (isNotVisible(r_row, r_col)) {
                Coordinate coord = new Coordinate(r_row, r_col);
//                coord.print();
                getResultsOfClick(coord);
            }
            print();
        }
    }

    private void print() {
        if (notFoundBombs == 0) {
            completed = true;
            game.printSolution(true);
            System.out.println("You won the game!");
            success = 1;
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




}
