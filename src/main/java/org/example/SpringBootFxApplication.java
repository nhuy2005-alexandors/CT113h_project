package org.example;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringBootFxApplication {

    public static ConfigurableApplicationContext startSpring() {
        return new SpringApplicationBuilder(SpringBootFxApplication.class)
                .run();
    }

    public static void main(String[] args) {

    }
}
