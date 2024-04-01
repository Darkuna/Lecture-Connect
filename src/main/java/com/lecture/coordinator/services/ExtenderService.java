package com.lecture.coordinator.services;

import com.lecture.coordinator.model.ExtenderExample;

import javax.faces.bean.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@ApplicationScoped
public class ExtenderService {

    public Map<String, ExtenderExample> createExtenderExamples() {
        Properties properties = new Properties();

        try (InputStream inStream = ExtenderService.class.getResourceAsStream("/schedule-extender-examples.properties")) {
            properties.load(inStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, ExtenderExample> extenderExamples = new HashMap<>();

        for (String key : properties.stringPropertyNames()) {
            if (key != null && key.endsWith(".name")) {
                String baseKey = key.substring(0, key.length() - 5);
                ExtenderExample example = new ExtenderExample(baseKey, properties);
                if (example.getName() != null && example.getValue() != null && !example.getName().trim().isEmpty()
                        && !example.getValue().trim().isEmpty()) {
                    extenderExamples.put(baseKey, example);
                }
            }
        }

        return extenderExamples;
    }
}
