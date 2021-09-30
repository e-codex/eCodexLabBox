package eu.ecodex.labbox.ui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication(scanBasePackages = "eu.ecodex.labbox.ui")
@EnableAsync
public class AppStarter {
    public static void main(String[] args) {
        final String javaVersion = System.getProperty("java.version");

        if (javaVersion.startsWith("1.8")) {
//             .headless(false) is needed to open File Browser via Java Desktop API
            new SpringApplicationBuilder(AppStarter.class).headless(false).run(args);
        } else {
            System.out.println("This Application requires Java 8. Your version is: " + javaVersion);
        }
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(1024);
        executor.setThreadNamePrefix("WatchDirectory-");
        executor.initialize();
        return executor;
    }
}
