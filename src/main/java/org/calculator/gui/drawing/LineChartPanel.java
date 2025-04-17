package org.calculator.gui.drawing;

import org.calculator.gui.CalculatorGUI;
import org.checkerframework.checker.units.qual.C;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class LineChartPanel extends ChartPanel {
    private final int LINE_PADDING = 30;
    private final int pointRadius = 5;

    private List<Integer> dataPointX;
    private List<Integer> dataPointY;

    private double yMin, yMax;
    private int chartWidth, chartHeight;
    private Color dayLineColor = new Color(70, 130, 255);
    private Color darkLineColor = new Color(70, 130, 255);
    private Color dayAxisColor = Color.BLACK;
    private Color darkAxisColor = Color.WHITE;
    private Color daySplitColor = Color.DARK_GRAY;
    private Color darkSplitColor = Color.DARK_GRAY;
    private Color dayDataColor = Color.BLACK;
    private Color darkDataColor = Color.WHITE;


    public LineChartPanel(List<Double> data, MainPanel top) {
        super(data, top);

        setPreferredSize(new Dimension(400, 300));
        setBorder(BorderFactory.createTitledBorder("折线图"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        dataPointX = new ArrayList<>();
        dataPointY = new ArrayList<>();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        chartWidth = getWidth() - 2 * LINE_PADDING;
        chartHeight = getHeight() - 2 * LINE_PADDING;

        yMax = Math.max(data.getFirst(), data.stream().mapToDouble(Double::doubleValue).max().orElse(1));
        yMin = Math.min(data.getFirst(), data.stream().mapToDouble(Double::doubleValue).min().orElse(1));
        double dataDiff = yMax - yMin;

        Path2D path = new Path2D.Double();
        for (int i = 0; i < data.size(); i++) {
            int x = (i * chartWidth) / (data.size() - 1) + LINE_PADDING;
            int y = chartHeight - (int) (((data.get(i) - yMin) / dataDiff) * chartHeight) + LINE_PADDING;
            dataPointX.add(x);
            dataPointY.add(y);
            // 坐标 y 的计算应该为 该数据在 最大值-最小值 中所占的比例 再乘以面板宽度
            g2d.fillOval(x - pointRadius, y - pointRadius, pointRadius * 2, pointRadius * 2); // 绘制点
            if (i > 0) {
                path.lineTo(x, y);
            } else {
                path.moveTo(x, y);
            }
        }
        //g2d.setColor(new Color(70, 130, 255));
        g2d.setColor(CalculatorGUI.isDarkMode ? darkLineColor : dayLineColor);
        g2d.draw(path);

        drawAxes(g2d);

        if (hoveredIndex != -1) {
            drawTooltip(g2d, hoveredIndex);
        }
    }

    @Override
    protected void updateHoveredIndex(int x, int y) {
        int preHovered = hoveredIndex;
        hoveredIndex = -1;

        for (int i = 0; i < data.size(); i++) {
            int sx = dataPointX.get(i);
            int sy = dataPointY.get(i);

            // 检测鼠标是否在点附近
            double distance = Math.sqrt(Math.pow(x - sx, 2) + Math.pow(y - sy, 2));

            if (distance <= pointRadius * 2) {
                hoveredIndex = i;
                break;
            }
        }

        if (preHovered != hoveredIndex) {
            repaint();
        }
    }

    @Override
    protected void drawTooltip(Graphics g, int index) {
        String text = String.format("(%d, %.1f)",
                index, data.get(index));

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
        //g.setColor(new Color(255, 255, 200));
        g.setColor(CalculatorGUI.isDarkMode ? darkTooltipBgColor : dayTooltipBgColor);
        g.fillRect(tooltipX, tooltipY, textWidth + 10, textHeight + 4);

        // 绘制边框
        //g.setColor(Color.DARK_GRAY);
        g.setColor(CalculatorGUI.isDarkMode ? darkTooltipBorderColor : dayTooltipBorderColor);
        g.drawRect(tooltipX, tooltipY, textWidth + 10, textHeight + 4);

        // 绘制文字
        //g.setColor(Color.BLACK);
        g.setColor(CalculatorGUI.isDarkMode ? darkTooltipTextColor : dayTooltipTextColor);
        g.drawString(text, tooltipX + 5, tooltipY + textHeight - 2);
    }

    private void drawAxes(Graphics g) {
        //g.setColor(Color.BLACK);
        g.setColor(CalculatorGUI.isDarkMode ? darkAxisColor : dayAxisColor);

        // 绘制坐标轴线
        g.drawLine(LINE_PADDING, getHeight() - LINE_PADDING,
                LINE_PADDING + chartWidth, getHeight() - LINE_PADDING); // X轴
        g.drawLine(LINE_PADDING, LINE_PADDING,
                LINE_PADDING, getHeight() - LINE_PADDING); // Y轴

        int numTicks = 5;

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        for (int i = 0; i <= numTicks; i++) {
            int ty = chartHeight - (i * chartHeight / 5) + LINE_PADDING;
            //g.setColor(Color.DARK_GRAY);
            g.setColor(CalculatorGUI.isDarkMode ? darkSplitColor : daySplitColor);
            g.drawLine(LINE_PADDING, ty, chartWidth + LINE_PADDING, ty); // 绘制分割线
            //g.setColor(Color.BLACK);
            g.setColor(CalculatorGUI.isDarkMode ? darkDataColor : dayDataColor);
            g.drawString(String.format("%.2f", i * (yMax - yMin) / numTicks + yMin), 10, ty); // 标注数值

        }
    }
}
