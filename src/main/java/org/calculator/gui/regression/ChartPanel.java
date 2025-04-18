package org.calculator.gui.regression;

import org.calculator.gui.CalculatorGUI;
import org.calculator.gui.ThemeColors;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChartPanel extends JPanel {
    private final RegressionPanel topPanel;
    private JButton dataButton;
    private List<Integer> dataPointX;
    private List<Integer> dataPointY;
    private List<TwoPoint> dataPoints;
    private int hoveredIndex = -1;
    private final int POINT_RADIUS = 3;

    public ChartPanel(RegressionPanel top) {
        this.topPanel = top;
        this.setLayout(null);
        this.setPreferredSize(new Dimension(400, 400)); // 设置图表区域大小
        this.setBackground(topPanel.getBackground());
        //this.setBorder(BorderFactory.createTitledBorder("Chart"));

        try {
            // 从类路径加载图片
            InputStream imgStream = ChartPanel.class.getResourceAsStream("/images/123pic.png");
            if (imgStream == null) {
                throw new IOException("图片资源未找到！");
            }
            BufferedImage originalImage = ImageIO.read(imgStream);

            // 调整大小并创建图标
            Image scaledImage = originalImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);

            dataButton = new JButton(icon) {
                // 定义按钮形状为圆形
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // 绘制圆形背景
                    if (getModel().isArmed()) {
                        g2.setColor(Color.darkGray); // 按下时颜色
                    } else {
                        g2.setColor(Color.lightGray);
                    }
                    int radius = Math.min(getWidth(), getHeight());
                    g2.fillOval(0, 0, radius, radius);

                    // 绘制文字
                    super.paintComponent(g2);
                    g2.dispose();
                }

                // 设置按钮形状边界为圆形
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(100, 100); // 按钮大小
                }

                @Override
                public boolean contains(int x, int y) {
                    return new Ellipse2D.Float(0, 0, getWidth(), getHeight()).contains(x, y);
                }
            };
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }


        // 设置按钮属性
        dataButton.setOpaque(false);
        dataButton.setContentAreaFilled(false);
        dataButton.setBorderPainted(false);
        dataButton.setFocusPainted(false);
        dataButton.setBackground(new Color(0, 150, 255)); // 默认背景色
        dataButton.setForeground(Color.WHITE); // 文字颜色

        // 添加悬停效果
        dataButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                dataButton.setBackground(ThemeColors.getFunctionHoverColor()); // 悬停颜色
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dataButton.setBackground(ThemeColors.getDarkBgColor()); // 恢复默认颜色
            }
        });

        dataButton.addActionListener(_ -> topPanel.switchDataPanelState(true));

        this.add(dataButton, BorderLayout.PAGE_END);

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
        dataButton.setBounds(this.getWidth() - 60, this.getHeight() - 120, 40, 40);
        dataPointX = new ArrayList<>();
        dataPointY = new ArrayList<>();
        drawChart(g);
    }

    private void drawChart(Graphics g) {
        dataPoints = topPanel.getDataPoints();
        g.setColor(ThemeColors.getDarkBgColor());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 绘制坐标轴
        g2d.setColor(ThemeColors.getAxisColor());
        g2d.drawLine(50, this.getHeight() - 50, this.getWidth() - 50, this.getHeight() - 50); // X轴
        g2d.drawLine(50, 50, 50, this.getHeight() - 50); // Y轴

        // 绘制数据点
        g2d.setColor(ThemeColors.getPointColor());
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

            g2d.setColor(ThemeColors.getLineColor());

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
        g.setColor(ThemeColors.getTooltipBgColor());
        g.fillRect(tooltipX, tooltipY, textWidth + 10, textHeight + 4);

        // 绘制边框
        g.setColor(ThemeColors.getTooltipBorderColor());
        g.drawRect(tooltipX, tooltipY, textWidth + 10, textHeight + 4);

        // 绘制文字
        g.setColor(ThemeColors.getTextColor());
        g.drawString(text, tooltipX + 5, tooltipY + textHeight - 2);
    }
}
