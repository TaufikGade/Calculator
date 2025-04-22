// MathEvaluator.java
package org.calculator.math;

import org.calculator.math.converter.InfixToPostfixConverter;
import org.calculator.math.evaluator.PostfixEvaluator;
import org.calculator.math.token.ExpressionTokenizer;
import org.calculator.math.token.ExpressionToken;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.expression.F;

import java.util.List;

public class MathEvaluator {
    private final InfixToPostfixConverter converter;
    private final PostfixEvaluator evaluator;
    private final ExpressionTokenizer tokenizer;

    private final ExprEvaluator symjaEvaluator;

    public MathEvaluator() {
        this.converter = new InfixToPostfixConverter();
        this.evaluator = new PostfixEvaluator();
        this.tokenizer = new ExpressionTokenizer();
        this.symjaEvaluator = new ExprEvaluator();
        F.initSymbols();
    }

    public double evaluate(String expression) {
        List<ExpressionToken> tokens = tokenizer.tokenize(expression);
        List<ExpressionToken> postfix = converter.convert(tokens);
        return evaluator.evaluate(postfix);
    }

    public String derivativeWithSymja(String expression) {
        try {
            expression = convertLogbase(expression);

            StringBuilder sb = new StringBuilder(expression);
            int lb = 0, rb = 0;
            for (int i = 0; i < sb.length(); i++) {
                if (sb.charAt(i) == '×') sb.replace(i, i + 1, "*");
                if (sb.charAt(i) == '(') lb++;
                if (sb.charAt(i) == ')') rb++;
            }

            while (lb != rb) {
                sb.insert(sb.length(), ')');
                rb++;
            }
            String derivativeExpression = "D(" + sb + ", x)";
            IExpr result = symjaEvaluator.evaluate(derivativeExpression);
            //System.out.println(symjaEvaluator.evaluate("D(sin(x) + x^2, x)"));
            sb = new StringBuilder(result.toString().toLowerCase());
            for (int i = 0; i < sb.length(); i++) {
                if (sb.charAt(i) == '*') sb.replace(i, i + 1, "×");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Symja 求导失败: " + e.getMessage());
        }
    }

    public static String convertLogbase(String expression) {
        // 1. 处理括号包围的 a 和 b，例如 "(a + b) logbase (c)"
        String regex1 = "\\$([^)]+)\\$\\s+logbase\\s+\\$([^)]+)\\$";
        String replaced = expression.replaceAll(regex1, "log($1, $2)");

        // 2. 处理 a 带括号、b 不带括号，例如 "(a) logbase b"
        String regex2 = "\\$([^)]+)\\$\\s+logbase\\s+([^\\s]+)";
        replaced = replaced.replaceAll(regex2, "log($1, $2)");

        // 3. 处理 a 不带括号、b 带括号，例如 "a logbase (b)"
        String regex3 = "([^\\s]+)\\s+logbase\\s+\\$([^)]+)\\$";
        replaced = replaced.replaceAll(regex3, "log($1, $2)");

        // 4. 处理 a 和 b 都不带括号，例如 "3 logbase 5"
        String regex4 = "([^\\s]+)\\s+logbase\\s+([^\\s]+)";
        replaced = replaced.replaceAll(regex4, "log($1, $2)");

        return replaced;
    }

    public String solveEquation(String expression) {
        StringBuilder sb = new StringBuilder(expression);
        sb.replace(0, 0, "Solve({");
        sb.replace(sb.length(), sb.length(), "}, {x})");
        IExpr result = symjaEvaluator.evaluate(sb.toString());
        sb = new StringBuilder(result.toString());
        int index = sb.indexOf("->");
        while (index != -1) {
            sb.replace(index, index + 2, "==");
            index = sb.indexOf("->");
        }
        index = sb.indexOf("{");
        while (index != -1) {
            sb.replace(index, index + 1, "");
            index = sb.indexOf("{");
        }
        index = sb.indexOf("}");
        while (index != -1) {
            sb.replace(index, index + 1, "");
            index = sb.indexOf("}");
        }
        return sb.toString();
    }
}