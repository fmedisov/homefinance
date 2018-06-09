package ru.medisov.home_finance.console_ui;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UiConfig
{
    private static final String CONFIG_NAME = "config.properties";
    private static final Properties UI_CONFIG = new Properties();

    public static void initGlobalConfig() {
        initGlobalConfig(null);
    }

    public static void initGlobalConfig(String name) {
        try {
            if (name != null && !name.trim().isEmpty()) {
                UI_CONFIG.load(new FileReader(name));
            } else {
                try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(CONFIG_NAME)) {
                    if (inputStream != null) {
                        UI_CONFIG.load(inputStream);
                    } else {
                        throw new IOException("Properties file not found");
                    }
                } catch (IOException e) {
                    Logger.getLogger(UiConfig.class.getName()).log(Level.SEVERE, e.getMessage());
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(UiConfig.class.getName()).log(Level.SEVERE, e.getMessage());
        }
    }

    public static String getProperty(String property) {
        return UI_CONFIG.getProperty(property);
    }
}