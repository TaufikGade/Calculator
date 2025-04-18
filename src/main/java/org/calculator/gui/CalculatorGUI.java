package org.calculator.gui;

import org.calculator.gui.drawing.DrawingPanel;
import org.calculator.gui.plotting.PlottingPanel;
import org.calculator.gui.scientific.ScientificPanel;
import org.calculator.gui.regression.RegressionPanel;
import org.chocosolver.solver.constraints.nary.nvalue.amnv.rules.R;

import java.util.List;

import javax.swing.*;
import java.util.Objects;

public class CalculatorGUI extends JFrame {
    public static boolean isDarkMode = false;
    private final JTabbedPane tabbedPane;
    private ScientificPanel scientificPanel;
    private DrawingPanel drawingPanel;
    private RegressionPanel regressionPanel;
    private String historyCache = "";
    private List<Double> drawingPanelData;
    private List<Double> regressionPanelDataX;
    private List<Double> regressionPanelDataY;

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
        if (!Objects.equals(historyCache, "")) scientificPanel = new ScientificPanel(historyCache);
        else scientificPanel = new ScientificPanel();

        tabbedPane.addTab("Scientific", scientificPanel);

        if (drawingPanelData != null) drawingPanel = new DrawingPanel(drawingPanelData);
        else drawingPanel = new DrawingPanel();

        tabbedPane.addTab("Drawing", drawingPanel);

        if (regressionPanelDataX != null) regressionPanel = new RegressionPanel(regressionPanelDataX, regressionPanelDataY);
        else regressionPanel = new RegressionPanel();

        tabbedPane.addTab("Regression", regressionPanel);
        tabbedPane.addTab("Plotting", new PlottingPanel());
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
            historyCache = scientificPanel.getHistory();
            drawingPanelData = drawingPanel.getData();
            regressionPanelDataX = regressionPanel.getDataX();
            regressionPanelDataY = regressionPanel.getDataY();
            init();
        }
    }
}