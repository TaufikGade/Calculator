// MathEvaluator.java
package org.calculator.math;

import org.calculator.math.converter.InfixToPostfixConverter;
import org.calculator.math.evaluator.PostfixEvaluator;
import org.calculator.math.token.ExpressionTokenizer;
import org.calculator.math.token.ExpressionToken;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

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
    }

    public double evaluate(String expression) {
        List<ExpressionToken> tokens = tokenizer.tokenize(expression);
        List<ExpressionToken> postfix = converter.convert(tokens);
        return evaluator.evaluate(postfix);
    }
    public String derivativeWithSymja(String expression) {
        try {

            String derivativeExpression = "D(" + expression + ", x)";
            IExpr result = symjaEvaluator.evaluate(derivativeExpression);
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException("Symja 求导失败: " + e.getMessage());
        }
    }
}