package org.calculator.math.operator;

import java.util.HashMap;
import java.util.Map;

public class Operator {
    private static final Map<String, Integer> PRECEDENCE = new HashMap<>();
    private static final Map<String, String> ASSOCIATIVITY = new HashMap<>();
    private static final Map<String, OperatorType> TYPES = new HashMap<>();

    static {
        // 定义运算符优先级、结合性和类型
        defineOperator("+", 1, "LEFT", OperatorType.BINARY);
        defineOperator("-", 1, "LEFT", OperatorType.BINARY);
        defineOperator("×", 2, "LEFT", OperatorType.BINARY);
        defineOperator("÷", 2, "LEFT", OperatorType.BINARY);
        defineOperator("%", 2, "LEFT", OperatorType.BINARY);
        defineOperator("yroot", 5, "LEFT", OperatorType.BINARY);
        defineOperator("logbase", 5, "LEFT", OperatorType.BINARY);
        defineOperator("^", 4, "RIGHT", OperatorType.BINARY);
        defineOperator("!", 5, "LEFT", OperatorType.UNARY);
        defineOperator("u-", 5, "RIGHT", OperatorType.UNARY); // 一元负号
        defineOperator("sin", 5, "LEFT", OperatorType.FUNCTION);
        defineOperator("cos", 5, "LEFT", OperatorType.FUNCTION);
        defineOperator("tan", 5, "LEFT", OperatorType.FUNCTION);
        defineOperator("log", 5, "LEFT", OperatorType.FUNCTION);
        defineOperator("ln", 5, "LEFT", OperatorType.FUNCTION);
        defineOperator("exp", 5, "LEFT", OperatorType.FUNCTION);
        defineOperator("sqrt", 5, "LEFT", OperatorType.FUNCTION);
        defineOperator("cbrt", 5, "LEFT", OperatorType.FUNCTION);
        defineOperator("abs", 5, "LEFT", OperatorType.FUNCTION);
    }

    private static void defineOperator(String symbol, int precedence, String associativity, OperatorType type) {
        PRECEDENCE.put(symbol, precedence);
        ASSOCIATIVITY.put(symbol, associativity);
        TYPES.put(symbol, type);
    }

    public static int getPrecedence(String operator) {
        return PRECEDENCE.getOrDefault(operator, -1);
    }

    public static String getAssociativity(String operator) {
        return ASSOCIATIVITY.get(operator);
    }

    public static OperatorType getType(String operator) {
        return TYPES.get(operator);
    }

    public static boolean isOperator(String token) {
        return PRECEDENCE.containsKey(token);
    }
}