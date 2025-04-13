// MathEvaluator.java
package org.calculator.math;

import org.calculator.math.converter.InfixToPostfixConverter;
import org.calculator.math.evaluator.PostfixEvaluator;
import org.calculator.math.exception.InvalidExpressionException;
import org.calculator.math.token.ExpressionToken;
import org.calculator.math.token.ExpressionTokenizer;
import org.calculator.math.token.NumberToken;

import java.util.List;
import java.util.Stack;

public class MathEvaluator {
    private final InfixToPostfixConverter converter;
    private final PostfixEvaluator evaluator;
    private final ExpressionTokenizer tokenizer;

    public MathEvaluator() {
        this.converter = new InfixToPostfixConverter();
        this.evaluator = new PostfixEvaluator();
        this.tokenizer = new ExpressionTokenizer();
    }

    public double evaluate(String expression) throws Exception {
        List<ExpressionToken> postfixTokens = converter.convert(tokenizer.tokenize(expression));
        return evaluator.evaluate(postfixTokens);
    }

    public String derivative(String expression) throws Exception {
        List<ExpressionToken> postfixTokens = converter.convert(tokenizer.tokenize(expression));
        return derivative(postfixTokens);
    }

    private String derivative(List<ExpressionToken> postfixTokens) {
        Stack<String> stack = new Stack<>();

        for (ExpressionToken token : postfixTokens) {
            if (token.isFunction()) {
                String funcName = token.getStringValue(); // 使用 getStringValue()
                String operand = stack.pop();

                switch (funcName) {
                    case "sin":
                        stack.push("cos(" + operand + ")");
                        break;
                    case "cos":
                        stack.push("-sin(" + operand + ")");
                        break;
                    case "tan":
                        stack.push("sec^2(" + operand + ")");
                        break;
                    case "asin":
                        stack.push("1 / sqrt(1 - " + operand + "^2)");
                        break;
                    case "acos":
                        stack.push("-1 / sqrt(1 - " + operand + "^2)");
                        break;
                    case "atan":
                        stack.push("1 / (1 + " + operand + "^2)");
                        break;
                    case "log":
                        stack.push("1 / (" + operand + " * ln(10))");
                        break;
                    case "ln":
                        stack.push("1 / " + operand);
                        break;
                    case "exp":
                        stack.push("exp(" + operand + ")");
                        break;
                    default:
                        throw new UnsupportedOperationException("不支持的函数求导: " + funcName);
                }
            } else if (token.isOperator()) {
                String operator = token.getStringValue(); // 使用 getStringValue()
                String rightOperand = stack.pop();
                String leftOperand = stack.pop();

                switch (operator) {
                    case "+":
                        stack.push(leftOperand + " + " + rightOperand);
                        break;
                    case "-":
                        stack.push(leftOperand + " - " + rightOperand);
                        break;
                    case "*":
                        stack.push(leftOperand + " * " + rightOperand + " + " + rightOperand + " * " + leftOperand);
                        break;
                    case "/":
                        stack.push("(" + leftOperand + " * " + rightOperand + " - " + rightOperand + " * " + leftOperand + ") / (" + rightOperand + " * " + rightOperand + ")");
                        break;
                    case "^":
                        stack.push(rightOperand + " * " + leftOperand + "^(" + (rightOperand + " - 1") + ") * " + leftOperand + "'");
                        break;
                    default:
                        throw new UnsupportedOperationException("不支持的运算符求导: " + operator);
                }
            } else {
                // 对于常数，求导结果为 0
                if (token instanceof NumberToken) {
                    stack.push("0");
                } else {
                    stack.push(token.getStringValue()); // 使用 getStringValue()
                }
            }
        }

        if (stack.size() != 1) {
            throw new InvalidExpressionException("最终栈中元素数量不为1，可能表达式格式错误");
        }

        return stack.pop();
    }
}
