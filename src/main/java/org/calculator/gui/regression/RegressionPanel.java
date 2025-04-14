package org.calculator.gui.regression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class RegressionPanel extends JLayeredPane {
    private final DataPanel dataPanel;
    private final JPanel chartPanel;
    private final Font font = new Font("Microsoft YaHei", Font.PLAIN, 25);

    public RegressionPanel() {
        //initUI();
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

    public JButton initButton(String buttonTitle, Color color) {
        JButton button = new JButton(buttonTitle);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBackground(color);
        button.setBorder(BorderFactory.createLineBorder(Color.white));
        return button;
    }
}