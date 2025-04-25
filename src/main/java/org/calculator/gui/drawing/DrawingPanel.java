package org.calculator.gui.drawing;

import org.calculator.gui.ThemeColors;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DrawingPanel extends JLayeredPane {

    private MainPanel mainPanel;
    private final DataPanel dataPanel;
    private final Font font = new Font("Microsoft YaHei", Font.PLAIN, 25);

    public DrawingPanel() {

        dataPanel = new DataPanel(this);
        init();
    }

    public DrawingPanel(List<Double> data) {
        dataPanel = new DataPanel(this, data);
        init();
    }

    private void init() {
        mainPanel = new MainPanel(this);
        dataPanel.setVisible(false);
        add(mainPanel, JLayeredPane.DEFAULT_LAYER);
        add(dataPanel, JLayeredPane.PALETTE_LAYER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.setBounds(0, 0, getWidth(), getHeight());
                dataPanel.setBounds(0, (int) (getHeight() * 0.15f), getWidth(), (int) (getHeight() * 0.85f));
            }
        });
    }

    public void switchDataPanelState() {

        dataPanel.setVisible(!dataPanel.isVisible());
    }

    public List<Double> getData() {
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