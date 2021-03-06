package stl.upmc.com.model;

public class Point {
    public int x;
    public int y;
    public int id;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = - 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                '}';
    }
}
