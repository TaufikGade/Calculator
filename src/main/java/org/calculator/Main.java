package org.calculator;

import org.calculator.gui.CalculatorGUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String CONFIG_PATH = System.getProperty("user.home") + "/.嘉哥哥/config.property";

    public static void main(String[] args) {
        CalculatorGUI mainFrame = new CalculatorGUI();
        loadSavedProperty(mainFrame);
        mainFrame.init();
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveProperty(mainFrame);
            }
        });

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 定期检查黑暗模式状态
        scheduler.scheduleAtFixedRate(() -> {
            try {
                mainFrame.setDarkMode(isDarkMode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, 5, TimeUnit.SECONDS);  // 每5秒检查一次

    }

    private static void saveProperty(JFrame frame) {
        try {
            Properties props = new Properties();
            props.setProperty("position.x", String.valueOf(frame.getX()));
            props.setProperty("position.y", String.valueOf(frame.getY()));
            var dimension = frame.getSize();
            props.setProperty("size.width", String.valueOf(dimension.width));
            props.setProperty("size.height", String.valueOf(dimension.height));
            props.setProperty("darkMode", String.valueOf(CalculatorGUI.isDarkMode));

            // 保存到用户目录的隐藏文件
            new File(System.getProperty("user.home") + "/.嘉哥哥").mkdirs();
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
            CalculatorGUI.isDarkMode = Boolean.parseBoolean(props.getProperty("darkMode", "false"));

            // 设置窗口位置
            frame.setLocation(x, y);
            frame.setSize(width, height);
        } catch (Exception e) {
            // 文件不存在或格式错误时使用默认位置
            frame.setLocationRelativeTo(null); // 居中显示（备用方案）
            frame.setSize(600, 800);
        }
    }

    private static boolean isDarkMode() {
        try {
            Process process = Runtime.getRuntime().exec("reg query \"HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize\" /v AppsUseLightTheme");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("AppsUseLightTheme")) {
                    String[] parts = line.split("\\s+");
                    String value = parts[parts.length - 1];
                    if (Objects.equals(value, "0x0")) {
                        System.out.println("System is in Dark Mode");
                        return true;
                    } else {
                        System.out.println("System is in Light Mode");
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
