package com.lecture.coordinator.model;

import java.util.Properties;

public class ExtenderExample {
    private String details;
    private String html;
    private String key;
    private String link;
    private String name;
    private String value;

    public ExtenderExample(String key, Properties properties) {
        this.key = key;
        this.details = properties.getProperty(key + ".details");
        this.html = properties.getProperty(key + ".html");
        this.link = properties.getProperty(key + ".link");
        this.name = properties.getProperty(key + ".name");
        this.value = properties.getProperty(key + ".value");
    }

    public String getDetails() {
        return details;
    }

    public String getHtml() {
        return html;
    }

    public String getKey() {
        return key;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}