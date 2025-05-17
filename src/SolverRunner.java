import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.util.Stack;

public class SolverRunner {
    public static void main(String[] arg) {
        int n = 0;
        SnakeWindow window = new SnakeWindow();
        window.run();
        while(!window.isLost()) {
            Point food = window.getFood();
            //int[][] board;
            //int headi, foodi;
            MazeSolver solver = new MazeSolver(window.getBoard(), (int) (window.getHead().getX() + window.getHead().getY() * window.getBoard()[0].length), (int) (food.getX() + food.getY() * window.getBoard()[0].length));
            /*try {
                FileWriter output = new FileWriter("mazes/"+n+".txt");
                output.write(headi + "\n");
                output.write(foodi + "\n");
                for(int[] a: board) {
                    for(int b: a) {
                        output.write(b + "\n");
                    }
                }
                output.close();
            }
            catch(Exception ignored) {}*/
            Stack<State> path = solver.solveAdaptive();
            path.pop();
            while (!path.isEmpty()) {
                State state = path.pop();
                if(path.isEmpty()) System.out.println(".");
                int[] c = Coordinates.get2DFrom1D(state.getCoordinate(), window.getBoard().length, window.getBoard()[0].length);
                try {
                    Robot robot = new Robot();
                    Point head = window.getHead();
                    if (head.getY() - c[0] == -1) {
                        robot.keyPress(KeyEvent.VK_DOWN);
                    } else if (head.getY() - c[0] == 1) {
                        robot.keyPress(KeyEvent.VK_UP);
                    } else if (head.getX() - c[1] == -1) {
                        robot.keyPress(KeyEvent.VK_RIGHT);
                    } else if (head.getX() - c[1] == 1) {
                        robot.keyPress(KeyEvent.VK_LEFT);
                    }
                    while(head.equals(window.getHead()));
                } catch (Exception ignored) {
                }
                if(!path.isEmpty()) {
                    solver = new MazeSolver(window.getBoard(), (int) (window.getHead().getX() + window.getHead().getY() * window.getBoard()[0].length), (int) (food.getX() + food.getY() * window.getBoard()[0].length));
                    path = solver.solveAdaptive();
                    path.pop();
                }
            }
            while(food.equals(window.getFood()));
            //n++;
        }
    }
}
