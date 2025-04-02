package org.calculator.gui.scientific;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class HistoryPanel extends JPanel {
    private final ScientificPanel topPanel;
    private final JTextArea historyArea;

    public HistoryPanel(ScientificPanel top) {
        this.topPanel = top;

        // Panel本体
        setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)
        ));

        Font chineseFont = new Font("Microsoft YaHei", Font.PLAIN, 20);

        // 顶部区域
        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setBorder(BorderFactory.createEmptyBorder());

        // title标题
        JLabel title = new JLabel("历史记录");
        title.setFont(chineseFont);
        topArea.add(title, BorderLayout.WEST);

        JButton closeButton = new JButton("关闭历史记录");
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFont(chineseFont);
        closeButton.setFocusPainted(false);
        closeButton.setBackground(new Color(238, 238, 238));
        topArea.add(closeButton, BorderLayout.EAST);
        closeButton.addActionListener(e -> topPanel.switchHistoryPanelState());

        add(topArea, BorderLayout.NORTH);

        // historyArea 历史记录区域
        historyArea = new JTextArea("没有历史记录\n");
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));

        // historyScroll 历史记录区域滚动条
        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(historyScroll, BorderLayout.CENTER);

        // 清除历史按钮
        JButton clearButton = new JButton("清除历史");
        clearButton.addActionListener(e -> historyArea.setText("没有历史记录\n"));
        clearButton.setFocusPainted(false);
        add(clearButton, BorderLayout.SOUTH);

        historyArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    getHistoryText(e);
                }
            }
        });
    }

    public void AddHistory(String historyEntry) {
        if (Objects.equals(historyArea.getText(), "没有历史记录\n")) historyArea.setText("");

        historyArea.append(historyEntry);

        // 自动滚动到底部
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }

    private void getHistoryText(MouseEvent e) {
        int pos = historyArea.viewToModel2D(e.getPoint());
        try {
            int start = historyArea.getLineStartOffset(historyArea.getLineOfOffset(pos));
            int end = historyArea.getLineEndOffset(historyArea.getLineOfOffset(pos));
            String selectedLine = historyArea.getText(start, end - start);

            if (start == end || Objects.equals(selectedLine, "没有历史记录\n")) return;

            // 提取表达式部分（示例格式：[15:30:45] 2+3*4=14.0）
            String expression = selectedLine.split("=")[0].split("] ")[1];
            expression = expression.substring(0, expression.length() - 1);

            topPanel.provideCalHistory(expression);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}