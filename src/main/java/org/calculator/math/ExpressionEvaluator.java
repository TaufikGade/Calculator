package org.calculator.math;

import java.util.*;

public class ExpressionEvaluator {
    private static final Map<String, Integer> OPERATOR_PRECEDENCE = new HashMap<>();

    static {
        OPERATOR_PRECEDENCE.put("sin", 4);
        OPERATOR_PRECEDENCE.put("cos", 4);
        OPERATOR_PRECEDENCE.put("tan", 4);
        OPERATOR_PRECEDENCE.put("^", 3); // 幂运算优先级高于乘除
        OPERATOR_PRECEDENCE.put("!", 3);
        OPERATOR_PRECEDENCE.put("*", 2);
        OPERATOR_PRECEDENCE.put("/", 2);
        OPERATOR_PRECEDENCE.put("+", 1);
        OPERATOR_PRECEDENCE.put("-", 1);
        OPERATOR_PRECEDENCE.put("(", 0);
    }

    public static double evaluate(String expression) {
        List<String> postfix = infixToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    // 1. 中缀表达式转换为后缀表达式（支持 `^` 幂运算）
    private static List<String> infixToPostfix(String expression) {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();
        StringBuilder numberBuffer = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // 处理数字
            if (Character.isDigit(c) || c == '.') {
                numberBuffer.append(c);
            } else {
                if (numberBuffer.length() > 0) {
                    output.add(numberBuffer.toString());
                    numberBuffer.setLength(0);
                }

                // 处理三角函数
                if (i + 2 < expression.length()) {
                    String func = expression.substring(i, Math.min(i + 3, expression.length()));
                    if (OPERATOR_PRECEDENCE.containsKey(func)) {
                        operators.push(func);
                        i += 2;
                        continue;
                    }
                }

                if (c == '(') {
                    operators.push("(");
                } else if (c == ')') {
                    while (!operators.isEmpty() && !operators.peek().equals("(")) {
                        output.add(operators.pop());
                    }
                    operators.pop();
                } else if (OPERATOR_PRECEDENCE.containsKey(String.valueOf(c))) {
                    while (!operators.isEmpty() && OPERATOR_PRECEDENCE.get(operators.peek()) >= OPERATOR_PRECEDENCE.get(String.valueOf(c))) {
                        output.add(operators.pop());
                    }
                    operators.push(String.valueOf(c));
                }
            }
        }

        if (numberBuffer.length() > 0) {
            output.add(numberBuffer.toString());
        }

        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        return output;
    }

    // 2. 计算后缀表达式
    private static double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (token.matches("-?\\d+(\\.\\d+)?")) { // 数字
                stack.push(Double.parseDouble(token));
            } else { // 计算操作符
                if (token.equals("!")) {
                    double a = stack.pop();
                    stack.push(factorial((int) a));
                } else if (token.equals("sin")) {
                    double a = stack.pop();
                    stack.push(Math.sin(Math.toRadians(a)));
                } else if (token.equals("cos")) {
                    double a = stack.pop();
                    stack.push(Math.cos(Math.toRadians(a)));
                } else if (token.equals("tan")) {
                    double a = stack.pop();
                    stack.push(Math.tan(Math.toRadians(a)));
                } else if (token.equals("^")) {
                    double b = stack.pop();
                    double a = stack.pop();
                    stack.push(Math.pow(a, b)); // 计算 a^b
                } else {
                    double b = stack.pop();
                    double a = stack.pop();
                    switch (token) {
                        case "+": stack.push(a + b); break;
                        case "-": stack.push(a - b); break;
                        case "*": stack.push(a * b); break;
                        case "/": stack.push(a / b); break;
                    }
                }
            }
        }

        return stack.pop();
    }

    // 3. 计算阶乘
    private static double factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("负数没有阶乘！");
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}

