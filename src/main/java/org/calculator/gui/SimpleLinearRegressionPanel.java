package org.calculator.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SimpleLinearRegressionPanel extends JPanel {
    private static final int MAX_DATA_POINTS = 100; // 最大数据点数
    private static final int INITIAL_DATA_POINTS = 5; // 初始数据点数

    private List<JTextField> xFields = new ArrayList<>();
    private List<JTextField> yFields = new ArrayList<>();
    private List<Point> dataPoints = new ArrayList<>();
    private JButton addButton, removeButton, generateButton;
    private JPanel dataPanel;
    private JPanel chartPanel;

    public SimpleLinearRegressionPanel() {
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // 初始化数据输入区域
        dataPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // 两列布局，用于输入x和y值
        dataPanel.setBorder(BorderFactory.createTitledBorder("Data Input"));

        // 初始化图表区域
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g);
            }
        };
        chartPanel.setPreferredSize(new Dimension(400, 400)); // 设置图表区域大小
        chartPanel.setBorder(BorderFactory.createTitledBorder("Chart"));

        // 初始化按钮
        addButton = new JButton("Add Data");
        removeButton = new JButton("Remove Data");
        generateButton = new JButton("Generate Fit Function");

        // 添加按钮监听器
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDataPoint();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeDataPoint();
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateFitFunction();
            }
        });

        // 添加初始数据输入框
        for (int i = 0; i < INITIAL_DATA_POINTS; i++) {
            addDataPoint();
        }

        // 添加组件到面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(generateButton);

        add(dataPanel, BorderLayout.WEST);
        add(chartPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addDataPoint() {
        if (xFields.size() < MAX_DATA_POINTS) {
            JTextField xField = new JTextField(5);
            JTextField yField = new JTextField(5);
            xFields.add(xField);
            yFields.add(yField);
            dataPanel.add(xField);
            dataPanel.add(yField);
            dataPanel.revalidate();
            dataPanel.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Max data points reached!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeDataPoint() {
        if (xFields.size() > 1) {
            dataPanel.remove(xFields.get(xFields.size() - 1));
            dataPanel.remove(yFields.get(yFields.size() - 1));
            xFields.remove(xFields.size() - 1);
            yFields.remove(yFields.size() - 1);
            dataPanel.revalidate();
            dataPanel.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Cannot remove more data points!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void generateFitFunction() {
        dataPoints.clear();
        for (int i = 0; i < xFields.size(); i++) {
            try {
                double x = Double.parseDouble(xFields.get(i).getText());
                double y = Double.parseDouble(yFields.get(i).getText());
                dataPoints.add(new Point(x, y));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input in row " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (dataPoints.size() < 2) {
            JOptionPane.showMessageDialog(this, "Need at least 2 data points!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        chartPanel.repaint();
    }

    private void drawChart(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, chartPanel.getWidth(), chartPanel.getHeight());

        // 绘制坐标轴
        g.setColor(Color.BLACK);
        g.drawLine(50, chartPanel.getHeight() - 50, chartPanel.getWidth() - 50, chartPanel.getHeight() - 50); // X轴
        g.drawLine(50, 50, 50, chartPanel.getHeight() - 50); // Y轴

        // 绘制数据点
        g.setColor(Color.BLUE);
        for (Point p : dataPoints) {
            int x = (int) ((p.x / getMaxX()) * (chartPanel.getWidth() - 100)) + 50;
            int y = chartPanel.getHeight() - 50 - (int) ((p.y / getMaxY()) * (chartPanel.getHeight() - 100));
            g.fillOval(x - 3, y - 3, 6, 6);
        }

        // 绘制拟合直线
        if (dataPoints.size() >= 2) {
            double[] coefficients = calculateLeastSquares(dataPoints);
            double slope = coefficients[0];
            double intercept = coefficients[1];

            int startX = 50;
            int endX = chartPanel.getWidth() - 50;
            int startY = chartPanel.getHeight() - 50 - (int) ((slope * 0 + intercept) / getMaxY() * (chartPanel.getHeight() - 100));
            int endY = chartPanel.getHeight() - 50 - (int) ((slope * getMaxX() + intercept) / getMaxY() * (chartPanel.getHeight() - 100));

            g.setColor(Color.RED);
            g.drawLine(startX, startY, endX, endY);
        }
    }

    private double getMaxX() {
        return dataPoints.stream().mapToDouble(p -> p.x).max().orElse(1);
    }

    private double getMaxY() {
        return dataPoints.stream().mapToDouble(p -> p.y).max().orElse(1);
    }

    private double[] calculateLeastSquares(List<Point> points) {
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        int n = points.size();

        for (Point p : points) {
            sumX += p.x;
            sumY += p.y;
            sumXY += p.x * p.y;
            sumX2 += p.x * p.x;
        }

        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;

        return new double[]{slope, intercept};
    }

    private static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}