package org.calculator.gui.plotting;

import org.calculator.gui.ThemeColors;
import org.calculator.math.MathEvaluator;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.Color;

public class PlottingPanel extends JPanel {
    private GraphPanel graphPanel;
    private final InputPanel inputPanel;
    private MathEvaluator evaluator;
    private FunctionData function;
    private JPanel cards; // 卡片面板
    private CardLayout cardLayout;

    public PlottingPanel() {
        init();

        // 创建输入面板
        inputPanel = new InputPanel(this);

        // 将面板添加到卡片布局容器中
        cards.add(inputPanel, "InputPanel");

        // 确保图形面板刷新
        graphPanel.repaint();
        cardLayout.show(cards, "GraphPanel");
    }

    public PlottingPanel(String function) {
        init();

        // 创建输入面板
        inputPanel = new InputPanel(this, function);

        // 将面板添加到卡片布局容器中
        cards.add(inputPanel, "InputPanel");

        addFunction(function);
        // 确保图形面板刷新
        graphPanel.repaint();
        cardLayout.show(cards, "GraphPanel");
    }

    private void init() {
        // 初始化组件和数据
        setLayout(new BorderLayout());
        setBackground(ThemeColors.getLightBgColor());
        evaluator = new MathEvaluator();

        // 创建图形面板
        graphPanel = new GraphPanel(this);

        // 创建卡片布局容器
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setBackground(ThemeColors.getLightBgColor());

        cards.add(graphPanel, "GraphPanel");

        // 添加卡片布局容器到主面板
        add(cards, BorderLayout.CENTER);
    }

    // 添加函数到绘图列表
    public void addFunction(String expression) {
        if (expression != null && !expression.isEmpty()) {
            // 生成随机颜色，但避免太浅的颜色
            Color color = ThemeColors.getRandomColor();

            function = new FunctionData(expression, color);

            // 重绘图形
            graphPanel.setFunction(function);
            graphPanel.repaint();
        }
    }

    // 清除所有函数
    public void clearFunctions() {
        function = null;
        graphPanel.setFunction(null);
        graphPanel.repaint();
    }

    // 获取函数计算器
    public MathEvaluator getEvaluator() {
        return evaluator;
    }

    // 数据类，存储函数表达式和绘图颜色
    public static class FunctionData {
        private final String expression;
        private final Color color;

        public FunctionData(String expression, Color color) {
            this.expression = expression;
            this.color = color;
        }

        public String getExpression() {
            return expression;
        }

        public Color getColor() {
            return color;
        }
    }

    public void switchInputPanelState(boolean isShow) {
        if (isShow) {
            cardLayout.show(cards, "InputPanel");
        } else {
            inputPanel.calExpression();
            cardLayout.show(cards, "GraphPanel");
        }
    }

    public String getExpression() {
        if (function != null) {
            return function.getExpression();
        }
        else return null;
    }
}
