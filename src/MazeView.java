import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * MazeView Class is used to display maze graphically
 *
 * Note: When using this class, make sure to set visibility to on
 * i.e. MazeView view = new MazeView(maze, title);
 *      view.setVisible(true);
 *
 */
public class MazeView extends JFrame{
    private int[][] maze;
    private static int squareSize = 20;                 //Controls Dimension of Each Square in Maze


    /**
     * Creates a MazeView Object
     * @param maze the maze the MazeView Object displays
     * @param title the title the MazeView Object displays
     */
    public MazeView(int [][] maze, String title){
        setTitle(title);
        setSize(squareSize * maze.length + 10, squareSize * maze.length + 35);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.maze = maze;

        MouseListener mouseListener = new MouseAdapter() {                      //Updates the Maze when mouse is clicked
            @Override
            public void mouseClicked(MouseEvent e) {
                repaint();
            }
        };
        this.addMouseListener(mouseListener);
    }

    /**
     * Creates a MazeView Object
     * Sets default title
     * @param maze the maze the MazeView Object displays
     */
    public MazeView(int [][] maze){
        this(maze, "A* search");
    }

    /**
     * Colors the tiles of the MazeView Object
     *
     * Different Colors and their meanings:
     * (0) White - Unblocked Tile
     * (1) Black - Blocked Tile
     * (2) Gray - Unknown Tile
     * (3) Pink - Expanded Tile (one that the algorithm has visited)
     * (4) Yellow - Tile on Current Optimal Path (algorithm plans to visit it)
     * (5) Green - Start Tile
     * (6) Red - Target Tile
     * (Default) Blue - No Meaning, Purely for Error Checking
     *
     * @param g the specified Graphics window
     */
    @Override
    public void paint(Graphics g){
        super.paint(g);
        g.translate(5, 30);
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                Color color = switch (maze[row][col]) {
                    case 0 -> Color.WHITE;
                    case 1 -> Color.BLACK;
                    case 2 -> Color.GRAY;
                    case 3 -> Color.PINK;
                    case 4 -> Color.YELLOW;
                    case 5 -> Color.GREEN;
                    case 6 -> Color.RED;
                    default -> Color.BLUE;
                };
                g.setColor(color);
                g.fillRect(squareSize * col, squareSize * row, squareSize, squareSize);
                g.setColor(Color.BLACK);
                g.drawRect(squareSize * col, squareSize * row, squareSize, squareSize);
            }
        }
    }


}
