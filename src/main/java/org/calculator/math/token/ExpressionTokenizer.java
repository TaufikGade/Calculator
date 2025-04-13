package org.calculator.math.token;
import org.calculator.math.operator.Operator;
import org.calculator.math.operator.OperatorType;
import org.calculator.math.exception.InvalidExpressionException;
import java.util.ArrayList;
import java.util.List;

public class ExpressionTokenizer {
    public List<ExpressionToken> tokenize(String expression) {
        List<ExpressionToken> tokens = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        int i = 0;

        while (i < expression.length()) {
            char c = expression.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
            } else if (Character.isLetter(c)) {
                int endIndex = i;
                while (endIndex < expression.length() && Character.isLetter(expression.charAt(endIndex))) {
                    endIndex++;
                }
                String token = expression.substring(i, endIndex);
                if (Operator.isOperator(token)) {
                    tokens.add(new OperatorToken(token));
                } else {
                    tokens.add(new FunctionToken(token));
                }
                i = endIndex;
            } else if (Character.isDigit(c) || c == '.') {
                buffer.append(c);
                if (i + 1 < expression.length() && (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '.')) {
                    i++;
                } else {
                    tokens.add(new NumberToken(Double.parseDouble(buffer.toString())));
                    buffer.setLength(0);
                    i++;
                }
            } else if (c == '(' || c == ')') {
                if (buffer.length() > 0) {
                    tokens.add(new NumberToken(Double.parseDouble(buffer.toString())));
                    buffer.setLength(0);
                }
                tokens.add(new ParenthesisToken(c == '('));
                i++;
            } else {
                if (buffer.length() > 0) {
                    tokens.add(new NumberToken(Double.parseDouble(buffer.toString())));
                    buffer.setLength(0);
                }
                String opStr = String.valueOf(c);
                if (Operator.isOperator(opStr)) {
                    tokens.add(new OperatorToken(opStr));
                } else {
                    throw new InvalidExpressionException("Invalid character: " + c);
                }
                i++;
            }
        }

        return tokens;
    }

    private void addTokenBasedOnType(List<ExpressionToken> tokens, String token) {
        if (Operator.isOperator(token)) {
            OperatorType type = Operator.getType(token);
            if (type == OperatorType.FUNCTION) {
                tokens.add(new FunctionToken(token));
            } else {
                tokens.add(new OperatorToken(token));
            }
        } else {
            throw new InvalidExpressionException("Unsupported token: " + token);
        }
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