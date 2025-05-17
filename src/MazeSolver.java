import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/**
 * Class solves mazes using Repeated Forward A*, Repeated Backward A* or
 * Adaptive A*
 * 
 * @author Tyler Amalfa, John Greaney-Cheng, Jesse Lerner
 */
public class MazeSolver {
    private int[][] unknownMaze;
    private int[][] knownMaze;
    private int length;
    private int width;
    private int start;
    private int target;
    private int expandedCells;
    private char tiebreaker;
    private int[] search;
    private int[] g;
    private HashMap<Integer, Integer> h;
    private PriorityQueue open;
    private ArrayList<Integer> closed;
    private Stack<State> path;
    private final int COST = 1;
    public static final int[][] NEIGHBORS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    /**
     * Creates a RepeatedForwardAStar instance
     * 
     * @param unknownMaze grid the algorithm is working in
     * @param knownMaze   maze that we are solving
     * @param start       coordinates of start tile
     * @param target      coordinates of target tile
     * @param tiebreaker  Used For Breaking Ties between States with same f values
     */
    public MazeSolver(int[][] unknownMaze, int[][] knownMaze, int start, int target) {
        this.unknownMaze = unknownMaze;
        this.knownMaze = knownMaze;
        this.length = unknownMaze.length;
        this.width = unknownMaze[0].length;
        this.start = start;
        this.target = target;
        this.tiebreaker = 'g';
        expandedCells = 0;
        search = new int[length * width];
        g = new int[length * width];
        int[] startC = Coordinates.get2DFrom1D(start, length, width);
        int[] targetC = Coordinates.get2DFrom1D(target, length, width);
        knownMaze[startC[0]][startC[1]] = 5;
        unknownMaze[startC[0]][startC[1]] = 5;
        knownMaze[targetC[0]][targetC[1]] = 6;
        unknownMaze[targetC[0]][targetC[1]] = 6;
    }
    public MazeSolver(int[][] knownMaze, int start, int target) {
        this.unknownMaze = new int[knownMaze.length][knownMaze[0].length];
        this.knownMaze = knownMaze;
        this.length = unknownMaze.length;
        this.width = unknownMaze[0].length;
        this.start = start;
        this.target = target;
        this.tiebreaker = 'g';
        expandedCells = 0;
        search = new int[length * width];
        g = new int[length * width];
        int[] startC = Coordinates.get2DFrom1D(start, length, width);
        int[] targetC = Coordinates.get2DFrom1D(target, length, width);
        knownMaze[startC[0]][startC[1]] = 5;
        unknownMaze[startC[0]][startC[1]] = 5;
        knownMaze[targetC[0]][targetC[1]] = 6;
        unknownMaze[targetC[0]][targetC[1]] = 6;
    }

    /**
     * Creates a mazeSolver instance. Assumes start is (0,0) and end is (size-1,
     * size,1).
     * Does not take in an Unknown maze for viewing purposes. This is meant for
     * testing
     * many mazes under the same conditions without the extra viewing overhead.
     * 
     * @param knownMaze  maze that we are solving
     * @param tiebreaker Used For Breaking Ties between States with same f values
     */
    /*public MazeSolver(int[][] knownMaze, char tiebreaker) {
        this.knownMaze = knownMaze;
        size = knownMaze.length;

        unknownMaze = new int[size][size];
        for (int[] row : unknownMaze)
            Arrays.fill(row, 2); // Marks all squares as unknown

        // Sets start at top left and target at bottom right
        start = Coordinates.get1DFrom2D(0, 0, size);
        target = Coordinates.get1DFrom2D(101 - 1, 101 - 1, size);

        this.tiebreaker = tiebreaker;

        expandedCells = 0;
        search = new int[size * size];
        g = new int[size * size];
    }*/

