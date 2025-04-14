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
        // TODO:泛型的函数求导
        try {
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
}