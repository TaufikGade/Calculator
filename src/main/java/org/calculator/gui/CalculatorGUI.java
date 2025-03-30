package org.calculator.gui;

import javax.swing.*;

public class CalculatorGUI extends JFrame {
    public CalculatorGUI() {
        setTitle("Java Calculator");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Scientific", new ScientificPanel());
        tabbedPane.addTab("Drawing", new DrawingPanel());
        tabbedPane.addTab("Date", new DatePanel());

        add(tabbedPane);
        setVisible(true);
    }
}