    /**
     * Computes Shortest Presumed Unblocked Path using A*
     * Returns Target State, which specifies Path through Tree Pointers (Parent in
     * State Class)
     * 
     * @param counter The number iteration of A* being run
     * @return Target State
     */
    private void search(int[][] board, boolean[][] visited, int x, int y) {
        for(int[] neighbor: NEIGHBORS) {
            int newx = x + neighbor[1];
            int newy = y + neighbor[0];
            if(newx >= 0 && newx < width && newy >= 0 && newy < length && !visited[newy][newx]) {
                visited[newy][newx] = true;
                if ((board[newy][newx]) != 1) search(board, visited, newx, newy);
            }
        }
    }
    private boolean closedOff(int x, int y) {
        int[][] board = new int[length][width];
        for(int a = 0; a < length; a++) {
            System.arraycopy(knownMaze[a], 0, board[a], 0, width);
        }
        board[y][x] = 1;
        boolean[][] visited = new boolean[length][width];
        search(board, visited, 0, 0);
        for(int a = 0; a < length; a++) {
            for(int b = 0; b < width; b++) {
                if(!visited[a][b]) return true;
            }
        }
        return false;
    }
    private State ComputePath(int counter, int target) {
        State targetState = null;
        while (g[target] > open.peek().getF()) {
            State state = open.pop();
            closed.add(state.getCoordinate());
            expandedCells++;
            int stateCoordinates[] = Coordinates.get2DFrom1D(state.getCoordinate(), length, width);
            for (int[] neighbor : NEIGHBORS) {
                int currentNeighborX = stateCoordinates[1] + neighbor[1];
                int currentNeighborY = stateCoordinates[0] + neighbor[0];
                if (currentNeighborX >= 0 && currentNeighborX < width && currentNeighborY >= 0 && currentNeighborY < length
                        && knownMaze[currentNeighborY][currentNeighborX] != 1 ) { //&& !closedOff(currentNeighborX, currentNeighborY)
                    int currentNeighbor = Coordinates.get1DFrom2D(currentNeighborX, currentNeighborY, length, width);
                    if (search[currentNeighbor] < counter) {
                        g[currentNeighbor] = Integer.MAX_VALUE;
                        search[currentNeighbor] = counter;
                    }
                    if (g[currentNeighbor] > g[state.getCoordinate()] + COST) {
                        g[currentNeighbor] = g[state.getCoordinate()] + COST;
                        open.remove(new State(currentNeighbor));
                        if (currentNeighbor == target) { // Used to Help Return Target State
                            targetState = new State(currentNeighbor, g[currentNeighbor], computeH(currentNeighbor),
                                    state);
                            open.insert(targetState);
                        } else {
                            open.insert(
                                    new State(currentNeighbor, g[currentNeighbor], computeH(currentNeighbor), state));
                        }
                    }
                }
            }
        }
        return targetState;
    }

    /**
     * Runs Repeated Forward A* to solve given maze
     * 
     * @return returns a solveResult with whether the solve suceeded and the number
     *         of expansions taken
     */
    public Stack<State> solveForward() {
        int counter = 0;
        int currentTile = start;
        while (currentTile != target) {
            counter++;
            g[currentTile] = 0;
            search[currentTile] = counter;
            g[target] = Integer.MAX_VALUE;
            search[target] = counter;

            open = new PriorityQueue(length * width, tiebreaker);
            closed = new ArrayList<Integer>();
            open.insert(new State(currentTile, g[currentTile], computeH(currentTile)));
            State targetState = ComputePath(counter, target);

            if (open.isEmpty()) {
                return null;
            }

            path = new Stack<>();
            State curr = targetState;
            while (curr != null) {
                path.push(curr);
                int[] coordinates = Coordinates.get2DFrom1D(curr.getCoordinate(), length, width);
                unknownMaze[coordinates[0]][coordinates[1]] = 4; // Colors SPUP Yellow
                curr = curr.getParent();
            }

            State wherePathEnded = followPath(); // Follows SPUP until Blocked
            resetColors(); // Resets color of SPUP unexpanded Tiles
            currentTile = wherePathEnded.getCoordinate();
        }
        return path;
    }

