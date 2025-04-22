package org.calculator.gui;

import org.calculator.gui.drawing.DrawingPanel;
import org.calculator.gui.plotting.PlottingPanel;
import org.calculator.gui.scientific.ScientificPanel;
import org.calculator.gui.regression.RegressionPanel;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import java.util.Objects;

public class CalculatorGUI extends JFrame {
    public static boolean isDarkMode = false;
    private final JTabbedPane tabbedPane;
    private ScientificPanel scientificPanel;
    private String expressionCache = "";
    private String historyCache = "";
    private PlottingPanel plottingPanel;
    private String functionCache;
    private DrawingPanel drawingPanel;
    private List<Double> drawingPanelData;
    private RegressionPanel regressionPanel;
    private List<Double> regressionPanelDataX;
    private List<Double> regressionPanelDataY;

    public CalculatorGUI() {
        setTitle("Java Calculator");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        init();
        add(tabbedPane);
        setVisible(true);
    }

    public void init() {
        remove();
        create();
    }

    private void create() {
        if (!Objects.equals(historyCache, "") || !Objects.equals(expressionCache, ""))
            scientificPanel = new ScientificPanel(historyCache, expressionCache);
        else scientificPanel = new ScientificPanel();

        tabbedPane.addTab("科学计算器", scientificPanel);

        if (functionCache != null) plottingPanel = new PlottingPanel(functionCache);
        else plottingPanel = new PlottingPanel();

        tabbedPane.addTab("函数绘图", plottingPanel);

        if (drawingPanelData != null) drawingPanel = new DrawingPanel(drawingPanelData);
        else drawingPanel = new DrawingPanel();

        tabbedPane.addTab("数据统计图", drawingPanel);

        if (regressionPanelDataX != null) regressionPanel = new RegressionPanel(regressionPanelDataX, regressionPanelDataY);
        else regressionPanel = new RegressionPanel();

        tabbedPane.addTab("一元回归", regressionPanel);
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
            expressionCache = scientificPanel.getExpression();
            historyCache = scientificPanel.getHistory();
            functionCache = plottingPanel.getExpression();
            drawingPanelData = drawingPanel.getData();
            regressionPanelDataX = regressionPanel.getDataX();
            regressionPanelDataY = regressionPanel.getDataY();
            init();
        }
    }
}