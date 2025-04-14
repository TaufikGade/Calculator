package org.calculator.gui.scientific;

import org.calculator.math.MathEvaluator;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainPanel extends JPanel {
    private final ScientificPanel topPanel;
    private final JTextField display;
    private final StringBuilder inputExpression;
    private final MathEvaluator evaluator;
    private final MathEvaluator symEvaluator;
    private boolean isSecondMode = false;
    private final Map<String, FunctionButton> functionButtons;
    private final ButtonHandler buttonHandler;
    // 添加三角函数弹出菜单
    private JPopupMenu trigPopupMenu;
    private JButton trigButton;
    private final ControlPanel controlPanel;
    private double memoryValue = Double.MAX_VALUE; // 新增变量，用于存储内存中的值

    public MainPanel(ScientificPanel top) {
        this.topPanel = top;

        this.evaluator = new MathEvaluator();
        this.symEvaluator = new MathEvaluator();
        this.functionButtons = new HashMap<>();
        buttonHandler = new ButtonHandler();
        setLayout(new BorderLayout());
        // 显示屏
        display = new JTextField();
        display.setFont(new Font("Microsoft YaHei", Font.BOLD, 38));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        inputExpression = new StringBuilder();

        // 创建顶部面板，包含特殊功能按钮（如三角函数按钮）
        JPanel topFunctionsPanel = createTopFunctionsPanel();

        // 控制符号面板
        controlPanel = new ControlPanel();

        // 将两个面板放在一个垂直面板中
        JPanel northPanel = new JPanel(new GridLayout(2, 1));
        northPanel.add(topFunctionsPanel, BorderLayout.NORTH);
        northPanel.add(controlPanel, BorderLayout.CENTER);

        // 下方符号面板
        JPanel symbolPanel = createSymbolPanel();

        // 创建中央面板来容纳北部面板和符号面板
        JPanel centralPanel = new JPanel(new BorderLayout());
        centralPanel.add(northPanel, BorderLayout.NORTH);
        centralPanel.add(symbolPanel, BorderLayout.CENTER);

        add(centralPanel, BorderLayout.CENTER);
    }

    private JPanel createTopFunctionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBackground(new Color(240, 240, 240));

        // 创建三角学按钮
        trigButton = new JButton("三角学 ▼");
        trigButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        trigButton.setFocusPainted(false);
        trigButton.setBackground(new Color(248, 249, 250));

        // 创建三角函数弹出菜单
        trigPopupMenu = createTrigPopupMenu();

        // 添加三角学按钮点击事件
        trigButton.addActionListener(e -> {
            // 显示弹出菜单在按钮下方
            trigPopupMenu.show(trigButton, 0, trigButton.getHeight());
        });
        panel.add(trigButton);

        // 添加求导按钮
        JButton derivativeButton = new JButton("求导");
        derivativeButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        derivativeButton.setFocusPainted(false);
        derivativeButton.setBackground(new Color(248, 249, 250));
        derivativeButton.addActionListener(buttonHandler);
        panel.add(derivativeButton);

        // 添加函数按钮
        JButton functionButton = new JButton("函数");
        functionButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        functionButton.setFocusPainted(false);
        functionButton.setBackground(new Color(248, 249, 250));
        functionButton.addActionListener(e -> {
            // 显示函数弹出菜单在按钮下方
            JPopupMenu functionPopupMenu = createFunctionPopupMenu();
            functionPopupMenu.show(functionButton, 0, functionButton.getHeight());
        });
        panel.add(functionButton);

        return panel;
    }

    // 创建三角函数弹出菜单
    private JPopupMenu createTrigPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(new Color(248, 249, 250));

        // 创建包含三角函数按钮的面板
        JPanel trigPanel = new JPanel(new GridLayout(2, 3, 2, 2));
        trigPanel.setBackground(new Color(248, 249, 250));

        // 添加三角函数按钮
        String[] regularFunctions = {"sin", "cos", "tan"};
        String[] inverseFunctions = {"asin", "acos", "atan"};

        // 添加常规三角函数
        for (String function : regularFunctions) {
            JButton button = new JButton(function);
            button.setFont(new Font("Arial", Font.PLAIN, 12));
            button.setForeground(Color.BLACK);
            button.setBackground(new Color(248, 249, 250));
            button.setFocusPainted(false);
            button.setActionCommand(function);
            button.addActionListener(buttonHandler);
            trigPanel.add(button);
        }

        // 添加反三角函数
        for (String function : inverseFunctions) {
            JButton button = new JButton(function);
            button.setFont(new Font("Arial", Font.PLAIN, 12));
            button.setForeground(Color.BLACK);
            button.setBackground(new Color(248, 249, 250));
            button.setFocusPainted(false);
            button.setActionCommand(function);
            button.addActionListener(buttonHandler);
            trigPanel.add(button);
        }

        menu.add(trigPanel);
        return menu;
    }

    private JPopupMenu createFunctionPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(new Color(248, 249, 250));

        JPanel functionPanel = new JPanel(new GridLayout(2, 3, 2, 2));
        functionPanel.setBackground(new Color(248, 249, 250));

        String[] functions = {"sin(x)", "cos(x)", "tan(x)", "ln(x)", "lg(10, x)", "exp(x)"};

        for (String function : functions) {
            JButton button = new JButton(function);
            button.setFont(new Font("Arial", Font.PLAIN, 12));
            button.setForeground(Color.BLACK);
            button.setBackground(new Color(248, 249, 250));
            button.setFocusPainted(false);
            button.setActionCommand(function);
            button.addActionListener(e -> {
                // 如果前一个字符是数字，则自动添加乘号
                if (!inputExpression.isEmpty() && Character.isDigit(inputExpression.charAt(inputExpression.length() - 1))) {
                    inputExpression.append("×");
                }
                inputExpression.append(function);
                display.setText(inputExpression.toString());
            });
            functionPanel.add(button);
        }

        menu.add(functionPanel);
        return menu;
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
                {"\\exp", "exp"},
                {"mod", "%"},

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

                {"\\log", "log(", "\\log_{y}{x}", "log_base"},
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
                button.addActionListener(buttonHandler);
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

    public void catchCalHistory(String history) {
        inputExpression.append(history);
        display.setText(inputExpression.toString());
    }

    public void onHistoryPanelVisible(boolean isVisible) {
        controlPanel.updateHistoryButton(isVisible);
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            switch (cmd) {
                case "=":
                    try {
                        String expression = inputExpression.toString();
                        double result = evaluator.evaluate(inputExpression.toString());

                        // 添加到历史记录
                        String historyEntry = String.format("[%tT] %s = %.4f\n",
                                new Date(), expression, result);
                        topPanel.addCalHistory(historyEntry);

                        display.setText(String.valueOf(result));
                        inputExpression.setLength(0);
                        inputExpression.append(result);
                    } catch (StackOverflowError | ArithmeticException ex) {
                        display.setText("溢出");
                        inputExpression.setLength(0);
                    } catch (Exception ex) {
                        display.setText("错误");
                        inputExpression.setLength(0);
                    }
                    break;

                case "⌫":
                    if (!inputExpression.isEmpty()) {
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
                case "log(":
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
                    inputExpression.append(" yroot ");
                    display.setText(inputExpression.toString());
                    break;

                case "log_base":
                    inputExpression.append(" logbase ");
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

                case "rand":
                    double random = Math.random();
                    inputExpression.append(random);
                    display.setText(inputExpression.toString());
                    break;

                case "+/-":
                    if (!inputExpression.isEmpty()) {
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

                // 求导
                case "求导":
                    try {
                        String expression = inputExpression.toString();
                        String derivative = evaluator.derivativeWithSymja(expression);
                        inputExpression.setLength(0);
                        inputExpression.append(derivative);
                        display.setText(derivative);
                    } catch (Exception ex) {
                        display.setText("无法求导");
                    }
                    break;

                // 基本运算符和数字
                default:
                    inputExpression.append(cmd);
                    display.setText(inputExpression.toString());
                    break;
            }
        }
    }




    private class FunctionButton extends JButton {
        private final Icon primaryIcon;
        private final Icon secondaryIcon;
        private final String primaryCommand;
        private final String secondaryCommand;
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

        public boolean isInSecondMode() {
            return isInSecondMode;
        }
    }

    private class ControlPanel extends JPanel {
        private JButton MCButton;
        private JButton MRButton;
        private JButton historyButton;

        public ControlPanel() {
            setLayout(new GridLayout(1, 6, 10, 10));
            String[] buttons = {
                    "MC", "MR", "M+", "M-", "MS", "History"
            };

            for (int i = 0; i < 6; i++) {
                String text = buttons[i];
                JButton button = new JButton(text);
                button.setFocusPainted(false);
                button.setFont(new Font("Arial", Font.PLAIN, 15));
                button.setBackground(new Color(238, 238, 238));
                button.setBorder(BorderFactory.createEmptyBorder());
                switch (i) {
                    case 0:
                        button.addActionListener(_ -> memoryClear());
                        MCButton = button;
                        break;
                    case 1:
                        button.addActionListener(_ -> memoryRecall());
                        MRButton = button;
                        break;
                    case 2:
                        button.addActionListener(_ -> memoryAdd());
                        break;
                    case 3:
                        button.addActionListener(_ -> memorySubtract());
                        break;
                    case 4:
                        button.addActionListener(_ -> memoryStore());
                        break;
                    case 5:
                        button.addActionListener(_ -> historyButton());
                        historyButton = button;
                        break;
                }
                add(button);
            }
            updateButtonState();
        }

        private void memoryClear() {
            memoryValue = Double.MAX_VALUE;
            updateButtonState();
        }

        private void memoryRecall() {
            inputExpression.append(memoryValue); // 将内存值追加到输入表达式
            display.setText(String.valueOf(memoryValue)); // 显示在显示屏上
        }

        private void memoryStore() {
            String text = display.getText(); // 获取当前显示屏上的值
            if (!text.isEmpty()) {
                try {
                    memoryValue = Double.parseDouble(text); // 从内存中减去当前值
                } catch (NumberFormatException ex) {
                    display.setText("错误"); // 如果解析失败，显示错误
                }
            }
            updateButtonState();
        }

        private void memoryAdd() {
            if (memoryValue == Double.MAX_VALUE) memoryValue = 0;
            String text = display.getText(); // 获取当前显示屏上的值
            if (!text.isEmpty()) {
                try {
                    double currentValue = Double.parseDouble(text);
                    memoryValue += currentValue; // 从内存中减去当前值
                } catch (NumberFormatException ex) {
                    display.setText("错误"); // 如果解析失败，显示错误
                }
            }
            updateButtonState();
        }

        private void memorySubtract() {
            if (memoryValue == Double.MAX_VALUE) memoryValue = 0;
            String text = display.getText(); // 获取当前显示屏上的值
            if (!text.isEmpty()) {
                try {
                    double currentValue = Double.parseDouble(text);
                    memoryValue -= currentValue; // 从内存中减去当前值
                } catch (NumberFormatException ex) {
                    display.setText("错误"); // 如果解析失败，显示错误
                }
            }
            updateButtonState();
        }

        private void historyButton() {
            topPanel.switchHistoryPanelState();
        }

        private void updateButtonState() {
            if (memoryValue == Double.MAX_VALUE) {
                MCButton.setEnabled(false);
                MRButton.setEnabled(false);
            } else {
                MCButton.setEnabled(true);
                MRButton.setEnabled(true);
            }
        }

        public void updateHistoryButton(boolean isVisible) {
            historyButton.setBackground(isVisible ? new Color(0, 103, 192) : new Color(238, 238, 238));
            // #EEEEEE
        }
    }
}