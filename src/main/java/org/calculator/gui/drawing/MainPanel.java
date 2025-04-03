package org.calculator.gui.drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class MainPanel extends JPanel {
    private final DrawingPanel topPanel;
    private final JPanel chartPanel; // 用于显示图表的面板
    private final JButton showDataButton; // 显示数据区域按钮
    private final BarChartPanel barChartPanel;
    private final PieChartPanel pieChartPanel;
    private final LineChartPanel lineChartPanel;
    private ChartPanel currentPanel = null;

    public MainPanel(DrawingPanel top) {
        this.topPanel = top;

        setLayout(new BorderLayout());

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));
        showDataButton = topPanel.initButton("数据");
        // 显示柱状图按钮
        JButton showBarChartButton = topPanel.initButton("柱状图");
        // 显示饼图按钮
        JButton showPieChartButton = topPanel.initButton("饼图");
        // 显示折线图按钮
        JButton showLineChartButton = topPanel.initButton("折线图");

        barChartPanel = new BarChartPanel(null, this);
        pieChartPanel = new PieChartPanel(null, this);
        lineChartPanel = new LineChartPanel(null, this);

        buttonPanel.add(showDataButton);
        buttonPanel.add(showBarChartButton);
        buttonPanel.add(showPieChartButton);
        buttonPanel.add(showLineChartButton);

        // 添加按钮事件
        showDataButton.addActionListener(e -> topPanel.switchDataPanelState());

        showBarChartButton.addActionListener(e -> showBarChartPanel());

        showPieChartButton.addActionListener(e -> updatePieChartPanel());

        showLineChartButton.addActionListener(e -> generateLineChart());

        // 创建图表面板
        chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());

        // 将图表面板添加到DrawingPanel
        add(chartPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // 更新柱状图
    private void showBarChartPanel() {
        // 获取数据
        var data = topPanel.getData();
        if (data.isEmpty()) return;

        // 创建柱状图面板
        barChartPanel.updateData(data);
        barChartPanel.setVisible(true);

        // 清空并添加柱状图面板到chartPanel
        chartPanel.removeAll();
        chartPanel.add(barChartPanel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
        currentPanel = barChartPanel;
    }

    // 更新饼状图
    private void updatePieChartPanel() {
        // 获取数据
        var data = topPanel.getData();
        if (data.isEmpty()) return;

        // 更新饼图面板
        pieChartPanel.updateData(data);
        pieChartPanel.setVisible(true);

        // 清空并添加饼图面板到chartPanel
        chartPanel.removeAll();
        chartPanel.add(pieChartPanel);
        chartPanel.revalidate();
        chartPanel.repaint();
        currentPanel = pieChartPanel;
    }

    // 更新折线图
    private void generateLineChart() {
        // 获取数据
        var data = topPanel.getData();
        if (data.isEmpty()) return;

        // 创建折线图面板
        lineChartPanel.updateData(data);
        lineChartPanel.setVisible(true);

        // 清空并添加折线图面板到chartPanel
        chartPanel.removeAll();
        chartPanel.add(lineChartPanel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
        currentPanel = lineChartPanel;
    }

    // 获取随机颜色
    public Color getRandomColor() {
        return new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }

    public void onDataPanelVisible(boolean isVisible) {
        showDataButton.setBackground(isVisible ? new Color(0, 103, 192) : new Color(238, 238, 238));
        if (!isVisible && currentPanel != null) {
            currentPanel.updateData(this.topPanel.getData());
        }
    }
}
