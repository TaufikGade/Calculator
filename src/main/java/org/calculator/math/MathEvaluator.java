// MathEvaluator.java
package org.calculator.math;

import org.calculator.math.converter.InfixToPostfixConverter;
import org.calculator.math.evaluator.PostfixEvaluator;
import org.calculator.math.token.ExpressionTokenizer;
import org.calculator.math.token.ExpressionToken;

import java.util.List;

public class MathEvaluator {
    private final InfixToPostfixConverter converter;
    private final PostfixEvaluator evaluator;
    private final ExpressionTokenizer tokenizer;

    public MathEvaluator() {
        this.converter = new InfixToPostfixConverter();
        this.evaluator = new PostfixEvaluator();
        this.tokenizer = new ExpressionTokenizer();
    }

    public double evaluate(String expression) {
        List<ExpressionToken> tokens = tokenizer.tokenize(expression);
        List<ExpressionToken> postfix = converter.convert(tokens);
        return evaluator.evaluate(postfix);
    }
}