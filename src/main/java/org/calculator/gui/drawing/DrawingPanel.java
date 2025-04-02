package org.calculator.gui.drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.List;

public class DrawingPanel extends JLayeredPane {

    private final MainPanel mainPanel;
    private final DataPanel dataPanel;
    private final Font font = new Font("Microsoft YaHei", Font.PLAIN, 25);

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
        mainPanel.onDataPanelVisible(dataPanel.isVisible());
    }

    public List<Double> getData() {
        return dataPanel.getData();
    }

    public JButton initButton(String buttonTitle) {
        JButton button = new JButton(buttonTitle);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBackground(new Color(238, 238, 238));
        button.setBorder(BorderFactory.createLineBorder(Color.white));
        return button;
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