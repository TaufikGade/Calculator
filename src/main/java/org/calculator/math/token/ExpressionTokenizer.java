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
        // 不要移除空格，我们将使用空格作为分隔符
        expression = expression.toLowerCase();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == ' ') {
                // 处理当前缓冲区中的数字
                if (buffer.length() > 0) {
                    tokens.add(parseNumber(buffer.toString()));
                    buffer.setLength(0);
                }

                // 查找下一个非空格字符的位置
                int nextNonSpaceIndex = i + 1;
                while (nextNonSpaceIndex < expression.length() &&
                        expression.charAt(nextNonSpaceIndex) == ' ') {
                    nextNonSpaceIndex++;
                }

                // 如果还有字符，且下一个字符是字母，则尝试解析特殊操作符或函数
                if (nextNonSpaceIndex < expression.length() &&
                        Character.isLetter(expression.charAt(nextNonSpaceIndex))){

                    int endIndex = nextNonSpaceIndex;
                    // 一直读取直到遇到空格或其他非字母字符
                    while (endIndex < expression.length() &&
                            Character.isLetter(expression.charAt(endIndex))) {
                        endIndex++;
                    }

                    String token = expression.substring(nextNonSpaceIndex, endIndex);
                    // 使用Operator类判断token类型
                    addTokenBasedOnType(tokens, token);
                    i = endIndex - 1; // 调整索引，减1是因为循环会自增
                }
            } else if (Character.isDigit(c) || c == '.') {
                buffer.append(c);
            } else if (c == '-' && (i == 0 || isOperatorOrParenthesis(expression.charAt(i - 1)) ||
                    expression.charAt(i - 1) == ' ')) {
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
                    // 使用Operator类判断token类型
                    addTokenBasedOnType(tokens, func);
                    i += func.length() - 1;
                } else {
                    String opStr = String.valueOf(c);
                    if (Operator.isOperator(opStr)) {
                        tokens.add(new OperatorToken(opStr));
                    } else {
                        throw new InvalidExpressionException("Invalid character: " + c);
                    }
                }
            }
        }

        if (buffer.length() > 0) {
            tokens.add(parseNumber(buffer.toString()));
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