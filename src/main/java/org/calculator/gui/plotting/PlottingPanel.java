package org.calculator.gui.plotting;

import org.calculator.math.MathEvaluator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlottingPanel extends JPanel {
    private GraphPanel graphPanel;
    private InputPanel inputPanel;
    private MathEvaluator evaluator;
    private List<FunctionData> functions;

    private JPanel cards; // 卡片面板
    private CardLayout cardLayout;

    public PlottingPanel() {
        // 初始化组件和数据
        setLayout(new BorderLayout());
        evaluator = new MathEvaluator();
        functions = new ArrayList<>();

        // 创建图形面板
        graphPanel = new GraphPanel(this);

        // 创建输入面板
        inputPanel = new InputPanel(this);

        // 创建卡片布局容器
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // 将面板添加到卡片布局容器中
        cards.add(inputPanel, "InputPanel");
        cards.add(graphPanel, "GraphPanel");

        // 添加卡片布局容器到主面板
        add(cards, BorderLayout.CENTER);

        // 创建顶部按钮面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // 切换到图形面板按钮
        JButton graphButton = new JButton("Graph");
        graphButton.addActionListener(e -> cardLayout.show(cards, "GraphPanel"));

        // 切换到输入面板按钮
        JButton inputButton = new JButton("Input");
        inputButton.addActionListener(e -> cardLayout.show(cards, "InputPanel"));

        // 将按钮添加到顶部面板
        topPanel.add(graphButton);
        topPanel.add(inputButton);

        // 将顶部按钮面板添加到主面板的顶部
        add(topPanel, BorderLayout.NORTH);

        // 确保图形面板刷新
        graphPanel.repaint();
    }

    // 添加函数到绘图列表
    public void addFunction(String expression) {
        if (expression != null && !expression.isEmpty()) {
            // 生成随机颜色，但避免太浅的颜色
            Color color = new Color(
                    (int)(Math.random() * 200),
                    (int)(Math.random() * 200),
                    (int)(Math.random() * 200)
            );

            FunctionData function = new FunctionData(expression, color);
            functions.add(function);

            // 重绘图形
            graphPanel.setFunctions(functions);
            graphPanel.repaint();
        }
    }

    // 清除所有函数
    public void clearFunctions() {
        functions.clear();
        graphPanel.setFunctions(functions);
        graphPanel.repaint();
    }

    // 获取函数计算器
    public MathEvaluator getEvaluator() {
        return evaluator;
    }

    // 数据类，存储函数表达式和绘图颜色
    public static class FunctionData {
        private String expression;
        private Color color;

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

    // 启动程序的主方法
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("函数计算器和绘图工具");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            PlottingPanel plottingPanel = new PlottingPanel();
            frame.getContentPane().add(plottingPanel);

            frame.setSize(600, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
