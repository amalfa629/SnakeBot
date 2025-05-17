/**
 * Coordinates is Helper Class used to Convert Coordinates
 *
 * @author Tyler Amalfa, John Greaney-Cheng, Jesse Lerner
 */
public class Coordinates {
    private Coordinates(){}

    /**
     * Gets 1D Coordinates From 2D Coordinates
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param size Side Length of Maze
     * @return 1D Coordinate
     */
    public static int get1DFrom2D(int x, int y, int length, int width){
        return width*y + x;
    }

    /**
     * Gets 2D Coordinates From 1D Coordinates
     * @param coordinate 1D Coordinate
     * @param size Side Length of Maze
     * @return 2D Coordinates, [y,x]
     */
    public static int[] get2DFrom1D(int coordinate, int length, int width){
        int[] twoDimcoordinates = new int[2];
        twoDimcoordinates[0] = coordinate / width;
        twoDimcoordinates[1] = coordinate % width;
        return twoDimcoordinates;
    }
}
