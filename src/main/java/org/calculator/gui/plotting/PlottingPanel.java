package org.calculator.gui.plotting;
import javax.swing.*;
import org.calculator.math.MathEvaluator;
public class PlottingPanel extends JPanel {
    private GraphPanel graphpanel;
    private InputPanel inputPanel;
    private MathEvaluator evaluator ;

    public PlottingPanel() {
        GraphPanel graphpanel = new GraphPanel(this);
        add(graphpanel, JLayeredPane.DEFAULT_LAYER);
    }
}
