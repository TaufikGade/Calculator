package org.calculator.gui.regression;

import org.calculator.gui.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DataPanel extends JPanel {
    private final RegressionPanel topPanel;
    private static final int MAX_DATA_POINTS = 100; // 最大数据点数
    private static final int INITIAL_DATA_POINTS = 5; // 初始数据点数
    private final List<JTextField> xFields;
    private final List<JTextField> yFields; // 存储输入框的列表
    private int inputCount = 0; // 当前输入框的数量
    private final List<TwoPoint> data; // 存储输入的数据
    private final JPanel inputFieldPanel;

    public DataPanel(RegressionPanel top) {
        this.topPanel = top;
        xFields = new ArrayList<>();
        yFields = new ArrayList<>();
        data = new ArrayList<>();

        setBackground(ThemeColors.getTotalBgColor());
        setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        // 顶部区域
        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setBorder(null);
        topArea.setBackground(ThemeColors.getTotalBgColor());

        Font chineseFont = new Font("Microsoft YaHei", Font.PLAIN, 25);

        // title标题
        JLabel title = new JLabel("数据：");
        title.setFont(chineseFont);
        title.setForeground(ThemeColors.getTextColor());

        JButton closeButton = topPanel.initButton("关闭数据面板");
        closeButton.addActionListener(_ -> topPanel.switchDataPanelState(false));

        topArea.add(title, BorderLayout.WEST);
        topArea.add(closeButton, BorderLayout.EAST);


        // 创建输入部分的面板（中部区域）
        inputFieldPanel = new JPanel();
        inputFieldPanel.setLayout(new BoxLayout(inputFieldPanel, BoxLayout.Y_AXIS));
        inputFieldPanel.setBackground(ThemeColors.getDarkContentColor());
        inputFieldPanel.setBorder(null);

        // 添加输入框面板和按钮面板到中央滚动面板
        JScrollPane dataPane = new JScrollPane(inputFieldPanel);
        dataPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        dataPane.setBorder(null);


        // 创建底部按钮区域
        JPanel bottomArea = new JPanel(new GridLayout(1, 2, 0, 20));
        bottomArea.setBackground(Color.white);
        bottomArea.setBorder(null);

        // 增加输入框按钮
        JButton addInputButton = topPanel.initButton("增加数据");
        // 减少输入框按钮
        JButton removeInputButton = topPanel.initButton("减少输入");
        bottomArea.add(addInputButton);
        bottomArea.add(removeInputButton);

        // 添加按钮事件
        addInputButton.addActionListener(_ -> addInputItem());

        removeInputButton.addActionListener(_ -> removeInputItem());

        add(topArea, BorderLayout.NORTH);

        add(dataPane, BorderLayout.CENTER);

        add(bottomArea, BorderLayout.SOUTH);

        for (int i = 0; i < INITIAL_DATA_POINTS; i++) addInputItem();
    }

    public List<TwoPoint> getData() {
        data.clear();
        for (int i = 0; i < xFields.size(); i++) {
            try {
                double x = Double.parseDouble(xFields.get(i).getText());
                double y = Double.parseDouble(yFields.get(i).getText());
                data.add(new TwoPoint(x, y));
            } catch (NumberFormatException e) {
                data.add(new TwoPoint()); // 如果输入无效，添加0
            }
        }
        return this.data;
    }


    private final Font labelFont = new Font("Microsoft YaHei", Font.PLAIN, 20);
    private final Font dataFont = new Font("Arial", Font.PLAIN, 18);

    private void addInputItem() {
        if (inputCount == MAX_DATA_POINTS) return;

        JPanel inputItem = new JPanel(new BorderLayout());
        inputItem.setBackground(ThemeColors.getDarkContentColor());

        JLabel label = new JLabel(String.format("数据%02d：", xFields.size() + 1));
        label.setFont(labelFont);
        label.setBackground(ThemeColors.getDarkContentColor());
        label.setForeground(ThemeColors.getTextColor());
        inputItem.add(label, BorderLayout.WEST);

        JPanel SingleInputPanelX = new JPanel(new BorderLayout());
        JLabel dimensionLabelX = new JLabel("x:");
        dimensionLabelX.setBackground(ThemeColors.getDarkContentColor());
        dimensionLabelX.setForeground(ThemeColors.getTextColor());
        JTextField textFieldX = new JTextField(30);
        textFieldX.setFont(dataFont);
        textFieldX.setBackground(ThemeColors.getDarkContentColor());
        textFieldX.setForeground(ThemeColors.getTextColor());
        SingleInputPanelX.add(dimensionLabelX, BorderLayout.WEST);
        SingleInputPanelX.add(textFieldX, BorderLayout.CENTER);
        SingleInputPanelX.setBackground(ThemeColors.getDarkContentColor());

        JPanel SingleInputPanelY = new JPanel(new BorderLayout());
        JLabel dimensionLabelY = new JLabel("y:");
        dimensionLabelY.setBackground(ThemeColors.getDarkContentColor());
        dimensionLabelY.setForeground(ThemeColors.getTextColor());
        JTextField textFieldY = new JTextField(30);
        textFieldY.setFont(dataFont);
        textFieldY.setBackground(ThemeColors.getDarkContentColor());
        textFieldY.setForeground(ThemeColors.getTextColor());
        SingleInputPanelY.add(dimensionLabelY, BorderLayout.WEST);
        SingleInputPanelY.add(textFieldY, BorderLayout.CENTER);
        SingleInputPanelY.setBackground(ThemeColors.getDarkContentColor());

        JPanel inputPart = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPart.setBackground(ThemeColors.getDarkContentColor());
        inputPart.add(SingleInputPanelX);
        inputPart.add(SingleInputPanelY);

        inputItem.add(inputPart, BorderLayout.CENTER);
        inputItem.setPreferredSize(new Dimension(200, 40));
        inputItem.setMaximumSize(new Dimension(10000, 40));

        xFields.add(textFieldX);
        yFields.add(textFieldY);

        inputFieldPanel.add(inputItem);
        inputFieldPanel.revalidate();
        inputFieldPanel.repaint();
        inputCount++;
    }

    private void removeInputItem() {
        if (inputCount == 0) return;

        inputFieldPanel.remove(inputCount - 1);
        xFields.removeLast();
        yFields.removeLast();
        inputFieldPanel.revalidate();
        inputFieldPanel.repaint();
        inputCount--;
    }
}
