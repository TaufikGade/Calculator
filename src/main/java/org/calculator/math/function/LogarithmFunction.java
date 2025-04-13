package org.calculator.math.function;

import org.calculator.math.Differentiable;

public class LogarithmFunction implements Differentiable {
    private final double base;

    public LogarithmFunction(double base) {
        this.base = base;
    }

    @Override
    public Differentiable derivative() {
        return new PowerFunction(-1).multiply(1 / (Math.log(base)));
    }

    public double evaluate(double x) {
        return Math.log(x) / Math.log(base);
    }
}
   