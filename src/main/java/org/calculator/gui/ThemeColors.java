package org.calculator.gui;

import java.awt.*;

public class ThemeColors {
    private static final Color dayLightBgColor = new Color(243, 243, 243);
    private static final Color darkLightBgColor = new Color(20, 20, 20);

    public static Color getLightBgColor() {
        return CalculatorGUI.isDarkMode ? darkLightBgColor : dayLightBgColor;
    }

    private static final Color dayDarkBgColor = new Color(238, 238, 238);
    private static final Color darkDarkBgColor = new Color(40, 40, 40);

    public static Color getDarkBgColor() {
        return CalculatorGUI.isDarkMode ? darkDarkBgColor : dayDarkBgColor;
    }

    private static final Color dayTotalBgColor = Color.white;
    private static final Color darkTotalBgColor = Color.black;

    public static Color getTotalBgColor() {
        return CalculatorGUI.isDarkMode ? darkTotalBgColor : dayTotalBgColor;
    }

    private static final Color daySymbolColor = new Color(249, 249, 249);
    private static final Color darkSymbolColor = new Color(32, 32, 32);

    public static Color getSymbolColor() {
        return CalculatorGUI.isDarkMode ? darkSymbolColor : daySymbolColor;
    }

    private static final Color dayNumberColor = Color.white;
    private static final Color darkNumberColor = new Color(59, 59, 59);

    public static Color getNumberColor() {
        return CalculatorGUI.isDarkMode ? darkNumberColor : dayNumberColor;
    }

    private static final Color dayTextColor = Color.black;
    private static final Color darkTextColor = Color.lightGray;

    public static Color getTextColor() {
        return CalculatorGUI.isDarkMode ? darkTextColor : dayTextColor;
    }

    public static Color getTextColor(boolean swap) {
        if (swap) return CalculatorGUI.isDarkMode ? dayTextColor : darkTextColor;
        else return getTextColor();
    }

    private static final Color dayEqualColor = new Color(0, 67, 192);
    private static final Color darkEqualColor = new Color(76, 194, 255);

    public static Color getEqualColor() {
        return CalculatorGUI.isDarkMode ? darkEqualColor : dayEqualColor;
    }

    private static final Color dayNumberHoverColor = new Color(252, 252, 252);
    private static final Color darkNumberHoverColor = new Color(32, 32, 32);

    public static Color getNumberHoverColor() {
        return CalculatorGUI.isDarkMode ? darkNumberHoverColor : dayNumberHoverColor;
    }

    private static final Color daySymbolHoverColor = new Color(246, 246, 246);
    private static final Color darkSymbolHoverColor = new Color(60, 60, 60);

    public static Color getSymbolHoverColor() {
        return CalculatorGUI.isDarkMode ? darkSymbolHoverColor : daySymbolHoverColor;
    }

    private static final Color dayNumberClickColor = new Color(249, 249, 251);
    private static final Color darkNumberClickColor = new Color(28, 28, 28);

    public static Color getNumberClickColor() {
        return CalculatorGUI.isDarkMode ? darkNumberClickColor : dayNumberClickColor;
    }

    private static final Color daySymbolClickColor = new Color(244, 244, 246);
    private static final Color darkSymbolClickColor = new Color(32, 32, 32);

    public static Color getSymbolClickColor() {
        return CalculatorGUI.isDarkMode ? darkSymbolClickColor : daySymbolClickColor;
    }

    private static final Color dayEqualHoverColor = new Color(19, 75, 197);
    private static final Color darkEqualHoverColor = new Color(47, 177, 232);

    public static Color getEqualHoverColor() {
        return CalculatorGUI.isDarkMode ? darkEqualHoverColor : dayEqualHoverColor;
    }

    private static final Color dayEqualClickColor = new Color(30, 83, 202);
    private static final Color darkEqualClickColor = new Color(42, 17, 211);

    public static Color getEqualClickColor() {
        return CalculatorGUI.isDarkMode ? darkEqualClickColor : dayEqualClickColor;
    }

