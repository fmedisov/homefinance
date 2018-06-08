package ru.medisov.home_finance.dao;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoConfig
{
    private static final String CONFIG_NAME = "config.properties";
    private static final Properties DAO_CONFIG = new Properties();

    public static void initGlobalConfig() {
        initGlobalConfig(null);
    }

    public static void initGlobalConfig(String name) {
        try {
            if (name != null && !name.trim().isEmpty()) {
                DAO_CONFIG.load(new FileReader(name));
            } else {
                try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(CONFIG_NAME)) {
                    if (inputStream != null) {
                        DAO_CONFIG.load(inputStream);
                    } else {
                        throw new IOException("Properties file not found");
                    }
                } catch (IOException e) {
                    Logger.getLogger(DaoConfig.class.getName()).log(Level.SEVERE, e.getMessage());
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(DaoConfig.class.getName()).log(Level.SEVERE, e.getMessage());
        }
    }

    public static String getProperty(String property) {
        return DAO_CONFIG.getProperty(property);
    }
}