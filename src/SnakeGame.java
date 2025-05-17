import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Stack;

public class SnakeGame extends JPanel{
    private Stack<State> path;
    private int[][] board;
    private int[][] been;
    private SnakeNode head;
    private int length;
    private Point food;
    private int dx;
    private int dy;
    private int add;
    private static int squareSize = 20;
    private boolean lost;
    private boolean pause;
    public SnakeGame(){
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.RED);
        setIgnoreRepaint( true ) ;
        board = new int[40][80];
        been = new int[40][80];
        setSize(squareSize * board[0].length + 10, squareSize * board.length + 35);
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                int newdx = 0;
                int newdy = 0;
                switch(key) {
                    case KeyEvent.VK_LEFT:
                        newdx = -1;
                        break;
                    case KeyEvent.VK_RIGHT:
                        newdx = 1;
                        break;
                    case KeyEvent.VK_UP:
                        newdy = -1;
                        break;
                    case KeyEvent.VK_DOWN:
                        newdy = 1;
                        break;
                    case KeyEvent.VK_SPACE:
                        if(lost) {
                            clearBoard();
                            start();
                        }
                        else if(dx != dy) {
                            pause = !pause;
                        }
                        break;
                }
                if((!pause) && (newdx != newdy) && ((head.getChild() == null) || !(new Point((int) head.getCoordinate().getX() + newdx, (int) head.getCoordinate().getY() + newdy).equals(head.getChild().getCoordinate())))) {
                    dx = newdx;
                    dy = newdy;
                }
            }
        };
        this.addKeyListener(keyListener);
    }
    public int getWidth() {
        return squareSize * board[0].length + 10;
    }
    public int getLength() {
        return squareSize * board.length + 35;
    }
    public void start() {
        head = new SnakeNode(new Point(1, 1));
        food = newFood();
        dx = 0;
        dy = 0;
        add = 0;
        length = 1;
        lost = false;
        pause = false;
        repaint();
        Thread gameThread = new Thread() {
            public void run() {
                while(!lost) {
                    try {
                        Point headCoordinate = head.getCoordinate();
                        int x = (int)headCoordinate.getX() + dx;
                        int y = (int)headCoordinate.getY() + dy;
                        if((y < 0) || (x < 0) || (y >= board.length) || (x >= board[0].length)) {
                            lost = true;
                            repaint();
                        }
                        else {
                            if(!pause) {
                                if (add == 0) {
                                    Point delete = head.move(dx, dy);
                                    board[(int) delete.getY()][(int) delete.getX()] = 0;
                                } else {
                                    Point oldCoordinate = head.getCoordinate();
                                    head = new SnakeNode(new Point((int) oldCoordinate.getX() + dx, (int) oldCoordinate.getY() + dy), head);
                                    length++;
                                    add--;
                                }
                                if (head.getCoordinate().equals(food)) eatFood();
                                lost = checkLost();
                                repaint();
                            }
                            Thread.sleep(75);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        };
        gameThread.start();
        this.setVisible(true);
    }
    private boolean checkLost() {
        SnakeNode snake = head.getChild();
        while(snake != null) {
            Point coordinate = snake.getCoordinate();
            if (coordinate.equals(head.getCoordinate())) {
                return true;
            }
            snake = snake.getChild();
        }
        return false;
    }
    public boolean getLost() {
        return lost;
    }
    private Point newFood() {
        Point newFood = null;
        boolean isSnake = true;
        while(isSnake) {
            isSnake = false;
            int x = (int)(board[0].length*Math.random());
            int y = (int)(board.length*Math.random());
            newFood = new Point(x, y);
            SnakeNode snake = head;
            while(snake != null) {
                Point coordinate = snake.getCoordinate();
                if (coordinate.equals(newFood)) {
                    isSnake = true;
                    break;
                }
                snake = snake.getChild();
            }
        }
        return newFood;
    }
    private void eatFood() {
        add = 5;
        food = newFood();
    }
    private void clearBoard() {
        board = new int[40][80];
    }
    @Override
    public void paintComponent(Graphics g){
        SnakeNode snake = head;
        while(snake != null) {
            Point coordinate = snake.getCoordinate();
            board[(int)coordinate.getY()][(int)coordinate.getX()] = 1;
            snake = snake.getChild();
        }
        board[(int)food.getY()][(int)food.getX()] = 2;
        if(lost) {
            board[(int)head.getCoordinate().getY()][(int)head.getCoordinate().getX()] = 3;
        }
        super.paintComponent(g);
        g.translate(5, 30);
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                Color color = switch (board[row][col]) {
                    case 0 -> Color.BLUE;
                    case 1 -> Color.YELLOW;
                    case 2 -> Color.RED;
                    case 3 -> Color.CYAN;
                    case 4 -> Color.GREEN;
                    case 5 -> Color.BLACK;
                    default -> Color.WHITE;
                };
                g.setColor(color);
                g.fillRect(squareSize * col, squareSize * row, squareSize, squareSize);
                g.setColor(Color.BLUE);
                g.drawRect(squareSize * col, squareSize * row, squareSize, squareSize);
            }
        }
    }
    public String getTitle() {
        return "Snake - " + length;
    }

    public Point getHead() {
        return head.getCoordinate();
    }
    public Point getFood() {
        return food;
    }
    public int[][] getBoard() {
        int[][] newBoard = new int[40][80];
        SnakeNode snake = head;
        while(snake != null) {
            Point point = snake.getCoordinate();
            newBoard[(int)point.getY()][(int)point.getX()] = 1;
            snake = snake.getChild();
        }
        return newBoard;
    }
}
