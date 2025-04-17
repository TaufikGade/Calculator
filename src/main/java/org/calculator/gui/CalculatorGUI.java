package org.calculator.gui;

import org.calculator.gui.drawing.DrawingPanel;
import org.calculator.gui.plotting.PlottingPanel;
import org.calculator.gui.scientific.ScientificPanel;
import org.calculator.gui.regression.RegressionPanel;

import javax.swing.*;

public class CalculatorGUI extends JFrame {
    public static boolean isDarkMode = false;
    private final JTabbedPane tabbedPane;

    public CalculatorGUI() {
        setTitle("Java Calculator");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabbedPane = new JTabbedPane();
        init();
        add(tabbedPane);
        setVisible(true);
    }

    public void init() {
        remove();
        create();
    }

    private void create() {
        tabbedPane.addTab("Scientific", new ScientificPanel());
        tabbedPane.addTab("Drawing", new DrawingPanel());
        tabbedPane.addTab("Regression", new RegressionPanel());
        tabbedPane.addTab("Plotting", new PlottingPanel());
        //tabbedPane.addTab("Date", new DatePanel());
    }

    private void remove() {
        int count = tabbedPane.getTabCount();
        for (int i = 0; i < count; i++) {
            tabbedPane.removeTabAt(0);
        }
    }

    public void setDarkMode(boolean mode) {
        if (mode != isDarkMode) {
            isDarkMode = mode;
            init();
        }
    }
}