package org.calculator.gui.scientific;

import javax.swing.*;
import java.awt.event.*;

// 科学计算器主面板类
public class ScientificPanel extends JLayeredPane {
    private HistoryPanel historyPanel;
    private MainPanel mainPanel;

    public ScientificPanel() {
        historyPanel = new HistoryPanel(this);
        historyPanel.setVisible(false);
        init();
    }

    public ScientificPanel(String history) {
        historyPanel = new HistoryPanel(this, history);
        historyPanel.setVisible(false);
        init();
    }

    public void init() {
        mainPanel = new MainPanel(this);

        add(mainPanel, JLayeredPane.DEFAULT_LAYER);
        add(historyPanel, JLayeredPane.PALETTE_LAYER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.setBounds(0, 0, getWidth(), getHeight());
                historyPanel.setBounds(0, (int) (getHeight() * 0.4f), getWidth(), (int) (getHeight() * 0.6f));
            }
        });
    }


    // 返回true  表示历史记录面板出现
    // 返回false 表示隐藏历史记录面板
    public void switchHistoryPanelState() {
        historyPanel.setVisible(!historyPanel.isVisible());
        mainPanel.onHistoryPanelVisible(historyPanel.isVisible());
    }

    public void addCalHistory(String result) {
        historyPanel.AddHistory(result);
    }

    public void provideCalHistory(String history) {
        mainPanel.catchCalHistory(history);
    }

    public String getHistory() {
        return historyPanel.getHistory();
    }
}