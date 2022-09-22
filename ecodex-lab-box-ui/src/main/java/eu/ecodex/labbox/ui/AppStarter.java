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
        if (!javaVersion.startsWith("1.8")) {
            System.err.println("WARNING: This Application requires Java 8. Your version is: " + javaVersion);
        }
        new SpringApplicationBuilder(AppStarter.class).headless(false).run(args);
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
