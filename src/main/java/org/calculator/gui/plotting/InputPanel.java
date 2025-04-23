package org.calculator.gui.plotting;

import org.calculator.gui.ThemeColors;
import org.calculator.math.MathEvaluator;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class InputPanel extends JPanel {
    private final PlottingPanel topPanel;
    private final JTextField display;
    private final StringBuilder inputExpression;
    private final MathEvaluator evaluator;
    private final Map<String, InputPanel.FunctionButton> functionButtons;
    private final InputPanel.ButtonHandler buttonHandler;
    // 添加三角函数弹出菜单
    private JPopupMenu trigPopupMenu;
    private JButton trigButton;
    private Font customFont;
    private boolean isSecondMode = false;

    public InputPanel(PlottingPanel top) {
        this.topPanel = top;
        setBackground(ThemeColors.getLightBgColor());
        setBorder(BorderFactory.createEmptyBorder());

        this.evaluator = new MathEvaluator();
        this.functionButtons = new HashMap<>();
        buttonHandler = new InputPanel.ButtonHandler();
        //setLayout(new BorderLayout());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        display = new JTextField();
        display.setForeground(ThemeColors.getTextColor());
        display.setBackground(ThemeColors.getTotalBgColor());
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        display.setEditable(false);

        try {
            InputStream fontStream = InputPanel.class.getResourceAsStream("/fonts/JetBrainsMono-Bold.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(30f);
            display.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "字体加载失败！");
        }
        // 显示屏
        JPanel WritePanel = createInputPanel();
        //add(WritePanel, BorderLayout.NORTH);
        add(WritePanel);

        inputExpression = new StringBuilder();

        // 创建顶部面板，包含特殊功能按钮（如三角函数按钮）
        JPanel topFunctionsPanel = createTopFunctionsPanel();

        // 下方符号面板
        JPanel symbolPanel = createSymbolPanel();
        // 创建中央面板来容纳北部面板和符号面板
        JPanel centralPanel = new JPanel(new BorderLayout());
        centralPanel.add(topFunctionsPanel, BorderLayout.NORTH);
        centralPanel.add(symbolPanel, BorderLayout.CENTER);

        add(centralPanel);
    }

    public InputPanel(PlottingPanel top, String function) {
        this.topPanel = top;
        setBackground(ThemeColors.getLightBgColor());
        setBorder(BorderFactory.createEmptyBorder());

        this.evaluator = new MathEvaluator();
        this.functionButtons = new HashMap<>();
        buttonHandler = new InputPanel.ButtonHandler();
        //setLayout(new BorderLayout());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        display = new JTextField(function);
        display.setForeground(ThemeColors.getTextColor());
        display.setBackground(ThemeColors.getTotalBgColor());
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        display.setEditable(false);

        try {
            InputStream fontStream = InputPanel.class.getResourceAsStream("/fonts/JetBrainsMono-Bold.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(30f);
            display.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "字体加载失败！");
        }
        // 显示屏
        JPanel WritePanel = createInputPanel();
        //add(WritePanel, BorderLayout.NORTH);
        add(WritePanel);

        inputExpression = new StringBuilder(function);

        // 创建顶部面板，包含特殊功能按钮（如三角函数按钮）
        JPanel topFunctionsPanel = createTopFunctionsPanel();

        // 下方符号面板
        JPanel symbolPanel = createSymbolPanel();
        // 创建中央面板来容纳北部面板和符号面板
        JPanel centralPanel = new JPanel(new BorderLayout());
        centralPanel.add(topFunctionsPanel, BorderLayout.NORTH);
        centralPanel.add(symbolPanel, BorderLayout.CENTER);

        add(centralPanel);
    }

    private JPanel createTopFunctionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.getLightBgColor());

        JPanel leftArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftArea.setBackground(ThemeColors.getLightBgColor());

        Font yahei = new Font("Microsoft YaHei", Font.PLAIN, 16);

        // 创建三角学按钮
        trigButton = new JButton("三角学 ▼");
        trigButton.setFont(yahei);
        trigButton.setBorder(BorderFactory.createLineBorder(ThemeColors.getLightBgColor()));
        trigButton.setFocusPainted(false);
        trigButton.setForeground(ThemeColors.getTextColor());
        trigButton.setBackground(ThemeColors.getLightBgColor());
        trigButton.addMouseListener(new FunctionMouseHandler(trigButton));

        // 创建三角函数弹出菜单
        trigPopupMenu = createTrigPopupMenu();

        // 添加三角学按钮点击事件
        trigButton.addActionListener(_ -> {
            // 显示弹出菜单在按钮下方
            trigPopupMenu.show(trigButton, 0, trigButton.getHeight());
        });
        leftArea.add(trigButton);

        // 添加求导按钮
        JButton derivativeButton = new JButton("求导");
        derivativeButton.setFont(yahei);
        derivativeButton.setBorder(BorderFactory.createLineBorder(ThemeColors.getLightBgColor()));
        derivativeButton.setFocusPainted(false);
        derivativeButton.setForeground(ThemeColors.getTextColor());
        derivativeButton.setBackground(ThemeColors.getLightBgColor());
        derivativeButton.addActionListener(buttonHandler);
        derivativeButton.addMouseListener(new FunctionMouseHandler(derivativeButton));
        leftArea.add(derivativeButton);

        JButton hideButton = new JButton("隐藏输入区域");
        hideButton.setFont(yahei);
        hideButton.setBorder(BorderFactory.createLineBorder(ThemeColors.getLightBgColor()));
        hideButton.setFocusPainted(false);
        hideButton.setForeground(ThemeColors.getTextColor());
        hideButton.setBackground(ThemeColors.getLightBgColor());
        hideButton.addActionListener(_ -> topPanel.switchInputPanelState(false));
        hideButton.addMouseListener(new FunctionMouseHandler(hideButton));

        panel.add(leftArea, BorderLayout.WEST);
        panel.add(hideButton, BorderLayout.EAST);

        return panel;
    }

    private JPopupMenu createTrigPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(ThemeColors.getLightBgColor());

        // 创建包含三角函数按钮的面板
        JPanel trigPanel = new JPanel(new GridLayout(2, 3, 2, 2));
        trigPanel.setBackground(ThemeColors.getLightBgColor());

        // 添加三角函数按钮
        String[] showText = {"sin", "cos", "tan", "asin", "acos", "atan"};
        String[] latexText = {"\\sin", "\\cos", "\\tan", "\\arcsin", "\\arccos", "\\arctan"};

        // 添加三角函数
        for (int i = 0; i < 6; i++) {
            TeXFormula formula = new TeXFormula(latexText[i]);
            Icon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 17, TeXFormula.SERIF, ThemeColors.getTextColor());
            JButton button = new JButton(icon);
            button.setBackground(ThemeColors.getLightBgColor());
            button.setBorder(null);
            button.setFocusPainted(false);
            button.setActionCommand(showText[i]);
            button.addActionListener(buttonHandler);
            button.addMouseListener(new SymbolMouseHandler(button));
            trigPanel.add(button);
        }

        menu.add(trigPanel);
        return menu;
    }

    private JPanel createSymbolPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 5, 5, 5));
        panel.setBackground(ThemeColors.getLightBgColor());
        panel.setPreferredSize(new Dimension(500, 800));
        panel.setMaximumSize(new Dimension(10000, 10800));

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

            // LaTeX表达式颜色
            TeXFormula primaryFormula = new TeXFormula(primaryLatex), secondaryFormula = new TeXFormula(secondaryLatex);
            Icon primaryIcon, secondaryIcon;

            if (i == buttonDefinitions.length - 1) {
                //等于号的字体颜色与其他的相反
                primaryIcon = primaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20,
                        TeXFormula.SERIF, ThemeColors.getTextColor(true));

                secondaryIcon = secondaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20,
                        TeXFormula.SERIF, ThemeColors.getTextColor(true));
            } else {
                primaryIcon = primaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20,
                        TeXFormula.SERIF, ThemeColors.getTextColor());

                secondaryIcon = secondaryFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20,
                        TeXFormula.SERIF, ThemeColors.getTextColor());
            }

            FunctionButton button;
            if (buttonDefinitions[i].length > 2) {
                button = new FunctionButton(primaryIcon, secondaryIcon, primaryCmd, secondaryCmd);
            } else {
                button = new FunctionButton(primaryIcon, primaryCmd);
            }

            if (i == buttonDefinitions.length - 1) {
                button.setBackground(ThemeColors.getEqualColor());
                button.addMouseListener(new EqualMouseHandler(button));
            } else if (i >= 16 && i <= 18 || i >= 21 && i <= 23 || i >= 26 && i <= 28 || i == 32) {
                button.setBackground(ThemeColors.getNumberColor());
                button.addMouseListener(new NumberMouseHandler(button));
            } else {
                button.setBackground(ThemeColors.getSymbolColor());
                button.addMouseListener(new SymbolMouseHandler(button));
            }

            button.setBorder(null);
            button.setFocusPainted(false);
            if (primaryCmd.equals("2nd")) {
                button.addActionListener(_ -> {
                    isSecondMode = !isSecondMode;
                    updateButtonsMode();
                });
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (!button.isEnabled()) return;
                        TeXFormula formula = new TeXFormula("2^{nd}");

                        Icon newIcon;
                        if (isSecondMode) {
                            newIcon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20,
                                    TeXFormula.SERIF, ThemeColors.getTextColor());
                            button.setBackground(ThemeColors.getEqualClickColor());
                        } else {
                            newIcon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20,
                                    TeXFormula.SERIF, ThemeColors.getTextColor(true));

                            button.setBackground(ThemeColors.getSymbolClickColor());
                        }
                        button.setIcon(newIcon);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (!button.isEnabled()) return;
                        if (isSecondMode) {
                            button.setBackground(ThemeColors.getEqualHoverColor());
                        } else {
                            button.setBackground(ThemeColors.getSymbolHoverColor());
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!button.isEnabled()) return;
                        if (isSecondMode) {
                            button.setBackground(ThemeColors.getEqualHoverColor());
                        } else {
                            button.setBackground(ThemeColors.getSymbolHoverColor());
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!button.isEnabled()) return;
                        if (isSecondMode) {
                            button.setBackground(ThemeColors.getEqualColor());
                        } else {
                            button.setBackground(ThemeColors.getSymbolColor());
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
        for (InputPanel.FunctionButton button : functionButtons.values()) {
            button.setSecondMode(isSecondMode);
        }
    }

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
            button.setBackground(ThemeColors.getNumberClickColor());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getNumberHoverColor());
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getNumberHoverColor());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getNumberColor());
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
            button.setBackground(ThemeColors.getSymbolClickColor());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getSymbolHoverColor());
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getSymbolHoverColor());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getSymbolColor());
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
            button.setBackground(ThemeColors.getEqualClickColor());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getEqualHoverColor());
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getEqualHoverColor());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getEqualColor());
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
            button.setBackground(ThemeColors.getFunctionClickColor());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getFunctionHoverColor());
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getFunctionHoverColor());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.isEnabled()) return;
            button.setBackground(ThemeColors.getLightBgColor());
        }
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            switch (cmd) {
                case "=":
                    calExpression();
                    break;

                // 这里添加一个clear函数的命令
                case "C":
                    inputExpression.delete(0, inputExpression.length());
                    display.setText("");
                    // 清除所有绘制的函数
                    topPanel.clearFunctions();
                    break;
                case "⌫":
                    if (!inputExpression.isEmpty()) {
                        inputExpression.deleteCharAt(inputExpression.length() - 1);
                        display.setText(inputExpression.toString());
                    }
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
                    inputExpression.append("^(1/2)");
                    display.setText(inputExpression.toString());
                    break;

                case "cbrt":
                    inputExpression.append("^(1/3)");
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
                        String expression = inputExpression.toString();
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

    public void calExpression() {
        String expression = inputExpression.toString();
        try {
            double result = evaluator.evaluate(inputExpression.toString());
            display.setText(String.valueOf(result));
            inputExpression.setLength(0);
            inputExpression.append(result);
        } catch (StackOverflowError | Exception ex) {

        }
        topPanel.addFunction(expression);
    }


    private class FunctionButton extends JButton {
        private final Icon primaryIcon;
        private final Icon secondaryIcon;
        private final String primaryCommand;
        private final String secondaryCommand;
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

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(ThemeColors.getLightBgColor());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // 函数输入框，带有函数符号
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(ThemeColors.getTotalBgColor());

        JLabel functionSymbol = new JLabel("ƒ");
        functionSymbol.setFont(new Font("Times New Roman", Font.ITALIC, 20));

        functionSymbol.setForeground(ThemeColors.getTextColor());
        functionSymbol.setBackground(ThemeColors.getDarkBgColor());
        functionSymbol.setOpaque(true);
        functionSymbol.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        displayPanel.setForeground(ThemeColors.getTextColor());
        displayPanel.setBackground(ThemeColors.getTotalBgColor());

        displayPanel.add(functionSymbol, BorderLayout.WEST);
        displayPanel.add(display, BorderLayout.CENTER);

        inputPanel.add(displayPanel, BorderLayout.NORTH);

        return inputPanel;
    }

//    public static void main(String[] args) {
//            // 设置 Swing 应用的线程安全性
//            SwingUtilities.invokeLater(() -> {
//                // 创建一个 JFrame（主窗口）
//                JFrame frame = new JFrame("计算器输入面板");
//
//                // 设置关闭操作，退出程序
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//                // 创建一个 PlottingPanel 实例 (在你的代码中有定义这个类)
//                PlottingPanel plottingPanel = new PlottingPanel();
//
//                // 创建 InputPanel 实例并将 PlottingPanel 传递进去
//                InputPanel inputPanel = new InputPanel(plottingPanel);
//
//                // 将 InputPanel 添加到 JFrame 中
//                frame.getContentPane().add(inputPanel);
//
//                // 设置窗口的尺寸
//                frame.setSize(600, 800);
//
//                // 设置窗口为可见
//                frame.setVisible(true);
//            });
//        }
}

