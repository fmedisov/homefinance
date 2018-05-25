package ru.medisov.home_finance.config;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalConfig
{
    private static final String CONFIG_NAME = "resources\\config.properties";
    private static final Properties GLOBAL_CONFIG = new Properties();

    public static void initGlobalConfig() {
        initGlobalConfig(null);
    }

    public static void initGlobalConfig(String name) {
        try {
            if (name != null && !name.trim().isEmpty()) {
                GLOBAL_CONFIG.load(new FileReader(name));
            } else {
                try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(CONFIG_NAME)) {
                    if (inputStream != null) {
                        GLOBAL_CONFIG.load(inputStream);
                    } else {
                        throw new IOException("Properties file not found");
                    }
                } catch (IOException e) {
                    Logger.getLogger(GlobalConfig.class.getName()).log(Level.SEVERE, e.getMessage());
                }
            }
        } catch (IOException e) {
            Logger.getLogger(GlobalConfig.class.getName()).log(Level.SEVERE, e.getMessage());
        }
    }

    public static String getProperty(String property) {
        return GLOBAL_CONFIG.getProperty(property);
    }
}