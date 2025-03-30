package org.calculator.gui;

import org.calculator.math.MathEvaluator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.scilab.forge.jlatexmath.*;
import java.util.HashMap;
import java.util.Map;

public class ScientificPanel extends JPanel {
    private JTextField display;
    private StringBuilder inputExpression;
    private final MathEvaluator evaluator;
    private boolean isSecondMode = false;
    private Map<String, FunctionButton> functionButtons;


    public ScientificPanel() {
        this.evaluator = new MathEvaluator();
        this.functionButtons = new HashMap<>();
        initUI();
    }

    private void initUI(){
        setLayout(new BorderLayout());

        // 显示屏
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 38));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setSize(400, 150);
        add(display, BorderLayout.NORTH);

        inputExpression = new StringBuilder();

        // 控制符号面板
        JPanel controPanel = createControlPanel();
        add(controPanel, BorderLayout.CENTER);

        // 下方符号面板
        JPanel symbolPanel = createSymbolPanel();
        add(symbolPanel, BorderLayout.PAGE_END);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 10,10));
        String[] buttons = {
                "MC", "MR", "M+","M-", "MS", "M"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.PLAIN, 12));
            button.setBackground(new Color(238, 238, 238));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.addActionListener(new ButtonHandler());
            panel.add(button);
        }
        return panel;
    }

    private JPanel createSymbolPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 5, 5, 5));

        String[][] buttonDefinitions = {
                {"2^{nd}", "2nd"},
                {"\\pi", "π"},
                {"e", "e"},
                {"C", "C"},
                {"⌫", "⌫"},

                {"x^2", "x^2", "x^3", "x^3"},
                {"\\frac{1}{x}", "1/x"},
                {"|x|", "|x|", "\\lfloor x \\rfloor", "floor"},
                {"\\exp", "exp", "Mod", "Mod"},
                {"mod", "%", "\\frac{x}{y}", "÷*"},

                {"\\sqrt[2]{x}", "sqrt", "\\sqrt[3]{x}", "cbrt"},
                {"(", "("},
                {")", ")"},
                {"n!", "!"},
                {"\\div", "÷"},

                {"x^y", "^", "\\sqrt[y]{x}", "yroot"},
                {"7", "7"},
                {"8", "8"},
                {"9", "9"},
                {"\\times", "×"},

                {"10^x", "10^", "2^x", "2^"},
                {"4", "4"},
                {"5", "5"},
                {"6", "6"},
                {"-", "-"},

                {"\\log", "log(", "\\log_{y}{x}", "log base"},
                {"1", "1"},
                {"2", "2"},
                {"3", "3"},
                {"+", "+"},

                {"\\ln", "ln", "e^x", "e^"},
                {"+/-", "+/-", "Rand", "rand"},
                {"0", "0"},
                {".", "."},
                {"=", "="}
        };

        for (int i = 0; i < buttonDefinitions.length; i++) {
            String primaryLatex = buttonDefinitions[i][0];
            String primaryCmd = buttonDefinitions[i][1];
            String secondaryLatex = (buttonDefinitions[i].length > 2) ? buttonDefinitions[i][2] : primaryLatex;
            String secondaryCmd = (buttonDefinitions[i].length > 3) ? buttonDefinitions[i][3] : primaryCmd;

            TeXFormula primaryFormula = new TeXFormula(primaryLatex);
            Icon primaryIcon = i == buttonDefinitions.length - 1 ?
                    primaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20, TeXFormula.SERIF, Color.white) :
                    primaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);

            TeXFormula secondaryFormula = new TeXFormula(secondaryLatex);
            Icon secondaryIcon = i == buttonDefinitions.length - 1 ?
                    secondaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20, TeXFormula.SERIF, Color.white) :
                    secondaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);

            FunctionButton button;
            if (buttonDefinitions[i].length > 2) {
                button = new FunctionButton(primaryIcon, secondaryIcon, primaryCmd, secondaryCmd);
            } else {
                button = new FunctionButton(primaryIcon, primaryCmd);
            }

            Color btnColor;
            if (i == buttonDefinitions.length - 1) {
                btnColor = new Color(0, 103, 192);
            } else if (i >= 16 && i <= 18 || i >= 21 && i <= 23 || i >= 26 && i <= 28) {
                btnColor = Color.white;
            } else {
                btnColor = new Color(248, 249, 250);
            }
            button.setBackground(btnColor);
            button.setFocusPainted(false);
            if (primaryCmd.equals("2nd")) {
                button.addActionListener(e -> {
                    isSecondMode = !isSecondMode;
                    updateButtonsMode();
                    if (isSecondMode) {
                        button.setBackground(new Color(0, 103, 192));
                    } else {
                        button.setBackground(new Color(248, 249, 250));
                    }
                });
            } else {
                button.addActionListener(new ButtonHandler());
            }
            functionButtons.put(primaryCmd, button);
            panel.add(button);
        }
        return panel;
    }

    private void updateButtonsMode() {
        for (FunctionButton button : functionButtons.values()) {
            button.setSecondMode(isSecondMode);
        }
    }

    private class FunctionButton extends JButton {
        private Icon primaryIcon;
        private Icon secondaryIcon;
        private String primaryCommand;
        private String secondaryCommand;
        private boolean isInSecondMode = false;
        private boolean hasSecondaryFunction;

        public FunctionButton(Icon primaryIcon, Icon secondaryIcon, String primaryCommand, String secondaryCommand) {
            super(primaryIcon);
            this.primaryIcon = primaryIcon;
            this.secondaryIcon = secondaryIcon;
            this.primaryCommand = primaryCommand;
            this.secondaryCommand = secondaryCommand;
            setActionCommand(primaryCommand);
            this.hasSecondaryFunction = secondaryCommand != null && !secondaryCommand.isEmpty() && !secondaryCommand.equals(primaryCommand);
        }

        public FunctionButton(Icon primaryIcon, String primaryCommand) {
            this(primaryIcon, primaryIcon, primaryCommand, primaryCommand);
            this.hasSecondaryFunction = false;
        }

        public void setSecondMode(boolean secondMode) {
            if (hasSecondaryFunction) {
                isInSecondMode = secondMode;
                Dimension currentSize = getSize();

                if (secondMode) {
                    setIcon(secondaryIcon);
                    setActionCommand(secondaryCommand);
                } else {
                    setIcon(primaryIcon);
                    setActionCommand(primaryCommand);
                }

                setPreferredSize(currentSize);
                setMinimumSize(currentSize);
                setMaximumSize(currentSize);
            }
        }
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            switch (cmd) {
                case "=":
                    try {
                        double result = evaluator.evaluate(inputExpression.toString());
                        display.setText(String.valueOf(result));
                        inputExpression.setLength(0);
                        inputExpression.append(result);
                    } catch (Exception ex) {
                        display.setText("错误");
                        inputExpression.setLength(0);
                    }
                    break;

                case "MC":
                    //evaluator.clearMemory();
                    break;

                case "MR":
                    //double memValue = evaluator.recallMemory();
                    //inputExpression.append(String.valueOf(memValue));
                    //display.setText(inputExpression.toString());
                    break;

                case "M+":
                    //try {
                    //evaluator.addToMemory(Double.parseDouble(display.getText()));
                    // } catch (NumberFormatException ex) {
                    // 忽略非数字
                    //}
                    break;

                case "M-":
                    /*try {
                        evaluator.subtractFromMemory(Double.parseDouble(display.getText()));
                    } catch (NumberFormatException ex) {
                        // 忽略非数字
                    }*/
                    break;

                case "MS":
                    /* try {
                        evaluator.storeInMemory(Double.parseDouble(display.getText()));
                    } catch (NumberFormatException ex) {
                        // 忽略非数字
                    }*/
                    break;

                case "⌫":
                    if (inputExpression.length() > 0) {
                        inputExpression.deleteCharAt(inputExpression.length() - 1);
                        display.setText(inputExpression.toString());
                    }
                    break;

                case "C":
                    inputExpression.delete(0, inputExpression.length());
                    display.setText("");
                    break;

                // 三角函数
                case "sin":
                    inputExpression.append("sin(");
                    display.setText(inputExpression.toString());
                    break;

                case "cos":
                    inputExpression.append("cos(");
                    display.setText(inputExpression.toString());
                    break;

                case "tan":
                    inputExpression.append("tan(");
                    display.setText(inputExpression.toString());
                    break;

                // 反三角函数
                case "asin":
                    inputExpression.append("asin(");
                    display.setText(inputExpression.toString());
                    break;

                case "acos":
                    inputExpression.append("acos(");
                    display.setText(inputExpression.toString());
                    break;

                case "atan":
                    inputExpression.append("atan(");
                    display.setText(inputExpression.toString());
                    break;

                // 对数函数
                case "log":
                    inputExpression.append("log(");
                    display.setText(inputExpression.toString());
                    break;

                case "log base":
                    inputExpression.append("log(");
                    display.setText(inputExpression.toString());
                    break;

                case "ln":
                    inputExpression.append("ln(");
                    display.setText(inputExpression.toString());
                    break;

                // 指数函数
                case "10^":
                    inputExpression.append("10^(");
                    display.setText(inputExpression.toString());
                    break;

                case "2^":
                    inputExpression.append("2^(");
                    display.setText(inputExpression.toString());
                    break;

                case "e^":
                    inputExpression.append("exp("); // Use 'exp' for e^ in MathEvaluator
                    display.setText(inputExpression.toString());
                    break;

                // 幂函数
                case "x^2":
                    inputExpression.append("^2");
                    display.setText(inputExpression.toString());
                    break;

                case "x^3":
                    inputExpression.append("^3");
                    display.setText(inputExpression.toString());
                    break;

                case "^":
                    inputExpression.append("^");
                    display.setText(inputExpression.toString());
                    break;

                // 开方
                case "sqrt":
                    inputExpression.append("sqrt(");
                    display.setText(inputExpression.toString());
                    break;

                case "cbrt":
                    inputExpression.append("cbrt(");
                    display.setText(inputExpression.toString());
                    break;

                case "yroot":
                    inputExpression.append("yroot");
                    display.setText(inputExpression.toString());
                    break;

                // 其他数学函数
                case "1/x":
                    inputExpression.append("1/(");
                    display.setText(inputExpression.toString());
                    break;

                case "|x|":
                    inputExpression.append("abs(");
                    display.setText(inputExpression.toString());
                    break;

                case "floor":
                    inputExpression.append("floor(");
                    display.setText(inputExpression.toString());
                    break;

                case "exp":
                    inputExpression.append("exp(");
                    display.setText(inputExpression.toString());
                    break;

                case "Mod":
                    inputExpression.append("mod(");
                    display.setText(inputExpression.toString());
                    break;

                case "rand":
                    double random = Math.random();
                    inputExpression.append(String.valueOf(random));
                    display.setText(inputExpression.toString());
                    break;

                case "+/-":
                    if (inputExpression.length() > 0) {
                        try {
                            double currentValue = Double.parseDouble(display.getText());
                            currentValue = -currentValue;
                            inputExpression.setLength(0);
                            inputExpression.append(currentValue);
                            display.setText(inputExpression.toString());
                        } catch (NumberFormatException ex) {
                            inputExpression.insert(0, "-(");
                            inputExpression.append(")");
                            display.setText(inputExpression.toString());
                        }
                    } else {
                        inputExpression.append("-(");
                        display.setText(inputExpression.toString());
                    }
                    break;

                // 常量
                case "π":
                    inputExpression.append(Math.PI);
                    display.setText(inputExpression.toString());
                    break;

                case "e":
                    inputExpression.append(Math.E);
                    display.setText(inputExpression.toString());
                    break;

                // 基本运算符和数字
                default:
                    inputExpression.append(cmd);
                    display.setText(inputExpression.toString());
                    break;
            }
        }
    }
}