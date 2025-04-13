// ExpressionToken.java
package org.calculator.math.token;

public abstract class ExpressionToken {
    public abstract String getStringValue();
    public abstract double getNumericValue();

    public boolean isFunction() {
        return false;
    }

    public boolean isOperator() {
        return false;
    }
}
