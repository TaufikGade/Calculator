package org.calculator.gui;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {
    public DrawingPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("绘图工具区域", SwingConstants.CENTER), BorderLayout.CENTER);

        // 这里可以添加绘图组件和事件处理
        // 示例：添加一个占位画布
        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawString("绘图区域（待实现）", 150, 150);
            }
        };
        canvas.setPreferredSize(new Dimension(600, 500));
        add(canvas, BorderLayout.CENTER);
    }
}