package org.calculator.gui;

import org.calculator.gui.drawing.DrawingPanel;
import org.calculator.gui.plotting.PlottingPanel;
import org.calculator.gui.scientific.ScientificPanel;
import org.calculator.gui.regression.RegressionPanel;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
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
    private int tabIndex = 0;

    public CalculatorGUI() {
        setTitle("Java Calculator");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        init();
        add(tabbedPane);
        setVisible(true);
        tabbedPane.setFocusable(false);
        tabbedPane.setUI(new BasicTabbedPaneUI() {
            // 不绘制内容区域周围的边框
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // 空实现，取消默认内容边框
            }

            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(isSelected ? ThemeColors.getDarkBgColor() : ThemeColors.getTotalBgColor());
                g2d.fillRect(x, y, w, h);
                g2d.dispose();
            }

            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return 100;
            }

            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return 30;
            }
        });
    }

    public void init() {
        remove();
        create();
    }

    private void create() {

        var container = this.getContentPane();
        container.setBackground(ThemeColors.getLightBgColor());
        container.setForeground(ThemeColors.getTextColor());

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

        if (regressionPanelDataX != null)
            regressionPanel = new RegressionPanel(regressionPanelDataX, regressionPanelDataY);
        else regressionPanel = new RegressionPanel();

        tabbedPane.addTab("一元回归", regressionPanel);

        tabbedPane.setSelectedIndex(tabIndex);

        tabbedPane.setBackground(ThemeColors.getLightBgColor());
        tabbedPane.setForeground(ThemeColors.getTextColor());
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
            tabIndex = tabbedPane.getSelectedIndex();
            init();
        }
    }
}