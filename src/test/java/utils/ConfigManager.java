
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static Properties properties;

    static {
        properties = new Properties();
        String path = "src/test/resources/config.properties";
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut încărca fișierul de configurare: " + path, e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}