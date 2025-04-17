package org.calculator.gui.drawing;

import org.calculator.gui.CalculatorGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DrawingPanel extends JLayeredPane {

    private final MainPanel mainPanel;
    private final DataPanel dataPanel;
    private final Font font = new Font("Microsoft YaHei", Font.PLAIN, 25);
    //region ColorDefinitions
    public final Color dayBgColor = Color.white; //EEEEEE
    public final Color darkBgColor = Color.black;
    public final Color dayContentColor = new Color(238, 238, 238); //FFFFFF
    public final Color darkContentColor = new Color(40, 40, 40);
    public final Color dayTextColor = Color.black;
    public final Color darkTextColor = Color.lightGray;
    public final Color dayHoverColor = new Color(234, 234, 234);
    public final Color darkHoverColor = new Color(45, 45, 45);
    public final Color dayClickColor = new Color(236, 237, 238);
    public final Color darkClickColor = new Color(29, 29, 42);
    //endregion

    public DrawingPanel() {
        mainPanel = new MainPanel(this);
        dataPanel = new DataPanel(this);
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
        button.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
        button.setForeground(CalculatorGUI.isDarkMode ? darkTextColor : dayTextColor);
        button.setBorder(BorderFactory.createLineBorder(CalculatorGUI.isDarkMode ? Color.lightGray : Color.black));
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
            button.setBackground(CalculatorGUI.isDarkMode ? darkClickColor : dayClickColor);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            button.setBackground(CalculatorGUI.isDarkMode ? darkHoverColor : dayHoverColor);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            button.setBackground(CalculatorGUI.isDarkMode ? darkHoverColor : dayHoverColor);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            button.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
        }
    }
}