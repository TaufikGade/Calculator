package org.calculator.gui.drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

public class LineChartPanel extends ChartPanel {
    private final int LINE_PADDING = 15;
    public LineChartPanel(List<Double> data, MainPanel top) {
        super(data, top);

        setPreferredSize(new Dimension(400, 300));
        setBorder(BorderFactory.createTitledBorder("折线图"));
    }

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
}
