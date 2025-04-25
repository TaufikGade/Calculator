package org.calculator.gui.drawing;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ChartPanel extends JPanel {
    protected List<Double> data; // 存储输入的数据
    protected MainPanel topPanel;
    protected int hoveredIndex = -1;

    public ChartPanel(List<Double> data, MainPanel top) {
        this.data = data;
        this.topPanel = top;

        setBackground(Color.white);

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

    public void updateData(List<Double> data) {
        this.data = data;
        repaint();
    }

    protected void updateHoveredIndex(int x, int y) {

    }

    protected void drawTooltip(Graphics g, int index) {

    }
}
