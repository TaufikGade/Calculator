package org.calculator.gui.drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

public class MainPanel extends JPanel {
    private final DrawingPanel topPanel;
    private final JPanel chartPanel; // 用于显示图表的面板
    private final JButton showDataButton; // 显示数据区域按钮

    private final int LINE_PADDING = 15; // 折线图间距
    private final int BAR_PADDING = 25; // 柱状图间距

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

        buttonPanel.add(showDataButton);
        buttonPanel.add(showBarChartButton);
        buttonPanel.add(showPieChartButton);
        buttonPanel.add(showLineChartButton);

        // 添加按钮事件
        showDataButton.addActionListener(e -> topPanel.switchDataPanelState());

        showBarChartButton.addActionListener(e -> generateBarChart());

        showPieChartButton.addActionListener(e -> generatePieChart());

        showLineChartButton.addActionListener(e -> generateLineChart());

        // 创建图表面板
        chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());

        // 将图表面板添加到DrawingPanel
        add(chartPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void generateBarChart() {
        // 获取数据
        var data = topPanel.getData();
        if (data.isEmpty()) return;
        // 创建柱状图面板
        JPanel barChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth() - 2 * BAR_PADDING;
                int height = getHeight() - 2 * BAR_PADDING;

                int barWidth = width / data.size();
                double maxData = Math.max(data.getFirst(), data.stream().mapToDouble(Double::doubleValue).max().orElse(1));
                double minData = Math.min(data.getFirst(), data.stream().mapToDouble(Double::doubleValue).min().orElse(1));
                double dataDiff = maxData - minData;

                if (minData < 0) {
                    int y = (int) ((1 - (Math.abs(minData) / dataDiff)) * height);
                    g.drawLine(BAR_PADDING, y, width + BAR_PADDING, y);
                    g.drawString("0", 10, y);

                    for (int i = 0; i < data.size(); i++) {
                        double num = data.get(i);
                        g.setColor(getRandomColor()); // 随机颜色
                        if (num > 0) {
                            int barHeight = (int) (num / dataDiff * height) - BAR_PADDING;
                            g.fillRect(i * barWidth + BAR_PADDING, y - barHeight, barWidth, barHeight);
                            g.setColor(Color.BLACK);
                            g.drawString("Data " + (i + 1), i * barWidth + 30, y - 10);
                        } else {
                            int barHeight = (int) (-num / dataDiff * height) + BAR_PADDING;
                            g.fillRect(i * barWidth + BAR_PADDING, y, barWidth, barHeight);
                            g.setColor(Color.BLACK);
                            g.drawString("Data " + (i + 1), i * barWidth + 30, y + 15);
                        }


                    }
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        int barHeight = (int) (((data.get(i)) / maxData) * height);
                        g.setColor(getRandomColor()); // 随机颜色
                        g.fillRect(i * barWidth + BAR_PADDING, height - barHeight + BAR_PADDING, barWidth, barHeight);
                        g.setColor(Color.BLACK);
                        g.drawString("Data " + (i + 1), i * barWidth + 30, height + 10);
                    }
                }
                // 绘制柱状图


                // 绘制 Y 方向的单位分割线
                int numTicks = 5; // Y 轴的分割线数量
                for (int i = 0; i <= numTicks; i++) {
                    int y = height - (i * height / numTicks) + BAR_PADDING;
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawLine(BAR_PADDING, y, width + BAR_PADDING, y); // 绘制分割线
                    g.setColor(Color.BLACK);
                    g.drawString(String.valueOf(Math.round(i * dataDiff / numTicks + minData)), 10, y); // 标注数值
                    g.setFont(new Font("Arial", Font.PLAIN, 12));
                }
            }
        };
        barChartPanel.setBackground(Color.white);
        barChartPanel.setPreferredSize(new Dimension(400, 300));
        barChartPanel.setBorder(BorderFactory.createTitledBorder("柱状图"));

        // 清空并添加柱状图面板到chartPanel
        chartPanel.removeAll();
        chartPanel.add(barChartPanel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    // 生成饼图
    private void generatePieChart() {
        // 获取数据
        var data = topPanel.getData();
        if (data.isEmpty()) return;
        // 创建饼图面板
        JPanel pieChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                int radius = Math.min(width, height) - LINE_PADDING;

                int total = data.stream().mapToInt(Double::intValue).sum();
                double startAngle = 0;

                int x = (width - radius) / 2;
                int y = (height - radius) / 2;

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for (Double datum : data) {
                    g2d.setColor(getRandomColor());
                    // 使用Arc2D和Path2D确保平滑边缘
                    double arcAngle = (datum / total) * 360;
                    Arc2D arc = new Arc2D.Double(x, y, radius, radius, startAngle, arcAngle, Arc2D.PIE);
                    Path2D path = new Path2D.Double();
                    path.append(arc, true);
                    g2d.fill(path);
                    startAngle += arcAngle;
                }
            }
        };
        pieChartPanel.setBackground(Color.white);
        pieChartPanel.setBorder(BorderFactory.createTitledBorder("饼图"));
        pieChartPanel.setPreferredSize(new Dimension(400, 400));

        //pieChartPanel.setBorder(BorderFactory.createTitledBorder("饼图"));

        // 清空并添加饼图面板到chartPanel
        chartPanel.removeAll();
        chartPanel.add(pieChartPanel);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    // 生成折线图
    private void generateLineChart() {
        // 获取数据
        var data = topPanel.getData();
        if (data.isEmpty()) return;
        // 创建折线图面板
        JPanel lineChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int width = getWidth() - 2 * LINE_PADDING;
                int height = getHeight() - 2 * LINE_PADDING;

                double maxData = Math.max(data.getFirst(), data.stream().mapToDouble(Double::doubleValue).max().orElse(1));
                double minData = Math.min(data.getFirst(), data.stream().mapToDouble(Double::doubleValue).min().orElse(1));
                double dataDiff = maxData - minData;

                int pointRadius = 5;

                Path2D path = new Path2D.Double();
                for (int i = 0; i < data.size(); i++) {
                    int x = (i * width) / (data.size() - 1) + LINE_PADDING;
                    int y = height - (int) (((data.get(i) - minData) / dataDiff) * height) + LINE_PADDING;
                    // 坐标 y 的计算应该为 该数据在 最大值-最小值 中所占的比例 再乘以面板宽度
                    g2d.fillOval(x - pointRadius, y - pointRadius, pointRadius * 2, pointRadius * 2); // 绘制点
                    if (i > 0) {
                        path.lineTo(x, y);
                    } else {
                        path.moveTo(x, y);
                    }
                }
                g2d.setColor(Color.BLACK);
                g2d.draw(path);
            }
        };
        lineChartPanel.setBackground(Color.white);
        lineChartPanel.setPreferredSize(new Dimension(400, 300));
        lineChartPanel.setBorder(BorderFactory.createTitledBorder("折线图"));

        // 清空并添加折线图面板到chartPanel
        chartPanel.removeAll();
        chartPanel.add(lineChartPanel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    // 获取随机颜色
    private Color getRandomColor() {
        return new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }

    public void onDataPanelVisible(boolean isVisible) {
        showDataButton.setBackground(isVisible ? new Color(0, 103, 192) : new Color(238, 238, 238));
    }
}
