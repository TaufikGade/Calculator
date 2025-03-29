// ExpressionTokenizer.java
package org.calculator.math.token;
import org.calculator.math.operator.Operator;
import org.calculator.math.exception.InvalidExpressionException;
import java.util.ArrayList;
import java.util.List;

public class ExpressionTokenizer {
    public List<ExpressionToken> tokenize(String expression) {
        List<ExpressionToken> tokens = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        expression = expression.replaceAll("\\s+", "").toLowerCase();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                buffer.append(c);
            } else if (c == '-' && (i == 0 || isOperatorOrParenthesis(expression.charAt(i - 1)))) {
                // 处理一元负号
                if (buffer.length() > 0) {
                    tokens.add(new NumberToken(Double.parseDouble(buffer.toString())));
                    buffer.setLength(0);
                }
                tokens.add(new OperatorToken("u-"));
            } else {
                if (buffer.length() > 0) {
                    tokens.add(parseNumber(buffer.toString()));
                    buffer.setLength(0);
                }

                if (c == '(' || c == ')') {
                    tokens.add(new ParenthesisToken(c == '('));
                } else if (Character.isLetter(c)) {
                    String func = parseFunction(expression, i);
                    tokens.add(new FunctionToken(func));
                    i += func.length() - 1;
                } else if (Operator.isOperator(String.valueOf(c))) {
                    tokens.add(new OperatorToken(String.valueOf(c)));
                } else {
                    throw new InvalidExpressionException("Invalid character: " + c);
                }
            }
        }

        if (buffer.length() > 0) {
            tokens.add(parseNumber(buffer.toString()));
        }

        return tokens;
    }

    private String parseFunction(String expr, int start) {
        int end = start;
        while (end < expr.length() && Character.isLetter(expr.charAt(end))) {
            end++;
        }
        return expr.substring(start, end);
    }

    private NumberToken parseNumber(String str) {
        try {
            return new NumberToken(Double.parseDouble(str));
        } catch (NumberFormatException e) {
            throw new InvalidExpressionException("Invalid number: " + str);
        }
    }

    private boolean isOperatorOrParenthesis(char c) {
        return c == '(' || c == ')' || Operator.isOperator(String.valueOf(c));
    }
}