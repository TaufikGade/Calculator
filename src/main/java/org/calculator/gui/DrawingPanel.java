package org.calculator.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel {
    private List<JTextField> inputFields; // 存储输入框的列表
    private int inputCount = 3; // 当前输入框的数量
    private final int MAX_INPUTS = 10; // 最大输入框数量限制
    private final int MIN_INPUTS = 1; // 最小输入框数量限制
    private List<Double> data; // 存储输入的数据
    private JPanel chartPanel; // 用于显示图表的面板
    private JButton addInputButton; // 增加输入框按钮
    private JButton removeInputButton; // 减少输入框按钮
    private JButton showBarChartButton; // 显示柱状图按钮
    private JButton showPieChartButton; // 显示饼图按钮
    private JButton showLineChartButton; // 显示折线图按钮

    private final int LINE_PADDING = 15; // 折线图间距
    private final int BAR_PADDING = 25; // 柱状图间距

    public DrawingPanel() {
        setLayout(new BorderLayout()); // 设置布局为BorderLayout
        inputFields = new ArrayList<>();
        data = new ArrayList<>();

        // 创建输入部分的面板
        JPanel inputPanel = new JPanel();
        //inputPanel.setLayout(new BorderLayout());
        JPanel inputFieldPanel = new JPanel();
        inputFieldPanel.setLayout(new GridLayout(1, MAX_INPUTS + 1)); // 输入框面板使用GridLayout
        inputFieldPanel.add(new JLabel("数据："));

        // 初始化输入框
        for (int i = 0; i < inputCount; i++) {
            JTextField textField = new JTextField(10);
            inputFields.add(textField);
            inputFieldPanel.add(textField);
        }

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));
        addInputButton = new JButton("增加输入");
        addInputButton.setFocusPainted(false);
        removeInputButton = new JButton("减少输入");
        removeInputButton.setFocusPainted(false);
        showBarChartButton = new JButton("柱状图");
        showBarChartButton.setFocusPainted(false);
        showPieChartButton = new JButton("饼图");
        showPieChartButton.setFocusPainted(false);
        showLineChartButton = new JButton("折线图");
        showLineChartButton.setFocusPainted(false);


        buttonPanel.add(addInputButton);
        buttonPanel.add(removeInputButton);
        buttonPanel.add(showBarChartButton);
        buttonPanel.add(showPieChartButton);
        buttonPanel.add(showLineChartButton);

        // 添加按钮的监听器
        addInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputCount < MAX_INPUTS) {
                    inputCount++;
                    JTextField textField = new JTextField(10);
                    inputFields.add(textField);
                    inputFieldPanel.add(textField);
                    inputFieldPanel.revalidate();
                    inputFieldPanel.repaint();
                }
            }
        });

        removeInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputCount > MIN_INPUTS) {
                    inputCount--;
                    inputFieldPanel.remove(inputFields.get(inputFields.size() - 1));
                    inputFields.remove(inputFields.size() - 1);
                    inputFieldPanel.revalidate();
                    inputFieldPanel.repaint();
                }
            }
        });

        showBarChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateBarChart();
            }
        });

        showPieChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePieChart();
            }
        });

        showLineChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateLineChart();
            }
        });

        // 添加输入框面板和按钮面板到输入部分
        inputPanel.add(inputFieldPanel, BorderLayout.CENTER);
        //inputPanel.add(buttonPanel, BorderLayout.PAGE_END);

        // 创建图表面板
        chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());

        // 将输入部分和图表面板添加到DrawingPanel
        add(inputPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
    }
    /*
    // 生成柱状图
    private void generateBarChart() {
        // 清空数据
        data.clear();
        for (JTextField textField : inputFields) {
            try {
                data.add(Double.parseDouble(textField.getText()));
            } catch (NumberFormatException e) {
                data.add(0.0); // 如果输入无效，添加0
            }
        }

        // 创建柱状图面板
        JPanel barChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                int barWidth = width / data.size();
                int maxData = (int) Math.max(1, data.stream().mapToInt(Double::intValue).max().orElse(1));

                for (int i = 0; i < data.size(); i++) {
                    int barHeight = (int) ((data.get(i) / maxData) * height);
                    g.setColor(getRandomColor()); // 随机颜色
                    g.fillRect(i * barWidth, height - barHeight, barWidth, barHeight);
                    g.setColor(Color.BLACK);
                    g.drawString("Data " + (i + 1), i * barWidth, height);
                }
            }
        };
        barChartPanel.setPreferredSize(new Dimension(400, 300));
        barChartPanel.setBorder(BorderFactory.createTitledBorder("柱状图"));

        // 清空并添加柱状图面板到chartPanel
        chartPanel.removeAll();
        chartPanel.add(barChartPanel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }
    */

    private void generateBarChart() {
        // 清空数据
        data.clear();
        for (JTextField textField : inputFields) {
            try {
                data.add(Double.parseDouble(textField.getText()));
            } catch (NumberFormatException e) {
                data.add(0.0); // 如果输入无效，添加0
            }
        }

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
        // 清空数据
        data.clear();
        for (JTextField textField : inputFields) {
            try {
                data.add(Double.parseDouble(textField.getText()));
            } catch (NumberFormatException e) {
                data.add(0.0); // 如果输入无效，添加0
            }
        }

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
                for (int i = 0; i < data.size(); i++) {
                    g2d.setColor(getRandomColor());
                    // 使用Arc2D和Path2D确保平滑边缘
                    double arcAngle = (data.get(i) / total) * 360;
                    Arc2D arc = new Arc2D.Double(x, y, radius, radius, startAngle, arcAngle, Arc2D.PIE);
                    Path2D path = new Path2D.Double();
                    path.append(arc, true);
                    g2d.fill(path);
                    startAngle += arcAngle;
                }
            }
        };
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
        // 清空数据
        data.clear();
        for (JTextField textField : inputFields) {
            try {
                data.add(Double.parseDouble(textField.getText()));
            } catch (NumberFormatException e) {
                data.add(0.0); // 如果输入无效，添加0
            }
        }

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
}