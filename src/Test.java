import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Stack;

public class Test {
    public static void main(String[] a) {
        int[][] maze = new int[40][80];
        String line = "";
        int head = 0;
        int food = 0;
        int s = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("mazes/12.txt"));
            head = Integer.parseInt(br.readLine());
            food = Integer.parseInt(br.readLine());
            while ((line = br.readLine()) != null) {
                int[] coordinates = Coordinates.get2DFrom1D(s, 40, 80);
                maze[coordinates[0]][coordinates[1]] = Integer.parseInt(line);
                s++;
            }
            br.close();
        }
        catch (Exception ignored) {
        }
        int[][] unknown = new int[40][80];
        for (int[] row : unknown)
            Arrays.fill(row, 2);
        MazeView knownMazeView = new MazeView(maze); //Maze has default size 30x30
        knownMazeView.setVisible(true);
        knownMazeView.setTitle("known");
        MazeView unknownMazeView = new MazeView(unknown); //Maze has default size 30x30
        unknownMazeView.setVisible(true);
        unknownMazeView.setTitle("unknown");
        MazeSolver solver = new MazeSolver(unknown, maze, head, food);
        Stack<State> path = solver.solveAdaptive();
        path.pop();
        while (!path.isEmpty()) {
            State state = path.pop();
            int[] c = Coordinates.get2DFrom1D(state.getCoordinate(), 40, 80);
            unknown[c[0]][c[1]] = 10;
        }
    }
}
