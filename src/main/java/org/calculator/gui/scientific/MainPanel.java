package org.calculator.gui.scientific;

import org.calculator.gui.CalculatorGUI;
import org.calculator.math.MathEvaluator;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainPanel extends JPanel {
    private final ScientificPanel topPanel;
    private final JTextField display;
    private final StringBuilder inputExpression;
    private final MathEvaluator evaluator;
    private final Map<String, FunctionButton> functionButtons;
    private final ButtonHandler buttonHandler;
    // 添加三角函数弹出菜单
    private JPopupMenu trigPopupMenu;
    private JButton trigButton;
    private final ControlPanel controlPanel;
    private double memoryValue = Double.MAX_VALUE; // 新增变量，用于存储内存中的值
    private boolean isHistoryShow = false;
    private boolean isSecondMode = false;

    //region ColorDefinitions
    private final Color dayBgColor = new Color(243, 243, 243); //F3F3F3
    private final Color darkBgColor = new Color(20, 20, 20); //202020
    private final Color daySymbolColor = new Color(249, 249, 249); //F9F9F9
    private final Color darkSymbolColor = new Color(32, 32, 32); //323232
    private final Color dayNumberColor = Color.white; //FFFFFF
    private final Color darkNumberColor = new Color(59, 59, 59); //3B3B3B
    private final Color dayTextColor = Color.black;
    private final Color darkTextColor = Color.lightGray;
    private final Color dayEqualColor = new Color(0, 67, 192);//#0067C0
    private final Color darkEqualColor = new Color(76, 194, 255);//#4CC2FF
    private final Color dayNumberHoverColor = new Color(252, 252, 252);
    private final Color darkNumberHoverColor = new Color(32, 32, 32);
    private final Color daySymbolHoverColor = new Color(246, 246, 246);
    private final Color darkSymbolHoverColor = new Color(60, 60, 60);
    private final Color dayNumberClickColor = new Color(249, 249, 251);
    private final Color darkNumberClickColor = new Color(28, 28, 28);
    private final Color daySymbolClickColor = new Color(244, 244, 246);
    private final Color darkSymbolClickColor = new Color(32, 32, 32);
    private final Color dayEqualHoverColor = new Color(19, 75, 197); //1975C5
    private final Color darkEqualHoverColor = new Color(47, 177, 232); //47B1E8
    private final Color dayEqualClickColor = new Color(30, 83, 202); //3083CA
    private final Color darkEqualClickColor = new Color(42, 17, 211); //42A1D3
    private final Color dayFunctionHoverColor = new Color(234, 234, 234);
    private final Color darkFunctionHoverColor = new Color(45, 45, 45);
    private final Color dayFunctionClick = new Color(236, 237, 238);
    private final Color darkFunctionClick = new Color(29, 29, 42);
    //endregion

    public MainPanel(ScientificPanel top) {
        this.topPanel = top;
        setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
        setBorder(BorderFactory.createEmptyBorder());

        this.evaluator = new MathEvaluator();
        this.functionButtons = new HashMap<>();
        buttonHandler = new ButtonHandler();
        setLayout(new BorderLayout());
        // 显示屏
        display = new JTextField();
        display.setForeground(CalculatorGUI.isDarkMode ? darkTextColor : dayTextColor);
        display.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
        display.setBorder(null);

        try {
            InputStream fontStream = MainPanel.class.getResourceAsStream("/fonts/JetBrainsMono-Bold.ttf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(38f);
            display.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "字体加载失败！");
        }

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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);

        // 创建三角学按钮
        trigButton = new JButton("三角学 ▼");
        trigButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        trigButton.setBorder(BorderFactory.createLineBorder(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor));
        trigButton.setFocusPainted(false);
        trigButton.setForeground(CalculatorGUI.isDarkMode ? darkTextColor : dayTextColor);
        trigButton.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
        trigButton.addMouseListener(new FunctionMouseHandler(trigButton));

        // 创建三角函数弹出菜单
        createTrigPopupMenu();

        // 添加三角学按钮点击事件
        trigButton.addActionListener(e -> {
            // 显示弹出菜单在按钮下方
            trigPopupMenu.show(trigButton, 0, trigButton.getHeight());
        });
        panel.add(trigButton);

        // 添加求导按钮
        JButton derivativeButton = new JButton("求导");
        derivativeButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        derivativeButton.setBorder(BorderFactory.createLineBorder(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor));
        derivativeButton.setFocusPainted(false);
        derivativeButton.setForeground(CalculatorGUI.isDarkMode ? darkTextColor : dayTextColor);
        derivativeButton.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
        derivativeButton.addActionListener(buttonHandler);
        derivativeButton.addMouseListener(new FunctionMouseHandler(derivativeButton));
        panel.add(derivativeButton);

        return panel;
    }

    // 创建三角函数弹出菜单
    private void createTrigPopupMenu() {
        trigPopupMenu = new JPopupMenu();
        trigPopupMenu.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);

        // 创建包含三角函数按钮的面板
        JPanel trigPanel = new JPanel(new GridLayout(2, 3, 2, 2));
        trigPanel.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);

        // 添加三角函数按钮
        String[] showText = {"sin", "cos", "tan", "asin", "acos", "atan"};
        String[] latexText = {"\\sin", "\\cos", "\\tan", "\\arcsin", "\\arccos", "\\arctan"};

        // 添加三角函数
        for (int i = 0; i < 6; i++) {
            TeXFormula formula = new TeXFormula(latexText[i]);
            Icon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 17, TeXFormula.SERIF, CalculatorGUI.isDarkMode ? darkTextColor : dayTextColor);
            JButton button = new JButton(icon);
            button.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
            button.setBorder(null);
            button.setFocusPainted(false);
            button.setActionCommand(showText[i]);
            button.addActionListener(buttonHandler);
            button.addMouseListener(new SymbolMouseHandler(button));
            trigPanel.add(button);
        }

        trigPopupMenu.add(trigPanel);
    }

