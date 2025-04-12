package org.calculator.gui.regression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class RegressionPanel extends JLayeredPane {
    private final DataPanel dataPanel;
    //private JButton addButton, removeButton, generateButton;
    private final JPanel chartPanel;
    private final Font font = new Font("Microsoft YaHei", Font.PLAIN, 25);

    public RegressionPanel() {
        //initUI();
        // 初始化图表区域
        dataPanel = new DataPanel(this); // 两列布局，用于输入x和y值
        chartPanel = new ChartPanel(this);

        dataPanel.setVisible(false);

        add(chartPanel, JLayeredPane.DEFAULT_LAYER);
        add(dataPanel, JLayeredPane.PALETTE_LAYER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                chartPanel.setBounds(0, 0, getWidth(), getHeight());
                chartPanel.repaint();
                dataPanel.setBounds(0, (int) (getHeight() * 0.15f), getWidth(), (int) (getHeight() * 0.85f));
                dataPanel.repaint();
            }
        });

    }

    public void switchDataPanelState(boolean isShow) {
        if (!isShow) {
            generateFitFunction();
        }
        dataPanel.setVisible(isShow);
    }

    private void initUI() {
        // 初始化数据输入区域

        //dataPanel.setBorder(BorderFactory.createTitledBorder("Data Input"));


        // 初始化按钮
//        addButton = new JButton("Add Data");
//        removeButton = new JButton("Remove Data");
//        generateButton = new JButton("Generate Fit Function");

        // 添加按钮监听器
//        addButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addDataPoint();
//            }
//        });
//
//        removeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                removeDataPoint();
//            }
//        });
//
//        generateButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                generateFitFunction();
//            }
//        });

        // 添加初始数据输入框
//        for (int i = 0; i < INITIAL_DATA_POINTS; i++) {
//            addDataPoint();
//        }

        // 添加组件到面板
//        JPanel buttonPanel = new JPanel(new FlowLayout());
//        buttonPanel.add(addButton);
//        buttonPanel.add(removeButton);
//        buttonPanel.add(generateButton);

//        add(dataPanel, BorderLayout.WEST);
//        add(chartPanel, BorderLayout.CENTER);
        //add(buttonPanel, BorderLayout.SOUTH);
    }

//    private void addDataPoint() {
//        if (xFields.size() < MAX_DATA_POINTS) {
//            JTextField xField = new JTextField(5);
//            JTextField yField = new JTextField(5);
//            xFields.add(xField);
//            yFields.add(yField);
//            dataPanel.add(xField);
//            dataPanel.add(yField);
//            dataPanel.revalidate();
//            dataPanel.repaint();
//        } else {
//            JOptionPane.showMessageDialog(this, "Max data points reached!", "Warning", JOptionPane.WARNING_MESSAGE);
//        }
//    }

//    private void removeDataPoint() {
//        if (xFields.size() > 1) {
//            dataPanel.remove(xFields.get(xFields.size() - 1));
//            dataPanel.remove(yFields.get(yFields.size() - 1));
//            xFields.remove(xFields.size() - 1);
//            yFields.remove(yFields.size() - 1);
//            dataPanel.revalidate();
//            dataPanel.repaint();
//        } else {
//            JOptionPane.showMessageDialog(this, "Cannot remove more data points!", "Warning", JOptionPane.WARNING_MESSAGE);
//        }
//    }

    public void generateFitFunction() {
        List<TwoPoint> dataPoints = dataPanel.getData();
//        for (int i = 0; i < xFields.size(); i++) {
//            try {
//                double x = Double.parseDouble(xFields.get(i).getText());
//                double y = Double.parseDouble(yFields.get(i).getText());
//                dataPoints.add(new TwoPoint(x, y));
//            } catch (NumberFormatException e) {
//                JOptionPane.showMessageDialog(this, "Invalid input in row " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//        }
        if (dataPoints.size() < 2) {
            JOptionPane.showMessageDialog(this, "Need at least 2 data points!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        chartPanel.repaint();
    }

//    private void drawChart(Graphics g) {
//        g.setColor(Color.WHITE);
//        g.fillRect(0, 0, chartPanel.getWidth(), chartPanel.getHeight());
//
//        // 绘制坐标轴
//        g.setColor(Color.BLACK);
//        g.drawLine(50, chartPanel.getHeight() - 50, chartPanel.getWidth() - 50, chartPanel.getHeight() - 50); // X轴
//        g.drawLine(50, 50, 50, chartPanel.getHeight() - 50); // Y轴
//
//        // 绘制数据点
//        g.setColor(Color.BLUE);
//        for (TwoPoint p : dataPoints) {
//            int x = (int) ((p.getX() / getMaxX()) * (chartPanel.getWidth() - 100)) + 50;
//            int y = chartPanel.getHeight() - 50 - (int) ((p.getY() / getMaxY()) * (chartPanel.getHeight() - 100));
//            g.fillOval(x - 3, y - 3, 6, 6);
//        }
//
//        // 绘制拟合直线
//        if (dataPoints.size() >= 2) {
//            double[] coefficients = calculateLeastSquares(dataPoints);
//            double slope = coefficients[0];
//            double intercept = coefficients[1];
//
//            int startX = 50;
//            int endX = chartPanel.getWidth() - 50;
//            int startY = chartPanel.getHeight() - 50 - (int) ((slope * 0 + intercept) / getMaxY() * (chartPanel.getHeight() - 100));
//            int endY = chartPanel.getHeight() - 50 - (int) ((slope * getMaxX() + intercept) / getMaxY() * (chartPanel.getHeight() - 100));
//
//            g.setColor(Color.RED);
//            g.drawLine(startX, startY, endX, endY);
//        }
//    }

//    private double getMaxX() {
//        return dataPoints.stream().mapToDouble(TwoPoint::getX).max().orElse(1);
//    }
//
//    private double getMaxY() {
//        return dataPoints.stream().mapToDouble(TwoPoint::getY).max().orElse(1);
//    }

//    private double[] calculateLeastSquares(List<TwoPoint> points) {
//        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
//        int n = points.size();
//
//        for (TwoPoint p : points) {
//            sumX += p.getX();
//            sumY += p.getY();
//            sumXY += p.getX() * p.getY();
//            sumX2 += p.getX() * p.getX();
//        }
//
//        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
//        double intercept = (sumY - slope * sumX) / n;
//
//        return new double[]{slope, intercept};
//    }

    public List<TwoPoint> getDataPoints() {
        return dataPanel.getData();
    }

    public JButton initButton(String buttonTitle) {
        JButton button = new JButton(buttonTitle);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBackground(new Color(238, 238, 238));
        button.setBorder(BorderFactory.createLineBorder(Color.white));
        return button;
    }

    public JButton initButton(String buttonTitle, Color color) {
        JButton button = new JButton(buttonTitle);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBackground(color);
        button.setBorder(BorderFactory.createLineBorder(Color.white));
        return button;
    }
}