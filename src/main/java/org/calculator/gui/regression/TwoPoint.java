package org.calculator.gui.regression;

public class TwoPoint {
    private double x, y;

    public TwoPoint() {
        this.x = 0;
        this.y = 0;
    }

    public TwoPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double y) {
        this.y = y;
    }
}