//    private JPopupMenu createFunctionPopupMenu() {
//        JPopupMenu menu = new JPopupMenu();
//        menu.setBackground(new Color(248, 249, 250));
//
//        JPanel functionPanel = new JPanel(new GridLayout(2, 3, 2, 2));
//        functionPanel.setBackground(new Color(248, 249, 250));
//
//        String[] functions = {"sin(x)", "cos(x)", "tan(x)", "ln(x)", "lg(10, x)", "exp(x)"};
//
//        for (String function : functions) {
//            JButton button = new JButton(function);
//            button.setFont(new Font("Arial", Font.PLAIN, 12));
//            button.setForeground(Color.BLACK);
//            button.setBackground(new Color(248, 249, 250));
//            button.setFocusPainted(false);
//            button.setActionCommand(function);
//            button.addActionListener(e -> {
//                // 如果前一个字符是数字，则自动添加乘号
//                if (!inputExpression.isEmpty() && Character.isDigit(inputExpression.charAt(inputExpression.length() - 1))) {
//                    inputExpression.append("×");
//                }
//                inputExpression.append(function);
//                display.setText(inputExpression.toString());
//            });
//            functionPanel.add(button);
//        }
//
//        menu.add(functionPanel);
//        return menu;
//    }

    private JPanel createSymbolPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 5, 5, 5));
        panel.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);

        String[][] buttonDefinitions = {
                {"2^{nd}", "2nd"},
                {"\\pi", "π"},
                {"e", "e"},
                {"C", "C"},
                {"⌫", "⌫"},

                {"x^2", "x^2", "x^3", "x^3"},
                {"\\frac{1}{x}", "1/x"},
                {"|x|", "|x|", "x", "x"},
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

                {"\\lg", "lg(", "\\log_{y}{x}", "log_base"},
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

            // LaTeX表达式颜色
            TeXFormula primaryFormula = new TeXFormula(primaryLatex), secondaryFormula = new TeXFormula(secondaryLatex);
            Icon primaryIcon, secondaryIcon;
            if (CalculatorGUI.isDarkMode) {
                if (i == buttonDefinitions.length - 1) {
                    //等于号的字体颜色与其他的相反
                    primaryIcon = primaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20, TeXFormula.SERIF, dayTextColor);
                    secondaryIcon = secondaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20, TeXFormula.SERIF, dayTextColor);
                } else {
                    primaryIcon = primaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20, TeXFormula.SERIF, darkTextColor);
                    secondaryIcon = secondaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20, TeXFormula.SERIF, darkTextColor);
                }
            } else {
                if (i == buttonDefinitions.length - 1) {
                    //等于号的字体颜色与其他的相反
                    primaryIcon = primaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20, TeXFormula.SERIF, darkTextColor);
                    secondaryIcon = secondaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20, TeXFormula.SERIF, darkTextColor);
                } else {
                    primaryIcon = primaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20, TeXFormula.SERIF, dayTextColor);
                    secondaryIcon = secondaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20, TeXFormula.SERIF, dayTextColor);
                }
            }

            FunctionButton button;
            if (buttonDefinitions[i].length > 2) {
                button = new FunctionButton(primaryIcon, secondaryIcon, primaryCmd, secondaryCmd);
            } else {
                button = new FunctionButton(primaryIcon, primaryCmd);
            }

            if (i == buttonDefinitions.length - 1) {
                button.setBackground(CalculatorGUI.isDarkMode ? darkEqualColor : dayEqualColor);
                button.addMouseListener(new EqualMouseHandler(button));
            } else if (i >= 16 && i <= 18 || i >= 21 && i <= 23 || i >= 26 && i <= 28 || i == 32) {
                button.setBackground(CalculatorGUI.isDarkMode ? darkNumberColor : dayNumberColor);
                button.addMouseListener(new NumberMouseHandler(button));
            } else {
                button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolColor : daySymbolColor);
                button.addMouseListener(new SymbolMouseHandler(button));
            }

            button.setBorder(null);
            button.setFocusPainted(false);
            if (primaryCmd.equals("2nd")) {
                button.addActionListener(e -> {
                    isSecondMode = !isSecondMode;
                    updateButtonsMode();
                });
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (!button.isEnabled()) return;
                        if (isSecondMode) {
                            button.setBackground(CalculatorGUI.isDarkMode ? darkEqualClickColor : dayEqualClickColor);
                        } else {
                            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolClickColor : daySymbolClickColor);
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (!button.isEnabled()) return;
                        if (isSecondMode) {
                            button.setBackground(CalculatorGUI.isDarkMode ? darkEqualHoverColor : dayEqualHoverColor);
                        } else {
                            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolHoverColor : daySymbolHoverColor);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!button.isEnabled()) return;
                        if (isSecondMode) {
                            button.setBackground(CalculatorGUI.isDarkMode ? darkEqualHoverColor : dayEqualHoverColor);
                        } else {
                            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolHoverColor : daySymbolHoverColor);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!button.isEnabled()) return;
                        if (isSecondMode) {
                            button.setBackground(CalculatorGUI.isDarkMode ? darkEqualColor : dayEqualColor);
                        } else {
                            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolColor : daySymbolColor);
                        }
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
        isHistoryShow = isVisible;
        if (!isVisible) {
            controlPanel.onHistoryPanelHide();
        }
    }

    //region Handler
    private class NumberMouseHandler extends MouseAdapter {
        private final JButton button;

        public NumberMouseHandler(JButton button) {
            this.button = button;
            button.setContentAreaFilled(false);  // 取消默认背景填充（包括点击变色）
            button.setBorderPainted(false);      // 取消默认边框绘制
            button.setOpaque(true);              // 允许自定义背景色（必须设为不透明）
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkNumberClickColor : dayNumberClickColor);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkNumberHoverColor : dayNumberHoverColor);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkNumberHoverColor : dayNumberHoverColor);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkNumberColor : dayNumberColor);
        }
    }

    private class SymbolMouseHandler extends MouseAdapter {
        private final JButton button;

        public SymbolMouseHandler(JButton button) {
            this.button = button;
            button.setContentAreaFilled(false);  // 取消默认背景填充（包括点击变色）
            button.setBorderPainted(false);      // 取消默认边框绘制
            button.setOpaque(true);              // 允许自定义背景色（必须设为不透明）
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolClickColor : daySymbolClickColor);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolHoverColor : daySymbolHoverColor);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolHoverColor : daySymbolHoverColor);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolColor : daySymbolColor);
        }
    }

    private class EqualMouseHandler extends MouseAdapter {
        private final JButton button;

        public EqualMouseHandler(JButton button) {
            this.button = button;
            button.setContentAreaFilled(false);  // 取消默认背景填充（包括点击变色）
            button.setBorderPainted(false);      // 取消默认边框绘制
            button.setOpaque(true);              // 允许自定义背景色（必须设为不透明）
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkEqualClickColor : dayEqualClickColor);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkEqualHoverColor : dayEqualHoverColor);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkEqualHoverColor : dayEqualHoverColor);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkEqualColor : dayEqualColor);
        }
    }

    private class FunctionMouseHandler extends MouseAdapter {
        private final JButton button;

        public FunctionMouseHandler(JButton button) {
            this.button = button;
            button.setContentAreaFilled(false);  // 取消默认背景填充（包括点击变色）
            button.setBorderPainted(false);      // 取消默认边框绘制
            button.setOpaque(true);              // 允许自定义背景色（必须设为不透明）
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkFunctionClick : dayFunctionClick);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkFunctionHoverColor : dayFunctionHoverColor);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkFunctionHoverColor : dayFunctionHoverColor);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
        }
    }

    private class SwitchMouseHandler extends MouseAdapter {
        private final JButton button;
        private boolean enable = false;

        public SwitchMouseHandler(JButton button) {
            this.button = button;
            button.setContentAreaFilled(false);  // 取消默认背景填充（包括点击变色）
            button.setBorderPainted(false);      // 取消默认边框绘制
            button.setOpaque(true);              // 允许自定义背景色（必须设为不透明）
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!button.isEnabled()) return;
            enable = !enable;
            System.out.println(enable);
            if (enable) {
                button.setBackground(CalculatorGUI.isDarkMode ? darkEqualColor : dayEqualColor);
            } else {
                button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolColor : daySymbolColor);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!button.isEnabled() || enable) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolClickColor : daySymbolClickColor);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!button.isEnabled() || enable) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolHoverColor : daySymbolHoverColor);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.isEnabled() || enable) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkSymbolHoverColor : daySymbolHoverColor);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.isEnabled() || enable) return;
            button.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
        }
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            switch (cmd) {
                case "=":
                    String expression = inputExpression.toString();
                    if (expression.contains("x")) {
                        if (expression.contains("=")) {
                            String result = evaluator.solveEquation(expression);
                            display.setText(result);
                            inputExpression.setLength(0);
                            inputExpression.append(result);
                        } else {
                            inputExpression.append("==");
                            display.setText(inputExpression.toString());
                        }
                    } else {
                        try {
                            double result = evaluator.evaluate(inputExpression.toString());

                            // 添加到历史记录
                            String historyEntry = String.format("[%tT] %s = %.4f\n",
                                    new Date(), expression, result);
                            topPanel.addCalHistory(historyEntry);

                            display.setText(String.valueOf(result));
                            inputExpression.setLength(0);
                            inputExpression.append(result);
                        } catch (StackOverflowError | ArithmeticException ex) {
                            display.setText("OVERFLOW");
                            inputExpression.setLength(0);
                        } catch (Exception ex) {
                            display.setText("ERROR");
                            inputExpression.setLength(0);
                        }
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
                    inputExpression.append("lg(");
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
                    inputExpression.append(" ^(1/3)");
                    display.setText(inputExpression.toString());
                    break;

                case "yroot":
                    inputExpression.append("^(1/");
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

                case "x":
                    inputExpression.append("x");
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
                        expression = inputExpression.toString();
                        String derivative = evaluator.derivativeWithSymja(expression);
                        inputExpression.setLength(0);
                        inputExpression.append(derivative);
                        display.setText(derivative);
                    } catch (Exception ex) {
                        display.setText("CANNOT DIFF");
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
    //endregion


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
            setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
            for (int i = 0; i < 6; i++) {
                String text = buttons[i];
                JButton button = new JButton(text);
                button.setFocusPainted(false);
                button.setFont(new Font("Arial", Font.PLAIN, 15));
                button.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
                button.setForeground(CalculatorGUI.isDarkMode ? darkTextColor : dayTextColor);
                button.setBorder(BorderFactory.createEmptyBorder());
                switch (i) {
                    case 0:
                        button.addActionListener(_ -> memoryClear());
                        button.addMouseListener(new FunctionMouseHandler(button));
                        MCButton = button;
                        break;
                    case 1:
                        button.addActionListener(_ -> memoryRecall());
                        button.addMouseListener(new FunctionMouseHandler(button));
                        MRButton = button;
                        break;
                    case 2:
                        button.addActionListener(_ -> memoryAdd());
                        button.addMouseListener(new FunctionMouseHandler(button));
                        break;
                    case 3:
                        button.addActionListener(_ -> memorySubtract());
                        button.addMouseListener(new FunctionMouseHandler(button));
                        break;
                    case 4:
                        button.addActionListener(_ -> memoryStore());
                        button.addMouseListener(new FunctionMouseHandler(button));
                        break;
                    case 5:
                        button.addActionListener(_ -> historyButton());
                        button.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                if (!button.isEnabled()) return;
                                if (isHistoryShow) {
                                    button.setBackground(CalculatorGUI.isDarkMode ? darkEqualClickColor : dayEqualClickColor);
                                } else {
                                    button.setBackground(CalculatorGUI.isDarkMode ? darkFunctionClick : dayFunctionClick);
                                }
                            }

                            @Override
                            public void mouseReleased(MouseEvent e) {
                                if (!button.isEnabled()) return;
                                if (isHistoryShow) {
                                    button.setBackground(CalculatorGUI.isDarkMode ? darkEqualHoverColor : dayEqualHoverColor);
                                } else {
                                    button.setBackground(CalculatorGUI.isDarkMode ? darkFunctionHoverColor : dayFunctionHoverColor);
                                }
                            }

                            @Override
                            public void mouseEntered(MouseEvent e) {
                                if (!button.isEnabled()) return;
                                if (isHistoryShow) {
                                    button.setBackground(CalculatorGUI.isDarkMode ? darkEqualHoverColor : dayEqualHoverColor);
                                } else {
                                    button.setBackground(CalculatorGUI.isDarkMode ? darkFunctionHoverColor : dayFunctionHoverColor);
                                }
                            }

                            @Override
                            public void mouseExited(MouseEvent e) {
                                if (!button.isEnabled()) return;
                                if (isHistoryShow) {
                                    button.setBackground(CalculatorGUI.isDarkMode ? darkEqualColor : dayEqualColor);
                                } else {
                                    button.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
                                }
                            }
                        });
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
                    display.setText("ERROR"); // 如果解析失败，显示错误
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
                    display.setText("ERROR"); // 如果解析失败，显示错误
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
                    display.setText("ERROR"); // 如果解析失败，显示错误
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

        public void onHistoryPanelHide() {
            historyButton.setBackground(CalculatorGUI.isDarkMode ? darkBgColor : dayBgColor);
        }
    }
}