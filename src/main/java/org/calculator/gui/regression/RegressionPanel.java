package org.calculator.gui.regression;

import org.calculator.gui.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class RegressionPanel extends JLayeredPane {
    private final DataPanel dataPanel;
    private final JPanel chartPanel;
    private final Font font = new Font("Microsoft YaHei", Font.PLAIN, 25);

    public RegressionPanel() {
        setBackground(ThemeColors.getDarkBgColor());
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

    public void generateFitFunction() {
        List<TwoPoint> dataPoints = dataPanel.getData();

        if (dataPoints.size() < 2) {
            JOptionPane.showMessageDialog(this, "Need at least 2 data points!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        chartPanel.repaint();
    }

    public List<TwoPoint> getDataPoints() {
        return dataPanel.getData();
    }

    public JButton initButton(String buttonTitle) {
        JButton button = new JButton(buttonTitle);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBackground(ThemeColors.getTotalBgColor());
        button.setForeground(ThemeColors.getTextColor());
        button.setBorder(BorderFactory.createLineBorder(ThemeColors.getTextColor()));
        button.addMouseListener(new ButtonMouseHandler(button));
        return button;
    }

    private class ButtonMouseHandler extends MouseAdapter {
        private final JButton button;

        public ButtonMouseHandler(JButton button) {
            this.button = button;
            button.setContentAreaFilled(false);  // 取消默认背景填充（包括点击变色）
            button.setBorderPainted(false);      // 取消默认边框绘制
            button.setOpaque(true);              // 允许自定义背景色（必须设为不透明）
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getFunctionClickColor());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            button.setBackground(ThemeColors.getFunctionHoverColor());
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            button.setBackground(ThemeColors.getFunctionHoverColor());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            button.setBackground(ThemeColors.getTotalBgColor());
        }
    }
}