    /**
     * Runs Repeated Backward A* to solve given maze
     * 
     * @return returns a solveResult with whether the solve suceeded and the number
     *         of expansions taken
     */
    public solveResult solveBackward() {
        int counter = 0;
        int currentTile = start;
        while (currentTile != target) {
            counter++;
            g[target] = 0;
            search[target] = counter;
            g[currentTile] = Integer.MAX_VALUE;
            search[currentTile] = counter;

            open = new PriorityQueue(length * width, tiebreaker);
            closed = new ArrayList<Integer>();
            open.insert(new State(target, g[target], computeH(target)));
            State pathState = ComputePath(counter, currentTile); // In backward A* the state itself acts as the path
                                                                 // stack

            if (open.isEmpty()) {
                return new solveResult(false, expandedCells);
            }
            State wherePathEnded = followPath(pathState); // Follows SPUP until Blocked
            resetColors(); // Resets color of SPUP unexpanded Tiles
            currentTile = wherePathEnded.getCoordinate();
        }
        return new solveResult(true, expandedCells);
    }

    /**
     * Runs Adaptive A* to solve the maze
     * 
     * @return returns a solveResult with whether the solve suceeded and the number
     *         of expansions taken
     */
    public Stack<State> solveAdaptive() {
        h = new HashMap<>();
        int counter = 0;
        int currentTile = start;
        while (currentTile != target) {
            counter++;
            g[currentTile] = 0;
            search[currentTile] = counter;
            g[target] = Integer.MAX_VALUE;
            search[target] = counter;
            open = new PriorityQueue(length * width, tiebreaker);
            closed = new ArrayList<Integer>();
            open.insert(new State(currentTile, g[currentTile], computeH(currentTile)));
            State targetState = ComputePath(counter, target);
            if (open.isEmpty()) {
                return null;
            }

            path = new Stack<>();
            State curr = targetState;
            while (curr != null) {
                path.push(curr);
                int[] coordinates = Coordinates.get2DFrom1D(curr.getCoordinate(), length, width);
                unknownMaze[coordinates[0]][coordinates[1]] = 4; // Colors SPUP Yellow
                curr = curr.getParent();
            }
            State wherePathEnded = followPath(); // Follows SPUP until Blocked
            resetColors(); // Resets color of SPUP unexpanded Tiles
            currentTile = wherePathEnded.getCoordinate();
            for (Integer s : closed) {
                h.put(s, g[target] - g[s]);
            }
        }
        return path;
    }

    /**
     * Expands the tiles along the SPUP until blocked.
     * Colors Tiles we Expand Pink, and its Neighbours Black and White (White
     * doesn't override Pink though)
     * Note: Since Start Tile is guaranteed unblocked and we return Tile immediately
     * before first blocked tile (which is next Start Tile), we know that first tile
     * on Stack (which is the Start Tile) is guaranteed to be unblocked
     * 
     * @return Target State if SPUP was never blocked
     *         Otherwise, returns State immediately before first blocked tile
     */
    private State followPath() {
        Stack<State> path2 = (Stack<State>)path.clone();
        State currentState = path2.pop();
        while (currentState.getCoordinate() != target) {
            int[] currentStateCoordinates = Coordinates.get2DFrom1D(currentState.getCoordinate(), length, width);
            unknownMaze[currentStateCoordinates[0]][currentStateCoordinates[1]] = 3; // Colors Tiles we Expanded Pink
            for (int[] neighbor : NEIGHBORS) {
                int currentNeighborX = currentStateCoordinates[1] + neighbor[1];
                int currentNeighborY = currentStateCoordinates[0] + neighbor[0];
                if (currentNeighborX >= 0 && currentNeighborX < width && currentNeighborY >= 0
                        && currentNeighborY < length) {
                    if (knownMaze[currentNeighborY][currentNeighborX] == 1) {
                        unknownMaze[currentNeighborY][currentNeighborX] = 1;
                    } else if (unknownMaze[currentNeighborY][currentNeighborX] == 2
                            || unknownMaze[currentNeighborY][currentNeighborX] == 4) {
                        unknownMaze[currentNeighborY][currentNeighborX] = 0;
                    }
                }
            }
            int[] nextCoordinates = Coordinates.get2DFrom1D(path2.peek().getCoordinate(), length, width);
            if (unknownMaze[nextCoordinates[0]][nextCoordinates[1]] == 1) { // Returns first time next
                return currentState; // coordinates are blocked
            }
            currentState = path2.pop();
        }
        return currentState;
    }

