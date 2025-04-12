package org.calculator.gui.regression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ChartPanel extends JPanel {
    private final RegressionPanel topPanel;
    private final JButton dataButton;
    private List<Integer> dataPointX;
    private List<Integer> dataPointY;
    private List<TwoPoint> dataPoints;
    private int hoveredIndex = -1;
    private final int POINT_RADIUS = 3;

    public ChartPanel(RegressionPanel top) {
        this.topPanel = top;
        this.setLayout(null);
        this.setPreferredSize(new Dimension(400, 400)); // 设置图表区域大小
        this.setBorder(BorderFactory.createTitledBorder("Chart"));


        dataButton = topPanel.initButton("数据区域", Color.white);

        dataButton.addActionListener(_ -> topPanel.switchDataPanelState(true));

        this.add(dataButton);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateHoveredIndex(e.getX(), e.getY());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredIndex == -1) return;
                hoveredIndex = -1;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dataButton.setBounds(this.getWidth() - 150, this.getHeight() - 60, 150, 60);
        dataPointX = new ArrayList<>();
        dataPointY = new ArrayList<>();
        drawChart(g);
    }

    private void drawChart(Graphics g) {
        dataPoints = topPanel.getDataPoints();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 绘制坐标轴
        g2d.setColor(Color.BLACK);
        g2d.drawLine(50, this.getHeight() - 50, this.getWidth() - 50, this.getHeight() - 50); // X轴
        g2d.drawLine(50, 50, 50, this.getHeight() - 50); // Y轴

        // 绘制数据点
        g2d.setColor(Color.BLUE);
        for (TwoPoint p : dataPoints) {
            int x = (int) ((p.getX() / getMaxX(dataPoints)) * (this.getWidth() - 100)) + 50;
            int y = this.getHeight() - 50 - (int) ((p.getY() / getMaxY(dataPoints)) * (this.getHeight() - 100));
            dataPointX.add(x);
            dataPointY.add(y);
            g2d.fillOval(x - POINT_RADIUS, y - POINT_RADIUS, POINT_RADIUS * 2, POINT_RADIUS * 2);
        }

        // 绘制拟合直线
        if (dataPoints.size() >= 2) {
            double[] coefficients = calculateLeastSquares(dataPoints);
            double slope = coefficients[0];
            double intercept = coefficients[1];

            int startX = 50;
            int endX = this.getWidth() - 50;
            int startY = this.getHeight() - 50 - (int) ((slope * 0 + intercept) / getMaxY(dataPoints) * (this.getHeight() - 100));
            int endY = this.getHeight() - 50 - (int) ((slope * getMaxX(dataPoints) + intercept) / getMaxY(dataPoints) * (this.getHeight() - 100));

            g2d.setColor(Color.RED);

            g2d.drawLine(startX, startY, endX, endY);
        }

        if (hoveredIndex != -1) {
            drawTooltip(g2d, hoveredIndex);
        }
    }

    private double getMaxX(List<TwoPoint> dataPoints) {
        return dataPoints.stream().mapToDouble(TwoPoint::getX).max().orElse(1);
    }

    private double getMaxY(List<TwoPoint> dataPoints) {
        return dataPoints.stream().mapToDouble(TwoPoint::getY).max().orElse(1);
    }

    private double[] calculateLeastSquares(List<TwoPoint> points) {
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        int n = points.size();

        for (TwoPoint p : points) {
            sumX += p.getX();
            sumY += p.getY();
            sumXY += p.getX() * p.getY();
            sumX2 += p.getX() * p.getX();
        }

        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;

        return new double[]{slope, intercept};
    }

    private void updateHoveredIndex(int x, int y) {
        int preHovered = hoveredIndex;
        hoveredIndex = -1;

        for (int i = 0; i < dataPointX.size(); i++) {
            int sx = dataPointX.get(i);
            int sy = dataPointY.get(i);

            // 检测鼠标是否在点附近
            double distance = Math.sqrt(Math.pow(x - sx, 2) + Math.pow(y - sy, 2));

            if (distance <= POINT_RADIUS * 2) {
                hoveredIndex = i;
                break;
            }
        }

        if (preHovered != hoveredIndex) {
            repaint();
        }
    }

    private void drawTooltip(Graphics g, int index) {
        String text = String.format("(%.1f, %.1f)",
                dataPoints.get(index).getX(), dataPoints.get(index).getY());

        int screenX = dataPointX.get(index);
        int screenY = dataPointY.get(index);

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        // 计算提示框位置
        int tooltipX = screenX + 15;
        int tooltipY = screenY - textHeight - 10;

        // 边界检查
        if (tooltipX + textWidth + 10 > getWidth()) {
            tooltipX = screenX - textWidth - 25;
        }
        if (tooltipY < 20) {
            tooltipY = screenY + 20;
        }

        // 绘制背景
        g.setColor(new Color(255, 255, 200));
        g.fillRect(tooltipX, tooltipY, textWidth + 10, textHeight + 4);

        // 绘制边框
        g.setColor(Color.DARK_GRAY);
        g.drawRect(tooltipX, tooltipY, textWidth + 10, textHeight + 4);

        // 绘制文字
        g.setColor(Color.BLACK);
        g.drawString(text, tooltipX + 5, tooltipY + textHeight - 2);
    }
}
