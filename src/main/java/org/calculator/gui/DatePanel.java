package org.calculator.gui;

import javax.swing.*;
import java.awt.*;

public class DatePanel extends JPanel {
    public DatePanel() {
        setLayout(new GridLayout(3, 2, 10, 10));

        // 日期选择组件
        add(new JLabel("开始日期:"));
        JSpinner startSpinner = new JSpinner(new SpinnerDateModel());
        add(startSpinner);

        add(new JLabel("结束日期:"));
        JSpinner endSpinner = new JSpinner(new SpinnerDateModel());
        add(endSpinner);

        // 计算结果展示
        JButton calculateBtn = new JButton("计算间隔");
        JLabel resultLabel = new JLabel("结果将显示在这里");
        calculateBtn.addActionListener(e -> {
            // 这里可以添加日期计算逻辑
            resultLabel.setText("已计算：XX天");
        });

        add(calculateBtn);
        add(resultLabel);
    }
}