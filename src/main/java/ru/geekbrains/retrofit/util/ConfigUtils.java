package ru.geekbrains.retrofit.util;

import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class ConfigUtils {
    Properties prop = new Properties();

    public String getBaseUrl(){
        try {
            prop.load(new FileInputStream("src/test/resources/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty("url");
    }
}
