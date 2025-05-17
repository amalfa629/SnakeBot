import java.awt.*;

public class SnakeNode {
    private SnakeNode child;
    private Point coordinate;
    public SnakeNode(Point coordinate) {
        child = null;
        this.coordinate = coordinate;
    }
    public SnakeNode(Point coordinate, SnakeNode child) {
        this.child = child;
        this.coordinate = coordinate;
    }
    public Point move(int dx, int dy) {
        Point oldCoordinate = this.coordinate;
        this.coordinate = new Point((int)oldCoordinate.getX() + dx, (int)oldCoordinate.getY() + dy);
        if(child != null) return child.move(oldCoordinate);
        else return oldCoordinate;
    }
    public Point move(Point coordinate) {
        Point oldCoordinate = this.coordinate;
        this.coordinate = coordinate;
        if(child != null) return child.move(oldCoordinate);
        else return oldCoordinate;
    }
    public SnakeNode getChild() {
        return child;
    }
    public Point getCoordinate() {
        return coordinate;
    }
}
