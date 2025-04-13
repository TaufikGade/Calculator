package org.calculator.math.token;

public class ParenthesisToken extends ExpressionToken {
    private final boolean isOpen;

    public ParenthesisToken(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }
}