package org.calculator.gui.drawing;

import org.calculator.gui.ThemeColors;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BarChartPanel extends ChartPanel {
    private final int BAR_PADDING = 25;
    private List<Integer> barLowerBounds;
    private List<Integer> barUpperBounds;
    private final List<Color> barColors;

    public BarChartPanel(List<Double> data, MainPanel top) {
        super(data, top);

        setPreferredSize(new Dimension(400, 300));

        barColors = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (this.data == null) return;

        super.paintComponent(g);
        setBackground(topPanel.getBackground());
        barLowerBounds = new ArrayList<>(data.size());
        barUpperBounds = new ArrayList<>(data.size());

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
                if (i == barColors.size()) {
                    Color color = ThemeColors.getRandomColor();
                    barColors.add(i, color);
                }
                g.setColor(hoveredIndex == i ? barColors.get(i).brighter() : barColors.get(i));
                if (num > 0) {
                    int barHeight = (int) (num / dataDiff * height) - BAR_PADDING;
                    g.fillRect(i * barWidth + BAR_PADDING, y - barHeight, barWidth, barHeight);
                    g.setColor(ThemeColors.getDataColor());
                    g.drawString("Data " + (i + 1), i * barWidth + 30, y - 10);
                    barLowerBounds.add(y - barHeight);
                    barUpperBounds.add(y);
                } else {
                    int barHeight = (int) (-num / dataDiff * height) + BAR_PADDING;
                    g.fillRect(i * barWidth + BAR_PADDING, y, barWidth, barHeight);
                    g.setColor(ThemeColors.getDataColor());
                    g.drawString("Data " + (i + 1), i * barWidth + 30, y + 15);
                    barLowerBounds.add(y);
                    barUpperBounds.add(y + barHeight);
                }
            }

            // 绘制 Y 方向的单位分割线
            int numTicks = 5; // Y 轴的分割线数量
            for (int i = 0; i <= numTicks; i++) {
                int ty = height - (i * height / numTicks) + BAR_PADDING;
                g.setColor(ThemeColors.getSplitColor());
                g.drawLine(BAR_PADDING, ty, width + BAR_PADDING, ty); // 绘制分割线
                g.setColor(ThemeColors.getDataColor());
                g.drawString(String.format("%.2f", i * dataDiff / numTicks + minData), 10, ty); // 标注数值
                g.setFont(new Font("Arial", Font.PLAIN, 12));
            }

        } else {
            for (int i = 0; i < data.size(); i++) {
                int barHeight = (int) (((data.get(i)) / maxData) * height);
                if (i == barColors.size()) {
                    Color color = ThemeColors.getRandomColor();
                    barColors.add(i, color);
                }
                g.setColor(hoveredIndex == i ? barColors.get(i).brighter() : barColors.get(i));
                g.fillRect(i * barWidth + BAR_PADDING, height - barHeight + BAR_PADDING, barWidth, barHeight);
                g.setColor(ThemeColors.getDataColor());
                g.drawString("Data " + (i + 1), i * barWidth + 30, height + 10);
                barLowerBounds.add(height - barHeight + BAR_PADDING);
                barUpperBounds.add(height + BAR_PADDING);
            }

            // 绘制 Y 方向的单位分割线
            int numTicks = 5; // Y 轴的分割线数量
            for (int i = 0; i <= numTicks; i++) {
                int y = height - (i * height / numTicks) + BAR_PADDING;
                g.setColor(ThemeColors.getSplitColor());
                g.drawLine(BAR_PADDING, y, width + BAR_PADDING, y); // 绘制分割线
                g.setColor(ThemeColors.getDataColor());
                g.drawString(String.valueOf(Math.round(i * maxData / numTicks)), 10, y); // 标注数值
                g.setFont(new Font("Arial", Font.PLAIN, 12));
            }
        }

        if (hoveredIndex != -1) {
            drawTooltip(g, hoveredIndex);
        }
    }

    @Override
    protected void updateHoveredIndex(int x, int y) {
        int preHovered = hoveredIndex;
        hoveredIndex = -1;

        int numBars = data.size();
        if (numBars == 0) return;

        int width = getWidth() - 2 * BAR_PADDING;
        int barWidth = width / numBars;

        for (int i = 0; i < numBars; i++) {
            int barX = i * barWidth + BAR_PADDING;

            if (x > barX && x < barX + barWidth && y > barLowerBounds.get(i) && y < barUpperBounds.get(i)) {
                hoveredIndex = i;
                break;
            }
        }

        if (hoveredIndex != preHovered) {
            repaint();
        }
    }

    @Override
    protected void drawTooltip(Graphics g, int index) {
        Double bar = data.get(index);
        String text = String.format("数据%02d：", index + 1) + bar;

        int barWidth = (getWidth() - (data.size() + 1) * BAR_PADDING) / data.size();
        int x = BAR_PADDING + index * (barWidth + BAR_PADDING);
        int barHeight = barUpperBounds.get(index) - barLowerBounds.get(index);
        int y = getHeight() - BAR_PADDING - barHeight;

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        // 计算提示框位置
        int tooltipX = x + barWidth / 2 - textWidth / 2 - 5;
        int tooltipY = y - textHeight - 10;

        // 边界检查
        if (tooltipY < 20) {
            tooltipY = y + barHeight + 10;
        }
        if (tooltipX < 10) tooltipX = 10;
        if (tooltipX + textWidth + 10 > getWidth()) {
            tooltipX = getWidth() - textWidth - 10;
        }

        // 绘制背景
        g.setColor(ThemeColors.getTooltipBgColor());
        g.fillRect(tooltipX, tooltipY, textWidth + 20, textHeight + 6);

        // 绘制边框
        g.setColor(ThemeColors.getTooltipBorderColor());
        g.drawRect(tooltipX, tooltipY, textWidth + 20, textHeight + 6);

        // 绘制文字
        g.setColor(ThemeColors.getTextColor());
        g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        g.drawString(text, tooltipX + 5, tooltipY + textHeight - 2);
    }
}
