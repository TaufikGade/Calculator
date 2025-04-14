package org.calculator.gui;

import org.calculator.gui.drawing.DrawingPanel;
import org.calculator.gui.scientific.ScientificPanel;
import org.calculator.gui.regression.RegressionPanel;

import javax.swing.*;

public class CalculatorGUI extends JFrame {
    public CalculatorGUI() {
        setTitle("Java Calculator");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Scientific", new ScientificPanel());
        tabbedPane.addTab("Drawing", new DrawingPanel());
        tabbedPane.addTab("Regression", new RegressionPanel());
        tabbedPane.addTab("Date", new DatePanel());

        add(tabbedPane);
        setVisible(true);
    }
}