    /**
     * Expands the tiles along the SPUP until blocked.
     * Colors Tiles we Expand Pink, and its Neighbours Black and White (White
     * doesn't override Pink though)
     * Note: Since Start Tile is guaranteed unblocked and we return Tile immediately
     * before first blocked tile (which is next Start Tile), we know that first tile
     * on Stack (which is the Start Tile) is guaranteed to be unblocked
     * 
     * @param backwardpath the state containing the path stack. In the backward
     *                     case,
     *                     the parent chain from computePath is already in the
     *                     correct
     *                     direction of the stack necessary for followPath
     * @return Target State if SPUP was never blocked
     *         Otherwise, returns State immediately before first blocked tile
     */
    private State followPath(State backwardpath) {
        State currentState = backwardpath.getParent();
        while (currentState.getCoordinate() != target) {
            int[] currentStateCoordinates = Coordinates.get2DFrom1D(currentState.getCoordinate(), length, width);
            unknownMaze[currentStateCoordinates[0]][currentStateCoordinates[1]] = 3; // Colors Tiles we Expanded Pink
            for (int[] neighbor : NEIGHBORS) {
                int currentNeighborX = currentStateCoordinates[1] + neighbor[1];
                int currentNeighborY = currentStateCoordinates[0] + neighbor[0];
                if (currentNeighborX >= 0 && currentNeighborX < width && currentNeighborY >= 0
                        && currentNeighborY < length) {
                    if (knownMaze[currentNeighborY][currentNeighborX] == 1) {
                        unknownMaze[currentNeighborY][currentNeighborX] = 1;
                    } else if (unknownMaze[currentNeighborY][currentNeighborX] == 2
                            || unknownMaze[currentNeighborY][currentNeighborX] == 4) {
                        unknownMaze[currentNeighborY][currentNeighborX] = 0;
                    }
                }
            }
            int[] nextCoordinates = Coordinates.get2DFrom1D(currentState.getParent().getCoordinate(), length, width);
            if (unknownMaze[nextCoordinates[0]][nextCoordinates[1]] == 1) { // Returns first time next
                return currentState; // coordinates are blocked
            }
            currentState = currentState.getParent();
        }
        return currentState;
    }

    /**
     * Helper Method that computes the heuristic for a given coordinate
     * For a given coordinate, Heuristic returns Manhattan Distance from
     * given coordinate to target
     * 
     * @param coordinate Coordinate that method computes Heuristic for
     * @return Manhattan Distance from given coordinate to target
     */
    private int computeH(int coordinate) {
        if ((h != null) && (h.containsKey(coordinate)))
            return h.get(coordinate);
        int[] currentCoordinates = Coordinates.get2DFrom1D(coordinate, length, width);
        int[] targetCoordinates = Coordinates.get2DFrom1D(target, length, width);
        return (Math.abs(targetCoordinates[0] - currentCoordinates[0])
                + Math.abs(targetCoordinates[1] - currentCoordinates[1]));
    }

    /**
     * Helper Method that Resets Colors after Iteration of Forward A*
     * Replaces Any Shortest Presumed Path Tiles that haven't been expanded (Yellow,
     * 4)
     * with Unknown Tiles (Gray, 2)
     * Restores Colors of Start and Target Tiles
     */
    private void resetColors() {
        for (int i = 0; i < unknownMaze.length; i++) {
            for (int j = 0; j < unknownMaze[0].length; j++) {
                if (unknownMaze[i][j] == 4) { // Replaces Yellow with Gray
                    unknownMaze[i][j] = 2;
                }
            }
        }
        int[] startCoordinates = Coordinates.get2DFrom1D(start, length, width);
        int[] targetCoordinates = Coordinates.get2DFrom1D(target, length, width);
        unknownMaze[startCoordinates[0]][startCoordinates[1]] = 5; // Restores Start
        unknownMaze[targetCoordinates[0]][targetCoordinates[1]] = 6; // Restores Target
    }

}
