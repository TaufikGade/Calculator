package org.calculator.gui.scientific;

import org.calculator.gui.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class HistoryPanel extends JPanel {
    private final ScientificPanel topPanel;
    private JTextArea historyArea;

    public HistoryPanel(ScientificPanel top) {
        this.topPanel = top;
        init();
    }

    public HistoryPanel(ScientificPanel top, String history) {
        this.topPanel = top;
        init();
        historyArea.setText(history);
    }

    private void init() {
        // Panel本体
        setLayout(new BorderLayout());
        setBackground(ThemeColors.getDarkBgColor());
        this.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        Font chineseFont = new Font("Microsoft YaHei", Font.PLAIN, 20);

        // 顶部区域
        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setBackground(ThemeColors.getDarkBgColor());
        topArea.setBorder(BorderFactory.createEmptyBorder());

        // title标题
        JLabel title = new JLabel("历史记录");
        title.setFont(chineseFont);
        title.setForeground(ThemeColors.getTextColor());
        topArea.add(title, BorderLayout.WEST);

        JButton closeButton = new JButton("关闭历史记录");
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFont(chineseFont);
        closeButton.setBackground(ThemeColors.getDarkBgColor());
        closeButton.setForeground(ThemeColors.getTextColor());
        closeButton.setFocusPainted(false);
        //closeButton.setBackground(new Color(238, 238, 238));
        topArea.add(closeButton, BorderLayout.EAST);
        closeButton.addActionListener(_ -> topPanel.switchHistoryPanelState());
        closeButton.addMouseListener(new ButtonMouseHandler(closeButton));

        add(topArea, BorderLayout.NORTH);

        // historyArea 历史记录区域
        historyArea = new JTextArea("没有历史记录\n");
        historyArea.setEditable(false);
        historyArea.setBorder(null);
        historyArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));
        historyArea.setForeground(ThemeColors.getTextColor());
        historyArea.setBackground(ThemeColors.getLightContentColor());

        // historyScroll 历史记录区域滚动条
        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        historyScroll.setBorder(null);
        add(historyScroll, BorderLayout.CENTER);

        // 清除历史按钮
        JButton clearButton = new JButton("清除历史");
        clearButton.addActionListener(_ -> historyArea.setText("没有历史记录\n"));
        clearButton.setFocusPainted(false);
        clearButton.setBorder(null);
        clearButton.setBackground(ThemeColors.getDarkBgColor());
        clearButton.setForeground(ThemeColors.getTextColor());
        clearButton.addMouseListener(new ButtonMouseHandler(clearButton));
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
            button.setBackground(ThemeColors.getDarkBgColor());
        }
    }

    public String getHistory() {
        return historyArea.getText();
    }
}