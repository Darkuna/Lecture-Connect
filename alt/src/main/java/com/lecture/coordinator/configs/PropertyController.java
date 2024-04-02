package com.lecture.coordinator.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property")
public class PropertyController {

    @Value("${environment.name}")
    private String environmentName;

    @GetMapping
    public String getEnvironmentName(){
        return environmentName;
    }
}
