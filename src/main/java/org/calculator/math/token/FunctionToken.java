// FunctionToken.java
package org.calculator.math.token;

public class FunctionToken extends ExpressionToken {
    private final String functionName;

    public FunctionToken(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String getStringValue() {
        return functionName;
    }

    @Override
    public double getNumericValue() {
        throw new UnsupportedOperationException("FunctionToken does not support numeric value");
    }

    public String getFunctionName() {
        return functionName;
    }

    @Override
    public boolean isFunction() {
        return true;
    }
}
