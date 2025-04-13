package org.calculator.math.token;

public class NumberToken extends ExpressionToken {
    private final double value;

    public NumberToken(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}