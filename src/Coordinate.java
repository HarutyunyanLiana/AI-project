public class Coordinate {
    public int x;
    public int y;

    /**
     * The constructor of the class that gets the x and y coordinates.
     * @param x
     * @param y
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Helpful method for debugging that prints the x and y coordinates.
     */
    public void print() {
        System.out.println("x = " + x + " y = " + y);
    }
}