    private static final Color dayFunctionHoverColor = new Color(234, 234, 234);
    private static final Color darkFunctionHoverColor = new Color(45, 45, 45);

    public static Color getFunctionHoverColor() {
        return CalculatorGUI.isDarkMode ? darkFunctionHoverColor : dayFunctionHoverColor;
    }

    private static final Color dayFunctionClickColor = new Color(236, 237, 238);
    private static final Color darkFunctionClickColor = new Color(29, 29, 42);

    public static Color getFunctionClickColor() {
        return CalculatorGUI.isDarkMode ? darkFunctionClickColor : dayFunctionClickColor;
    }

    private static final Color dayLightContentColor = new Color(255, 255, 255);
    private static final Color darkLightContentColor = new Color(10, 10, 10);

    public static Color getLightContentColor() {
        return CalculatorGUI.isDarkMode ? darkLightContentColor : dayLightContentColor;
    }

    private static final Color dayDarkContentColor = new Color(238, 238, 238);
    private static final Color darkDarkContentColor = new Color(40, 40, 40);

    public static Color getDarkContentColor() {
        return CalculatorGUI.isDarkMode ? darkDarkContentColor : dayDarkContentColor;
    }

    private static final Color dayDataColor = Color.BLACK;
    private static final Color darkDataColor = Color.WHITE;

    public static Color getDataColor() {
        return CalculatorGUI.isDarkMode ? darkDataColor : dayDataColor;
    }

    private static final Color daySplitColor = Color.darkGray;
    private static final Color darkSplitColor = Color.lightGray;

    public static Color getSplitColor() {
        return CalculatorGUI.isDarkMode ? darkSplitColor : daySplitColor;
    }

    private static final Color dayTooltipBgColor = new Color(255, 255, 200);
    private static final Color darkTooltipBgColor = new Color(25, 25, 25);

    public static Color getTooltipBgColor() {
        return CalculatorGUI.isDarkMode ? darkTooltipBgColor : dayTooltipBgColor;
    }

    private static final Color dayTooltipBorderColor = Color.darkGray;
    private static final Color darkTooltipBorderColor = Color.lightGray;

    public static Color getTooltipBorderColor() {
        return CalculatorGUI.isDarkMode ? darkTooltipBorderColor : dayTooltipBorderColor;
    }

    private static final Color dayAxisColor = Color.black;
    private static final Color darkAxisColor = Color.lightGray;

    public static Color getAxisColor() {
        return CalculatorGUI.isDarkMode ? darkAxisColor : dayAxisColor;
    }

    private static final Color dayPointColor = new Color(33, 33, 33);
    private static final Color darkPointColor = new Color(230, 230, 230);

    public static Color getPointColor() {
        return CalculatorGUI.isDarkMode ? darkPointColor : dayPointColor;
    }

    private static final Color dayLineColor = new Color(70, 130, 255);
    private static final Color darkLineColor = new Color(70, 130, 255);

    public static Color getLineColor() {
        return CalculatorGUI.isDarkMode ? darkLineColor : dayLineColor;
    }

    private static final Color dayMajorGridColor = new Color(220, 220, 220);
    private static final Color darkMajorGridColor = new Color(40, 40, 40);

    public static Color getMajorGridColor() {
        return CalculatorGUI.isDarkMode ? darkMajorGridColor : dayMajorGridColor;
    }

    private static final Color dayMinorGridColor = new Color(240, 240, 240);
    private static final Color darkMinorGridColor = new Color(20, 20, 20);

    public static Color getMinorGridColor() {
        return CalculatorGUI.isDarkMode ? darkMinorGridColor : dayMinorGridColor;
    }

    public static Color getRandomColor() {
        // 生成随机颜色，但避免太浅的颜色
        return new Color(
                (int) (Math.random() * 200),
                (int) (Math.random() * 200),
                (int) (Math.random() * 200)
        );
    }
}
