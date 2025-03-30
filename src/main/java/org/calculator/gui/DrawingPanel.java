package org.calculator.gui;

import javax.swing.*;
import java.awt.*;
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

    public DrawingPanel() {
        setLayout(new BorderLayout()); // 设置布局为BorderLayout
        inputFields = new ArrayList<>();
        data = new ArrayList<>();

        // 创建输入部分的面板
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        JPanel inputFieldPanel = new JPanel();
        inputFieldPanel.setLayout(new GridLayout(1, inputCount)); // 输入框面板使用GridLayout

        // 初始化输入框
        for (int i = 0; i < inputCount; i++) {
            JTextField textField = new JTextField(10);
            inputFields.add(textField);
            inputFieldPanel.add(textField);
        }

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        addInputButton = new JButton("增加输入");
        removeInputButton = new JButton("减少输入");
        showBarChartButton = new JButton("显示柱状图");
        showPieChartButton = new JButton("显示饼图");
        showLineChartButton = new JButton("显示折线图");

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
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // 创建图表面板
        chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());

        // 将输入部分和图表面板添加到DrawingPanel
        add(inputPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
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
                int width = getWidth();
                int height = getHeight();
                //int width = 360;
                //int height = 360;
                int barWidth = width / data.size();
                int maxData = (int) Math.max(1, data.stream().mapToInt(Double::intValue).max().orElse(1));

                // 绘制 Y 方向的单位分割线
                int numTicks = 5; // Y 轴的分割线数量
                for (int i = 0; i <= numTicks; i++) {
                    int y = height - (i * height / numTicks);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawLine(0, y, width, y); // 绘制分割线
                    g.setColor(Color.BLACK);
                    g.drawString(String.valueOf((i * maxData) / numTicks), 0, y); // 标注数值
                }

                // 绘制柱状图
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
                //int width = 360;
                //int height = 360;
                int total = data.stream().mapToInt(Double::intValue).sum();
                int startAngle = 0;

                for (int i = 0; i < data.size(); i++) {
                    int arcAngle = (int) ((data.get(i) / total) * 360);
                    g.setColor(getRandomColor()); // 随机颜色
                    g.fillArc(0, 0, width, height, startAngle, arcAngle);
                    startAngle += arcAngle;
                }
            }
        };
        pieChartPanel.setPreferredSize(new Dimension(400, 400));
        pieChartPanel.setBorder(BorderFactory.createTitledBorder("饼图"));

        // 清空并添加饼图面板到chartPanel
        chartPanel.removeAll();
        chartPanel.add(pieChartPanel, BorderLayout.CENTER);
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
                int width = getWidth();
                int height = getHeight();
                int maxData = (int) Math.max(1, data.stream().mapToInt(Double::intValue).max().orElse(1));
                int pointRadius = 5;

                for (int i = 0; i < data.size(); i++) {
                    int x = (i * width) / (data.size() - 1);
                    int y = height - (int) ((data.get(i) / maxData) * height);

                    g.setColor(Color.BLACK);
                    g.fillOval(x - pointRadius, y - pointRadius, pointRadius * 2, pointRadius * 2); // 绘制点
                    if (i > 0) {
                        int prevX = ((i - 1) * width) / (data.size() - 1);
                        int prevY = height - (int) ((data.get(i - 1) / maxData) * height);
                        g.drawLine(prevX, prevY, x, y); // 绘制连线
                    }
                }
            }
        };
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