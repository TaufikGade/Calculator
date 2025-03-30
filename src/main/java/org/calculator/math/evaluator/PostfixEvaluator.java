// PostfixEvaluator.java
package org.calculator.math.evaluator;

import org.calculator.math.token.*;
import org.calculator.math.operator.Operator;
import org.calculator.math.exception.*;

import java.util.Stack;
import java.util.List;

public class PostfixEvaluator {
    public double evaluate(List<ExpressionToken> postfix) {
        Stack<Double> stack = new Stack<>();

        for (ExpressionToken token : postfix) {
            if (token instanceof NumberToken) {
                stack.push(((NumberToken) token).getValue());
            } else if (token instanceof OperatorToken) {
                processOperator((OperatorToken) token, stack);
            } else if (token instanceof FunctionToken) {
                processFunction((FunctionToken) token, stack);
            }
        }

        if (stack.size() != 1) {
            throw new InvalidExpressionException("Invalid expression format");
        }
        return stack.pop();
    }

    private void processOperator(OperatorToken token, Stack<Double> stack) {
        String symbol = token.getSymbol();
        switch (Operator.getType(symbol)) {
            case UNARY -> handleUnaryOperator(symbol, stack);
            case BINARY -> handleBinaryOperator(symbol, stack);
            default -> throw new UnsupportedOperatorException(symbol);
        }
    }

    private void processFunction(FunctionToken token, Stack<Double> stack) {
        String func = token.getFunctionName();
        double arg = stack.pop();
        switch (func) {
            case "sin" -> stack.push(Math.sin(Math.toRadians(arg)));
            case "cos" -> stack.push(Math.cos(Math.toRadians(arg)));
            case "tan" -> stack.push(Math.tan(Math.toRadians(arg)));
            case "log" -> stack.push(Math.log10(arg));
            case "ln" -> stack.push(Math.log(arg));
            case "exp" -> stack.push(Math.exp(arg));
            case "sqrt" -> stack.push(Math.sqrt(arg));
            case "cbrt" -> stack.push(Math.cbrt(arg));
            case "abs" -> stack.push(Math.abs(arg));
            default -> throw new UnsupportedOperatorException(func);
        }
    }
    private void handleUnaryOperator(String op, Stack<Double> stack) {
        if (op.equals("u-")) {
            stack.push(-stack.pop());
        } else if (op.equals("!")) {
            double num = stack.pop();
            validateFactorial(num);
            stack.push(factorial((int) num));
        }
    }

    private void handleBinaryOperator(String op, Stack<Double> stack) {
        double b = stack.pop();
        double a = stack.pop();
        switch (op) {
            case "+" -> stack.push(a + b);
            case "-" -> stack.push(a - b);
            case "×" -> stack.push(a * b);
            case "÷" -> {
                if (b == 0) throw new ArithmeticException("Division by zero");
                stack.push(a / b);
            }
            case "^" -> stack.push(Math.pow(a, b));
            default -> throw new UnsupportedOperatorException(op);
        }
    }

    private double factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative factorial");
        return n <= 1 ? 1 : n * factorial(n - 1);
    }

    private void validateFactorial(double num) {
        if (num < 0 || num != (int) num) {
            throw new IllegalArgumentException("Factorial requires non-negative integer");
        }
    }
}