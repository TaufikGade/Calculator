// OperatorToken.java
package org.calculator.math.token;

public class OperatorToken extends ExpressionToken {
    private final String symbol;

    public OperatorToken(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getStringValue() {
        return symbol;
    }

    @Override
    public double getNumericValue() {
        throw new UnsupportedOperationException("OperatorToken does not support numeric value");
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean isOperator() {
        return true;
    }
}
