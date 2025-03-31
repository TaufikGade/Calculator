// InfixToPostfixConverter.java
package org.calculator.math.converter;

import org.calculator.math.token.*;
import org.calculator.math.operator.Operator;
import java.util.*;

public class InfixToPostfixConverter {
    public List<ExpressionToken> convert(List<ExpressionToken> tokens) {
        List<ExpressionToken> output = new ArrayList<>();
        Stack<ExpressionToken> stack = new Stack<>();

        for (ExpressionToken token : tokens) {
            if (token instanceof NumberToken) {
                output.add(token);
            } else if (token instanceof ParenthesisToken) {
                handleParenthesis((ParenthesisToken) token, stack, output);
            } else if (token instanceof FunctionToken) {
                stack.push(token);
            } else if (token instanceof OperatorToken) {
                handleOperator((OperatorToken) token, stack, output);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }

        return output;
    }

    private void handleParenthesis(ParenthesisToken token, Stack<ExpressionToken> stack,
                                   List<ExpressionToken> output) {
        if (token.isOpen()) {
            stack.push(token);
        } else {
            while (!stack.isEmpty() && !(stack.peek() instanceof ParenthesisToken)) {
                output.add(stack.pop());
            }
            stack.pop(); // 弹出左括号
            if (!stack.isEmpty() && stack.peek() instanceof FunctionToken) {
                output.add(stack.pop());
            }
        }
    }

    private void handleOperator(OperatorToken token, Stack<ExpressionToken> stack,
                                List<ExpressionToken> output) {
        String currOp = token.getSymbol();
        while (!stack.isEmpty() && stack.peek() instanceof OperatorToken) {
            String stackOp = ((OperatorToken) stack.peek()).getSymbol();
            if (shouldPop(currOp, stackOp)) {
                output.add(stack.pop());
            } else {
                break;
            }
        }
        stack.push(token);
    }

    private boolean shouldPop(String currOp, String stackOp) {
        int currPrec = Operator.getPrecedence(currOp);
        int stackPrec = Operator.getPrecedence(stackOp);
        if (currPrec == stackPrec) {
            return !"RIGHT".equals(Operator.getAssociativity(currOp));
        }
        return currPrec < stackPrec;
    }
}