import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class SnakeWindow extends JFrame{
    private SnakeGame game;
    public SnakeWindow() {
        game = new SnakeGame();
        add(game);
        getContentPane().setBackground(Color.RED);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void run() {
        setVisible(true);
        game.start();
        Thread t1 = new Thread() {
            public void run() {
                while(true) {
                    setTitle(game.getTitle());
                }
            }
        };
        t1.start();
    }
    public Point getHead() {
        return game.getHead();
    }
    public Point getFood() {
        return game.getFood();
    }
    public int[][] getBoard() {
        int[][] board = game.getBoard();
        board[(int)getFood().getY()][(int)getFood().getX()] = 0;
        //board[(int)getHead().getY()][(int)getHead().getX()] = 0;
        return board;
    }
    public boolean isLost() {
        return game.getLost();
    }
}
