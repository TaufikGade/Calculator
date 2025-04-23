package org.calculator.gui.plotting;

import org.calculator.gui.ThemeColors;
import org.calculator.gui.regression.ChartPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GraphPanel extends JPanel {
    private double xMin = -15;
    private double xMax = 15;
    private double yMin = -15;
    private double yMax = 15;
    private Point dragStart;
    private final PlottingPanel topPanel;
    //private List<PlottingPanel.FunctionData> functions;
    private PlottingPanel.FunctionData function;
    private JButton dataButton;

    public GraphPanel(PlottingPanel top) {
        this.topPanel = top;
        setBackground(ThemeColors.getTotalBgColor());
        setLayout(new BorderLayout());

        try {
            // 从类路径加载图片
            InputStream imgStream = ChartPanel.class.getResourceAsStream("/images/123pic.png");
            if (imgStream == null) {
                throw new IOException("图片资源未找到！");
            }
            BufferedImage originalImage = ImageIO.read(imgStream);

            // 调整大小并创建图标
            Image scaledImage = originalImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);

            dataButton = new JButton(icon) {
                // 定义按钮形状为圆形
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // 绘制圆形背景
                    if (getModel().isArmed()) {
                        g2.setColor(Color.darkGray); // 按下时颜色
                    } else {
                        g2.setColor(Color.lightGray);
                    }
                    int radius = Math.min(getWidth(), getHeight());
                    g2.fillOval(0, 0, radius, radius);

                    // 绘制文字
                    super.paintComponent(g2);
                    g2.dispose();
                }
                @Override
                public boolean contains(int x, int y) {
                    return new Ellipse2D.Float(0, 0, getWidth(), getHeight()).contains(x, y);
                }
            };
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }

        // 设置按钮属性
        dataButton.setOpaque(false);
        dataButton.setContentAreaFilled(false);
        dataButton.setBorderPainted(false);
        dataButton.setFocusPainted(false);

        // 添加悬停效果
        dataButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                dataButton.setBackground(ThemeColors.getFunctionHoverColor()); // 悬停颜色
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dataButton.setBackground(ThemeColors.getDarkBgColor()); // 恢复默认颜色
            }
        });

        dataButton.addActionListener(_ -> topPanel.switchInputPanelState(true));

        this.add(dataButton, BorderLayout.PAGE_END);

        // Mouse event handlers for panning and zooming
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;

                    double xRange = xMax - xMin;
                    double yRange = yMax - yMin;

                    double xDelta = -dx * xRange / getWidth();
                    double yDelta = dy * yRange / getHeight();

                    xMin += xDelta;
                    xMax += xDelta;
                    yMin += yDelta;
                    yMax += yDelta;

                    dragStart = e.getPoint();
                    repaint();
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // Get mouse position in graph coordinates
                double mouseX = xMin + e.getX() * (xMax - xMin) / getWidth();
                double mouseY = yMax - e.getY() * (yMax - yMin) / getHeight();

                // Zoom factor
                double factor = e.getWheelRotation() < 0 ? 0.9 : 1.1;

                // Zoom centered on mouse position
                xMin = mouseX - (mouseX - xMin) * factor;
                xMax = mouseX + (xMax - mouseX) * factor;
                yMin = mouseY - (mouseY - yMin) * factor;
                yMax = mouseY + (yMax - mouseY) * factor;

                repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
    }

    public void setFunction(PlottingPanel.FunctionData function) {
        this.function = function;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        dataButton.setBounds(this.getWidth() - 60, this.getHeight() - 60, 40, 40);

        int width = getWidth();
        int height = getHeight();

        // Draw grid and axes
        drawGrid(g2, width, height);

        if (function != null) {
            drawFunction(g2, width, height);
        }
    }

    private void drawGrid(Graphics2D g2, int width, int height) {
        // Calculate appropriate grid spacing
        double xMajorSpacing = calculateMajorGridSpacing(xMax - xMin);
        double yMajorSpacing = calculateMajorGridSpacing(yMax - yMin);
        double xMinorSpacing = xMajorSpacing / 5;
        double yMinorSpacing = yMajorSpacing / 5;

        // Draw minor grid lines
        g2.setColor(ThemeColors.getMinorGridColor());
        g2.setStroke(new BasicStroke(0.5f));

        // X minor grid lines
        for (double x = Math.floor(xMin / xMinorSpacing) * xMinorSpacing; x <= xMax; x += xMinorSpacing) {
            if (Math.abs(x % xMajorSpacing) < 0.001) continue; // Skip major grid positions
            int screenX = mapX(x, width);
            g2.drawLine(screenX, 0, screenX, height);
        }

        // Y minor grid lines
        for (double y = Math.floor(yMin / yMinorSpacing) * yMinorSpacing; y <= yMax; y += yMinorSpacing) {
            if (Math.abs(y % yMajorSpacing) < 0.001) continue; // Skip major grid positions
            int screenY = mapY(y, height);
            g2.drawLine(0, screenY, width, screenY);
        }

        // Draw major grid lines
        g2.setColor(ThemeColors.getMajorGridColor());
        g2.setStroke(new BasicStroke(1.0f));

        // X major grid lines
        for (double x = Math.floor(xMin / xMajorSpacing) * xMajorSpacing; x <= xMax; x += xMajorSpacing) {
            int screenX = mapX(x, width);
            g2.drawLine(screenX, 0, screenX, height);
        }

        // Y major grid lines
        for (double y = Math.floor(yMin / yMajorSpacing) * yMajorSpacing; y <= yMax; y += yMajorSpacing) {
            int screenY = mapY(y, height);
            g2.drawLine(0, screenY, width, screenY);
        }

        // Draw axes
        g2.setColor(ThemeColors.getAxisColor());
        g2.setStroke(new BasicStroke(1.5f));

        int x0 = mapX(0, width);
        int y0 = mapY(0, height);

        // X-axis
        if (yMin <= 0 && yMax >= 0) {
            g2.drawLine(0, y0, width, y0);
        }

        // Y-axis
        if (xMin <= 0 && xMax >= 0) {
            g2.drawLine(x0, 0, x0, height);
        }

        // Draw tick marks and labels
        drawTickMarksAndLabels(g2, width, height, xMajorSpacing, yMajorSpacing);
    }

    private void drawTickMarksAndLabels(Graphics2D g2, int width, int height,
                                        double xSpacing, double ySpacing) {
        // Prepare font for labels
        g2.setColor(ThemeColors.getAxisColor());
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 10);
        g2.setFont(labelFont);
        FontMetrics metrics = g2.getFontMetrics();

        // Get origin coordinates
        int x0 = mapX(0, width);
        int y0 = mapY(0, height);

        // Calculate tick length
        int tickLength = 5;

        // Draw X-axis tick marks and labels
        for (double x = Math.floor(xMin / xSpacing) * xSpacing; x <= xMax; x += xSpacing) {
            if (Math.abs(x) < 0.001) continue; // Skip origin

            int screenX = mapX(x, width);

            // Draw tick mark
            if (yMin <= 0 && yMax >= 0) {
                // If y-axis is visible, draw ticks on it
                g2.drawLine(screenX, y0 - tickLength, screenX, y0 + tickLength);
            } else {
                // Otherwise draw at bottom
                g2.drawLine(screenX, height - tickLength, screenX, height);
            }

            // Format label
            String label = formatCoordinate(x);
            int labelWidth = metrics.stringWidth(label);

            // Position label
            int labelY;
            if (yMin <= 0 && yMax >= 0) {
                labelY = y0 + tickLength + metrics.getHeight();
            } else {
                labelY = height - tickLength - 2;
            }

            // Draw label
            g2.drawString(label, screenX - labelWidth / 2, labelY);
        }

        // Draw Y-axis tick marks and labels
        for (double y = Math.floor(yMin / ySpacing) * ySpacing; y <= yMax; y += ySpacing) {
            if (Math.abs(y) < 0.001) continue; // Skip origin

            int screenY = mapY(y, height);

            // Draw tick mark
            if (xMin <= 0 && xMax >= 0) {
                // If x-axis is visible, draw ticks on it
                g2.drawLine(x0 - tickLength, screenY, x0 + tickLength, screenY);
            } else {
                // Otherwise draw at left edge
                g2.drawLine(0, screenY, tickLength, screenY);
            }

            // Format label
            String label = formatCoordinate(y);
            int labelWidth = metrics.stringWidth(label);

            // Position label
            int labelX;
            if (xMin <= 0 && xMax >= 0) {
                labelX = x0 - tickLength - labelWidth - 2;
            } else {
                labelX = tickLength + 2;
            }

            // Draw label (centered vertically on the y-coordinate)
            g2.drawString(label, labelX, screenY + metrics.getAscent() / 2 - 1);
        }

        // Draw origin label (0,0)
        if (xMin <= 0 && xMax >= 0 && yMin <= 0 && yMax >= 0) {
            g2.drawString("0", x0 - metrics.stringWidth("0") - 2, y0 + metrics.getHeight());
        }

        // Axis labels
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // X-axis label
        g2.drawString("x", width - 15, y0 - 10);

        // Y-axis label
        g2.drawString("y", x0 + 10, 15);
    }

    private String formatCoordinate(double value) {
        // Format numbers to avoid scientific notation and unnecessary decimal places
        if (Math.abs(value) < 0.0001) {
            return "0";
        }

        if (Math.abs(value - Math.round(value)) < 0.001) {
            return String.format("%.0f", value);
        }

        return String.format("%.1f", value);
    }

    private double calculateMajorGridSpacing(double range) {
        // Calculate appropriate grid spacing based on current view range
        double raw = range / 10; // Aim for about 10 major divisions
        double exponent = Math.floor(Math.log10(raw));
        double fraction = raw / Math.pow(10, exponent);

        if (fraction < 1.5) return Math.pow(10, exponent);
        if (fraction < 3.5) return 2 * Math.pow(10, exponent);
        return 5 * Math.pow(10, exponent);
    }

    private void drawFunction(Graphics2D g2, int width, int height) {
        g2.setColor(function.getColor());
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        Path2D path = new Path2D.Double();
        boolean started = false;
        Double lastY = null;

        // Calculate points with adaptive sampling
        for (int screenX = 0; screenX < width; screenX++) {
            double x = xMin + screenX * (xMax - xMin) / width;

            try {
                double y = evaluateFunction(function.getExpression(), x);

                // Skip points outside the visible area
                if (Double.isNaN(y) || Double.isInfinite(y) || y < yMin || y > yMax) {
                    started = false;
                    continue;
                }

                int screenY = mapY(y, height);

                // Detect discontinuities
                if (lastY != null && Math.abs(y - lastY) > (yMax - yMin) / 10) {
                    started = false;
                }

                if (!started) {
                    path.moveTo(screenX, screenY);
                    started = true;
                } else {
                    path.lineTo(screenX, screenY);
                }

                lastY = y;
            } catch (Exception e) {
                started = false;
            }
        }

        g2.draw(path);
    }

    private double evaluateFunction(String function, double x) {
        try {
            // 使用\\b表示单词边界，确保只替换独立的x，不替换函数名中的x
            String expression = function.replaceAll("\\bx\\b", "(" + x + ")");
            return topPanel.getEvaluator().evaluate(expression);
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private int mapX(double x, int width) {
        return (int) ((x - xMin) / (xMax - xMin) * width);
    }

    private int mapY(double y, int height) {
        return height - (int) ((y - yMin) * height / (yMax - yMin));
    }
}