package org.calculator.gui.scientific;

import javax.swing.*;
import java.awt.event.*;

// 科学计算器主面板类
public class ScientificPanel extends JLayeredPane {
    private HistoryPanel historyPanel;
    private MainPanel mainPanel;

    public ScientificPanel() {
        mainPanel = new MainPanel(this);
        historyPanel = new HistoryPanel(this);
        historyPanel.setVisible(false);
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
    public boolean switchHistoryPanelState() {
        if (!historyPanel.isVisible()) {
            historyPanel.setVisible(true);
            return true;
        } else {
            historyPanel.setVisible(false);
            return false;
        }
    }

    public void addCalHistory(String result) {
        historyPanel.AddHistory(result);
    }

    public void provideCalHistory(String history) {
        mainPanel.catchCalHistory(history);
    }
}