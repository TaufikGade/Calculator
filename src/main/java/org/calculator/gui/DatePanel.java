package org.calculator.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DatePanel extends JPanel {

    private final JSpinner startSpinner;
    private final JSpinner endSpinner;
    private final JLabel resultLabel;

    public DatePanel() {
        setLayout(new GridLayout(3, 2, 10, 10));

        // 日期选择组件
        add(new JLabel("开始日期:"));
        startSpinner = new JSpinner(new SpinnerDateModel());
        add(startSpinner);

        add(new JLabel("结束日期:"));
        endSpinner = new JSpinner(new SpinnerDateModel());
        add(endSpinner);

        // 计算结果展示
        JButton calculateBtn = new JButton("计算间隔");
        resultLabel = new JLabel("结果将显示在这里");
        calculateBtn.addActionListener(e -> calDaysDiff());

        add(calculateBtn);
        add(resultLabel);
    }

    private void calDaysDiff() {
        LocalDate start = ((Date) startSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = ((Date) endSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String message = MessageFormat.format("已计算：{0}天", end.toEpochDay() - start.toEpochDay());
        resultLabel.setText(message);
    }
}