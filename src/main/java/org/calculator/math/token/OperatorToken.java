package org.calculator.math.token;

public class OperatorToken extends ExpressionToken {
    private final String symbol;

    public OperatorToken(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}