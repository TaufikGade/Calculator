package org.calculator.gui.plotting;

import javax.swing.*;
import org.calculator.math.MathEvaluator;
import java.awt.*;

public class PlottingPanel extends JPanel {
    private GraphPanel graphPanel;
    private InputPanel inputPanel;
    private MathEvaluator evaluator;

    public PlottingPanel() {
        // 初始化 MathEvaluator
        evaluator = new MathEvaluator();
        graphPanel = new GraphPanel(this);

        setLayout(new BorderLayout());
        add(graphPanel, BorderLayout.CENTER);

        // 确保图形面板刷新
        graphPanel.repaint();
    }
}
