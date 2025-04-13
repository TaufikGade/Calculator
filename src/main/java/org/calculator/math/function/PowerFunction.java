package org.calculator.math.function;

import org.calculator.math.Differentiable;

public class PowerFunction implements Differentiable {
    private final double exponent;

    public PowerFunction(double exponent) {
        this.exponent = exponent;
    }

    @Override
    public Differentiable derivative() {
        return new PowerFunction(exponent - 1).multiply(exponent);
    }

    public PowerFunction multiply(double factor) {
        // 返回乘以系数后的幂函数
        return new PowerFunction(exponent).setCoefficient(factor);
    }

    private PowerFunction setCoefficient(double coefficient) {
        // 设置系数的逻辑
        return this;
    }

    public double evaluate(double x) {
        return Math.pow(x, exponent);
    }
}
   