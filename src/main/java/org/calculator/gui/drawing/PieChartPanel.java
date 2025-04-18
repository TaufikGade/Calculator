package org.calculator.gui.drawing;

import org.calculator.gui.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class PieChartPanel extends ChartPanel {
    private final int PIE_PADDING = 15;
    private int centerX;
    private int centerY;
    private int radius;
    private double dataSum = 0;
    private List<Color> fragmentColor;

    public PieChartPanel(List<Double> data, MainPanel top) {
        super(data, top);

        //setBorder(BorderFactory.createTitledBorder("饼状图"));
        setPreferredSize(new Dimension(400, 400));

        fragmentColor = new ArrayList<>();

        JLabel tipLabel = new JLabel("注：负数不计入饼状图");
        tipLabel.setForeground(ThemeColors.getTextColor());
        add(tipLabel, -1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (this.data == null) return;

        super.paintComponent(g);
        setBackground(topPanel.getBackground());
        calculateLayout();

        dataSum = 0;
        for (double datum : data) {
            if (datum > 0) dataSum += datum;
        }
        double startAngle = 0;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < data.size(); i++) {
            double datum = data.get(i);
            if (fragmentColor.size() == i) {
                Color color = topPanel.getRandomColor();
                fragmentColor.add(color);
            }
            g2d.setColor(i == hoveredIndex ? fragmentColor.get(i).brighter() : fragmentColor.get(i));
            if (datum < 0) continue;
            // 使用Arc2D和Path2D确保平滑边缘
            double arcAngle = (datum / dataSum) * 360;
            Arc2D arc = new Arc2D.Double(centerX - radius / 2.0f, centerY - radius / 2.0f, radius, radius, startAngle, arcAngle, Arc2D.PIE);
            Path2D path = new Path2D.Double();
            path.append(arc, true);
            g2d.fill(path);
            startAngle += arcAngle;
        }

        if (hoveredIndex != -1) {
            drawTooltip(g2d, hoveredIndex);
        }
    }

    @Override
    protected void updateHoveredIndex(int x, int y) {
        int preHovered = hoveredIndex;
        hoveredIndex = -1;

        // 转换为相对中心坐标
        int dx = x - centerX;
        int dy = y - centerY;

        // 计算距离中心的距离
        double distance = Math.sqrt(dx * dx + dy * dy) * 2;
        if (distance > radius) {
            repaint();
            return;
        }

        // 计算角度（转换为0-360度）
        double angle = Math.toDegrees(Math.atan2(-dy, dx));
        angle = (angle + 360) % 360;

        // 遍历所有扇区检测匹配
        double startAngle = 0;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) < 0) continue;
            double sweepAngle = (data.get(i) / dataSum) * 360;
            double endAngle = startAngle + sweepAngle;

            if (angle >= startAngle && angle <= endAngle) {
                hoveredIndex = i;
                break;
            }
            startAngle = endAngle;
        }


        if (preHovered != hoveredIndex) {
            repaint();
        }
    }

    @Override
    protected void drawTooltip(Graphics g, int index) {

        String text = String.format("数据%02d：%f(%.1f%%)",
                (index + 1), data.get(index),
                (data.get(index) / dataSum) * 100);

        // 计算提示框位置
        Point mousePos = getMousePosition();
        if (mousePos == null) return;

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        // 位置修正（避免超出边界）
        int x = mousePos.x + 15;
        int y = mousePos.y - 20;

        if (x + textWidth + 10 > getWidth()) {
            x = mousePos.x - textWidth - 20;
        }
        if (y < 20) y = 20;

        // 绘制背景
        //g.setColor(new Color(255, 255, 200));
        g.setColor(ThemeColors.getTooltipBgColor());
        g.fillRect(x, y, textWidth + 10, textHeight + 4);

        // 绘制边框
        //g.setColor(Color.DARK_GRAY);
        g.setColor(ThemeColors.getTooltipBorderColor());
        g.drawRect(x, y, textWidth + 10, textHeight + 4);

        // 绘制文字
        //g.setColor(Color.BLACK);
        g.setColor(ThemeColors.getTextColor());
        g.drawString(text, x + 5, y + textHeight - 2);
    }

    private void calculateLayout() {
        int width = getWidth();
        int height = getHeight();
        radius = (Math.min(width, height)) * 3 / 5;

        centerX = width / 2;
        centerY = height / 2;
    }
}
