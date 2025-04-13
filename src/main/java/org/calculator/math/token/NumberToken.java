// NumberToken.java
package org.calculator.math.token;

public class NumberToken extends ExpressionToken {
    private final double value;

    public NumberToken(double value) {
        this.value = value;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(value);
    }

    @Override
    public double getNumericValue() {
        return value;
    }
}
