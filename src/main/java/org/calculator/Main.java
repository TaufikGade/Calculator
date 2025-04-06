package org.calculator;

import org.calculator.gui.CalculatorGUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Main {

    private static final String CONFIG_PATH = System.getProperty("user.home") + "/.jiage/config.property";

    public static void main(String[] args) {
        JFrame mainFrame = new CalculatorGUI();
        loadSavedProperty(mainFrame);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveProperty(mainFrame);
            }
        });
    }

    private static void saveProperty(JFrame frame) {
        try {
            Properties props = new Properties();
            props.setProperty("position.x", String.valueOf(frame.getX()));
            props.setProperty("position.y", String.valueOf(frame.getY()));
            var dimension = frame.getSize();
            props.setProperty("size.width", String.valueOf(dimension.width));
            props.setProperty("size.height", String.valueOf(dimension.height));

            // 保存到用户目录的隐藏文件
            new File(System.getProperty("user.home") + "/.jiage").mkdirs();
            props.store(new FileOutputStream(CONFIG_PATH), "Window Position");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void loadSavedProperty(JFrame frame) {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            props.load(fis);

            int x = Integer.parseInt(props.getProperty("position.x", "0"));
            int y = Integer.parseInt(props.getProperty("position.y", "0"));
            int width = Integer.parseInt(props.getProperty("size.width", "600"));
            int height = Integer.parseInt(props.getProperty("size.height", "800"));

            // 设置窗口位置
            frame.setLocation(x, y);
            frame.setSize(width, height);
        } catch (Exception e) {
            // 文件不存在或格式错误时使用默认位置
            frame.setLocationRelativeTo(null); // 居中显示（备用方案）
            frame.setSize(600, 800);
        }
    }
}
