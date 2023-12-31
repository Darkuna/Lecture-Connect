package com.lecture.coordinator.configs;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for servlet context.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Configuration
public class CustomServletContextInitializer implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        sc.setInitParameter("com.sun.faces.forceLoadConfiguration", "true");
        sc.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
        sc.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        sc.setInitParameter("javax.faces.STATE_SAVING_METHOD", "server");
        sc.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
    }
}
