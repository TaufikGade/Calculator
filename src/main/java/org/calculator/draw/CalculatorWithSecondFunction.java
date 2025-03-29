package org.calculator.draw;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorWithSecondFunction extends JFrame {
    private boolean isSecondMode = false;
    private JButton secondButton;
    private JButton sinButton;

    public CalculatorWithSecondFunction() {
        setTitle("科学计算器");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));

        // 创建2nd按钮
        secondButton = new JButton("2nd");
        secondButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSecondMode = !isSecondMode;
                updateButtonLabels();
                // 可以改变2nd按钮颜色以指示当前模式
                secondButton.setBackground(isSecondMode ? Color.YELLOW : null);
            }
        });

        // 创建sin按钮(在2nd模式下会变为arcsin)
        sinButton = new JButton("sin");
        sinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSecondMode) {
                    // 执行arcsin功能
                    performArcSin();
                } else {
                    // 执行sin功能
                    performSin();
                }
            }
        });

        // 添加按钮到面板
        buttonPanel.add(secondButton);
        buttonPanel.add(sinButton);
        // 添加其他按钮...

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void updateButtonLabels() {
        // 根据当前模式更新按钮标签
        sinButton.setText(isSecondMode ? "arcsin" : "sin");
        // 更新其他按钮...
    }

    private void performSin() {
        // 实现sin计算
        System.out.println("执行sin计算");
    }

    private void performArcSin() {
        // 实现arcsin计算
        System.out.println("执行arcsin计算");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CalculatorWithSecondFunction().setVisible(true);
        });
    }
}