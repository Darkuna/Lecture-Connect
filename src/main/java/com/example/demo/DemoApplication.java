package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        //Man kann Spring Boot beim Startup konfigurieren
        /*
        SpringApplication app = new SpringApplication(DemoApplication.class);
        app.setHeadless(false);
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.run(args);
        */

        //Oder mit dem ApplicationBuilder
        /*
        new SpringApplicationBuilder( DemoApplication.class )
                .headless( false )
                .bannerMode( Banner.Mode.OFF )
                .logStartupInfo( false )
                .run( args );
        */
        SpringApplication.run(DemoApplication.class, args);

        //Code zum Auslesen aller managed Beans
        /*
        ApplicationContext ctx =
                SpringApplication.run( DemoApplication.class, args );
        Arrays.stream( ctx.getBeanDefinitionNames() )
                .sorted()
                .forEach( System.out::println );
         */
    }
}
