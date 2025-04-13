package org.calculator.math.function;

import org.calculator.math.Differentiable;

public class ExponentialFunction implements Differentiable {
    @Override
    public Differentiable derivative() {
        return this;
    }

    public double evaluate(double x) {
        return Math.exp(x);
    }
}
   