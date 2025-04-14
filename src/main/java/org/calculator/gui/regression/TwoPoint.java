package org.calculator.gui.regression;

public class TwoPoint {
    private final double x;
    private final double y;
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
}
