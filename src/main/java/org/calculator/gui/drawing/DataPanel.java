package org.calculator.gui.drawing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DataPanel extends JPanel {
    private final DrawingPanel topPanel;
    private final List<JTextField> inputFields; // 存储输入框的列表
    private int inputCount = 0; // 当前输入框的数量
    private final int MAX_INPUTS = 50; // 最大输入框数量限制
    private List<Double> data; // 存储输入的数据
    private final JPanel inputFieldPanel;

    public DataPanel(DrawingPanel top) {
        this.topPanel = top;
        inputFields = new ArrayList<>();
        data = new ArrayList<>();

        setBackground(Color.white);
        setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)
        ));

        // 顶部区域
        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setBorder(BorderFactory.createEmptyBorder());
        topArea.setBackground(Color.white);

        Font chineseFont = new Font("Microsoft YaHei", Font.PLAIN, 25);

        // title标题
        JLabel title = new JLabel("数据：");
        title.setFont(chineseFont);

        JButton closeButton = topPanel.initButton("关闭数据面板", Color.white);
        closeButton.setBorder(BorderFactory.createLineBorder(new Color(238, 238, 238)));
        closeButton.addActionListener(e -> topPanel.switchDataPanelState());

        topArea.add(title, BorderLayout.WEST);
        topArea.add(closeButton, BorderLayout.EAST);


        // 创建输入部分的面板（中部区域）
        inputFieldPanel = new JPanel();
        inputFieldPanel.setLayout(new BoxLayout(inputFieldPanel, BoxLayout.Y_AXIS));

        // 添加输入框面板和按钮面板到中央滚动面板
        JScrollPane dataPane = new JScrollPane(inputFieldPanel);
        dataPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        // 创建底部按钮区域
        JPanel bottomArea = new JPanel(new GridLayout(1, 2, 0, 20));
        bottomArea.setBackground(Color.white);

        // 增加输入框按钮
        JButton addInputButton = topPanel.initButton("增加数据", Color.white);
        addInputButton.setBorder(BorderFactory.createLineBorder(new Color(238, 238, 238)));
        // 减少输入框按钮
        JButton removeInputButton = topPanel.initButton("减少输入", Color.white);
        removeInputButton.setBorder(BorderFactory.createLineBorder(new Color(238, 238, 238)));
        bottomArea.add(addInputButton);
        bottomArea.add(removeInputButton);

        // 添加按钮事件
        addInputButton.addActionListener(e -> addInputItem());

        removeInputButton.addActionListener(e -> removeInputItem());

        add(topArea, BorderLayout.NORTH);

        add(dataPane, BorderLayout.CENTER);

        add(bottomArea, BorderLayout.SOUTH);
    }

    public List<Double> getData() {
        data.clear();
        for (JTextField textField : inputFields) {
            try {
                data.add(Double.parseDouble(textField.getText()));
            } catch (NumberFormatException e) {
                data.add(0.0); // 如果输入无效，添加0
            }
        }
        return this.data;
    }


    private final Font labelFont = new Font("Microsoft YaHei", Font.PLAIN, 20);
    private final Font dataFont = new Font("Arial", Font.PLAIN, 18);

    private void addInputItem() {
        if (inputCount == MAX_INPUTS) return;

        JPanel inputItem = new JPanel(new BorderLayout());

        JLabel label = new JLabel(String.format("数据%02d：", inputFields.size() + 1));
        label.setFont(labelFont);
        inputItem.add(label, BorderLayout.WEST);

        JTextField textField = new JTextField(30);
        textField.setFont(dataFont);
        inputItem.add(textField, BorderLayout.CENTER);
        inputItem.setPreferredSize(new Dimension(200, 40));
        inputItem.setMaximumSize(new Dimension(10000, 40));

        inputFields.add(textField);
        inputFieldPanel.add(inputItem);
        inputFieldPanel.revalidate();
        inputFieldPanel.repaint();
        inputCount++;
    }

    private void removeInputItem() {
        if (inputCount == 0) return;

        inputFieldPanel.remove(inputCount - 1);
        inputFields.removeLast();
        inputFieldPanel.revalidate();
        inputFieldPanel.repaint();
        inputCount--;
    }
}
