package org.calculator.math.token;

public class FunctionToken extends ExpressionToken {
    private final String functionName;

    public FunctionToken(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }
}
