package eu.ecodex.labbox.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "eu.ecodex.labbox.ui")
public class AppStarter {
    public static void main(String[] args) {
        // .headless(false) is needed to open File Browser via Java Desktop API
        new SpringApplicationBuilder(AppStarter.class).headless(false).run(args);
    }
}
